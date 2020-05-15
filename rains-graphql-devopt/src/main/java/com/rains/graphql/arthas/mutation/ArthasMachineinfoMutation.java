package com.rains.graphql.arthas.mutation;


import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.arthas.service.impl.MachineSshSvc;
import com.rains.graphql.arthas.ssh.SshTool;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.exception.ValidationException;
import com.rains.graphql.common.utils.SpringContextUtil;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * (ArthasMachineinfo)表控制层
 *
 * @author hugoDD
 * @since 2020-05-12 10:52:16
 */
@Slf4j
@Component
public class ArthasMachineinfoMutation implements GraphQLMutationResolver {
    /**
     * 服务对象
     */
    @Autowired
    private ArthasMachineinfoService machineinfoService;
    @Autowired
    private MachineSshSvc machineSshSvc;

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("arthasMachineinfo:[#request.opt]")
    public boolean arthasMachineinfoBaseMutation(QueryRequest request, DataFetchingEnvironment env) {

        if ("export".equals(request.getOpt())) {
            Consumer<QueryRequest> exportOpt = q -> machineinfoService.export(q, env);
            request.opt(request.getOpt(), exportOpt);
        }


        return machineinfoService.baseOpt(request);
    }

    /**
     * @param machineId 机器id
     *                  1. 检查账号密码
     *                  2. 上传arthas
     *                  3. 初始化arthas
     *                  4. 选择需要操作的应用
     */
    private void initMachine(Long machineId) {
        Connection connection = null;
        Session session = null;
        try {
            connection = machineSshSvc.getConnection(machineId);
            //上传arthas
            session = connection.openSession();
            boolean hasArthas = SshTool.checkFile(session, "meteor");
            boolean flag = false;
            if (!hasArthas) {
                //获取arthas资源
                byte[] data = SpringContextUtil.getResourceBytes("agent/meteor.tar.gz");
                if (null != data && data.length > 0) {
                    String filePath = "/tmp/meteor.tar.gz";
                    flag = SshTool.uploadAndUnTar(connection, data, filePath, "meteor");
                }


            } else {
                flag = true;
            }
            ArthasMachineinfo tmpMachineinfoEntity = new ArthasMachineinfo();
            tmpMachineinfoEntity.setId(machineId.intValue());
            //0初始状态  1 agent上传成功  3 agent上传失败
            if (flag) {
                tmpMachineinfoEntity.setServerStatus("1");
                machineinfoService.save(tmpMachineinfoEntity);
                // return XjjJson.success("meteor-agent上传成功");
            } else {
                tmpMachineinfoEntity.setServerStatus("3");
                machineinfoService.saveOrUpdate(tmpMachineinfoEntity);
                // return XjjJson.error("meteor-agent上传失败");
            }
        } catch (Exception e) {
            throw new ValidationException(e.getMessage(), e.getCause());
        } finally {
            if (null != connection) {
                connection.close();
            }
            if (null != session) {
                session.close();
            }
        }
    }


}
