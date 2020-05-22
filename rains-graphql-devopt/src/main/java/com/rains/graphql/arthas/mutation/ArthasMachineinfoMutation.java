package com.rains.graphql.arthas.mutation;


import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.arthas.service.impl.MachineSshSvc;
import com.rains.graphql.arthas.ssh.SshResponse;
import com.rains.graphql.arthas.ssh.SshTool;
import com.rains.graphql.common.annotation.Log;
import com.rains.graphql.common.domain.QueryRequest;
import com.rains.graphql.common.domain.SelectData;
import com.rains.graphql.common.exception.ValidationException;
import com.rains.graphql.common.utils.SpringContextUtil;
import com.rains.graphql.common.utils.StringUtils;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public ArthasMachineinfoMutation() {

    }

    @Log("[#request.opt]操作系统日志")
    @RequiresPermissions("arthas:machineinfo:[#request.opt]")
    public boolean arthasMachineinfoBaseMutation(QueryRequest request, DataFetchingEnvironment env) {

        if ("export".equals(request.getOpt())) {
            Consumer<QueryRequest> exportOpt = q -> machineinfoService.export(q, env);
            request.opt(request.getOpt(), exportOpt);
        }

        return machineinfoService.baseOpt(request);
    }

    /**
     * @param machineIds 机器ids
     *                   1. 检查账号密码
     *                   2. 上传arthas
     *                   3. 初始化arthas
     *                   4. 返回要选择需要操作的应用
     */
    @Log("arthas安装日志")
    @RequiresPermissions("arthas:machineinfo:install")
    public List<SelectData> installArthas(Long[] machineIds) {
        List<SelectData> list = new ArrayList<>();
        if (Objects.isNull(machineIds)) {
            log.warn("machineIds is null,not machine init!!");
            return list;
        }

        for (Long machineId : machineIds) {
            Connection connection = null;
            Session session = null;
            try {
                connection = machineSshSvc.getConnection(machineId);
                //SshSessionEntity sessionEntity = machineSshSvc.createSshSession(machineId);
                //上传arthas
                session = connection.openSession();

                boolean hasArthas = SshTool.checkFile(session, "agent");
                boolean flag = false;

                ArthasMachineinfo machineInfo = machineinfoService.getById(machineId);
                machineInfo.setServerStatus("0");

                if (!hasArthas) {
                    //获取arthas资源
                    byte[] data = SpringContextUtil.getResourceBytes("agent/arthas-packaging-3.2.0-bin.tar.gz");
                    if (null != data && data.length > 0) {
                        String filePath = "/tmp/agent.tar.gz";
                        flag = SshTool.uploadAndUnTar(connection, data, filePath, "agent");
                    }
                    //0初始状态  1 agent上传成功  3 agent上传失败
                    machineInfo.setServerStatus("1");
                    machineinfoService.saveOrUpdate(machineInfo);
                }

                SshResponse sshResponse = SshTool.excuteCmd(connection, "jps");
                String result = sshResponse.getSuccessMsg();
                String[] lines = result.split("\n");

                for (String line : lines) {
                    SelectData selectData = new SelectData();
                    if (line.contains("Jps")) {
                        continue;
                    }
                    String[] processStr = line.split(" ");
                    selectData.setDictType(machineId + "");
                    selectData.setDictLabel(processStr[1]);
                    selectData.setDictValue(processStr[0]);
                    list.add(selectData);
                }


            } catch (Exception e) {
                ArthasMachineinfo tmpMachineinfoEntity = new ArthasMachineinfo();
                tmpMachineinfoEntity.setId(machineId.intValue());
                //0初始状态  1 agent上传成功  3 agent上传失败
                tmpMachineinfoEntity.setServerStatus("3");
                machineinfoService.saveOrUpdate(tmpMachineinfoEntity);
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
        return list;
    }

    public void startArthas(List<SelectData> datas) {
        for (SelectData data : datas ) {
            Long machineId = Long.parseLong(data.getDictType());
            String pid = data.getDictValue();
            ArthasMachineinfo machineInfo = machineinfoService.getById(machineId);

            String cmd = "";
            if (StringUtils.isEmpty(machineInfo.getArthasIp())) {
                cmd = "java -jar /tmp/arthas-packaging-3.2.0-bin/arthas-boot.jar "+pid+" --target-ip 0.0.0.0";
            } else if (StringUtils.isEmpty(machineInfo.getArthasAgentId())) {
                cmd = " java -jar /tmp/arthas-packaging-3.2.0-bin/arthas-boot.jar "+pid+" --tunnel-server 'ws://" + machineInfo.getArthasIp() + ":" + machineInfo.getArthasPort() + "/ws'";
            } else {
                cmd = " java -jar /tmp/arthas-packaging-3.2.0-bin/arthas-boot.jar "+pid+" --tunnel-server 'ws://" + machineInfo.getArthasIp() + ":" + machineInfo.getArthasPort() + "/ws' --agent-id ${machine.arthasAgentId}";
            }

            Connection connection = null;
            Session session = null;
            try {
                connection = machineSshSvc.getConnection(machineId);
                //上传arthas


                SshResponse sshResponse = SshTool.excuteCmd(connection, cmd);
                boolean flag =sshResponse.getSuccessMsg().contains("Attach process "+pid+" success");
                if(flag){
                    log.info("============={pid}启动成功=========================",pid);
                    log.info(sshResponse.getSuccessMsg());
                    log.info("============={pid}启动成功=========================",pid);
                }
            } catch (Exception e) {
                throw new ValidationException(e.getMessage(), e.getCause());
            } finally {
                if (null != connection) {
                    connection.close();
                }

            }
        }

    }


}
