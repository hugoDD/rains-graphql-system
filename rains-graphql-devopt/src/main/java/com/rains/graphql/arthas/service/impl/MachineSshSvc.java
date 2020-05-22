package com.rains.graphql.arthas.service.impl;

import ch.ethz.ssh2.Connection;
import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.entity.ArthasRsa;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.arthas.service.ArthasRsaService;
import com.rains.graphql.arthas.ssh.SshSessionEntity;
import com.rains.graphql.arthas.ssh.SshSessionFactory;
import com.rains.graphql.arthas.ssh.SshTool;
import com.rains.graphql.common.exception.ValidationException;
import com.rains.graphql.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class MachineSshSvc {
    @Autowired
    private ArthasMachineinfoService machineService;
    @Autowired
    private ArthasRsaService rsaService;


    public SshSessionEntity createSshSession(Long machineId){
        SshSessionEntity  sshSessionEntity=null;
        if(Objects.isNull(machineId)){
            throw new ValidationException("服务器ID不能为空");
        }
        ArthasMachineinfo machine = machineService.getById(machineId);
        String hostname = machine.getHostname();
        if (StringUtils.isBlank(hostname)) {
            throw new ValidationException("服务器地址为空");
        }

        String username = machine.getUsername();
        String password = machine.getPassword();
        Integer port = machine.getPort();
        if (port == null) {
            throw new ValidationException("连接端口为空");
        }
        if (machine.getLoginType() != null && machine.getLoginType().equals("rsa")) {
            ArthasRsa rsaEntity = rsaService.getById(machine.getRsaId().longValue());

            username = rsaEntity.getRsaUsername();
            if (StringUtils.isBlank(username)) {
                throw new ValidationException("用户名为空");
            }
            password = rsaEntity.getRsaPassword();
            String rsaValue = rsaEntity.getRsaValue();
            sshSessionEntity = SshSessionFactory.createSession(hostname, username, password,port, rsaValue.toCharArray() );

        } else {
            if (StringUtils.isBlank(username)) {
                throw new ValidationException("用户名为空");
            }
            if (StringUtils.isBlank(password)) {
                throw new ValidationException("密码为空");
            }
        }
        return sshSessionEntity;
    }


    public Connection getConnection(Long machineId) throws Exception {
        Connection connection = null;
        if (machineId != null) {
            if (machineId != null) {
                ArthasMachineinfo machine = machineService.getById(machineId);
                String hostname = machine.getHostname();
                if (StringUtils.isBlank(hostname)) {
                    throw new ValidationException("服务器地址为空");
                }

                String username = machine.getUsername();
                String password = machine.getPassword();
                Integer port = machine.getPort();
                if (port == null) {
                    throw new ValidationException("连接端口为空");
                }
                if (machine.getLoginType() != null && machine.getLoginType().equals("rsa")) {
                    ArthasRsa rsaEntity = rsaService.getById(machine.getRsaId().longValue());

                    username = rsaEntity.getRsaUsername();
                    if (StringUtils.isBlank(username)) {
                        throw new ValidationException("用户名为空");
                    }
                    password = rsaEntity.getRsaPassword();
                    String rsaValue = rsaEntity.getRsaValue();
                    connection = SshTool.getConnection(hostname, username, port, rsaValue.toCharArray(), password);

                } else {
                    if (StringUtils.isBlank(username)) {
                        throw new ValidationException("用户名为空");
                    }
                    if (StringUtils.isBlank(password)) {
                        throw new ValidationException("密码为空");
                    }
                    connection = SshTool.getConnection(hostname, username, password, port);
                }

            } else {
                throw new ValidationException("服务器ID不能为空");
            }
        }
        return connection;
    }
}
