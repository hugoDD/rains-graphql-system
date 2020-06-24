package com.rains.graphql.arthas.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.arthas.command.GeneralCommandResolve;
import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.entity.commands.Bean;
import com.rains.graphql.arthas.entity.commands.General;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.arthas.ssh.ArthasTelnetConsole;
import com.rains.graphql.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class ArthasCommandsQuery implements GraphQLQueryResolver {
    @Autowired
    private ArthasMachineinfoService machineService;

    private ArthasTelnetConsole telnetConsole;

    @Autowired
    private GeneralCommandResolve commandResolve;


    public List<General> generalCommand(Long machineId, String cmd) throws IOException {
        initArthasTelnet(machineId);
        List<General> generalList = new ArrayList<>();
        telnetConsole.batchModeRun(commandResolve.transform(), cmd).subscribe(generalList::addAll);


        return generalList;
    }

    public List<Bean> scCommand(Long machineId, String cmd) throws IOException {
        initArthasTelnet(machineId);
        List<Bean> beans = new ArrayList<>();

        Function<List<String>,  List<Bean>> transform = null;
        if(cmd.indexOf("-d") > 0){

        }else {
            transform =rs -> {
                rs.remove(0);
                rs.remove(rs.size()-1);
                rs.remove(rs.size()-1);
                for(String s : rs){
                    Bean bean = new Bean();
                    bean.setName(s);
                    bean.setSimpleName(s.substring(s.lastIndexOf(".")));
                    beans.add(bean);
                }
                return beans;
            };

        }
        telnetConsole.batchModeRun(transform, cmd).subscribe();
        return beans;
    }

    public void initArthasTelnet(long machineId) {
        ArthasMachineinfo machineInfo = machineService.getById(machineId);
        if (telnetConsole != null ) {

            telnetConsole.reconnect();
        } else {
            String targetIp = "";
            if (StringUtils.isNotEmpty(machineInfo.getArthasIp())) {
                targetIp = machineInfo.getArthasIp();
            } else if (StringUtils.isNotEmpty(machineInfo.getArthasAgentId())) {
                targetIp = machineInfo.getArthasAgentId();
            }
            telnetConsole = new ArthasTelnetConsole(targetIp);
        }
    }
}
