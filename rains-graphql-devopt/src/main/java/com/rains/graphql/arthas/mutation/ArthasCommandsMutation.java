package com.rains.graphql.arthas.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.arthas.command.GeneralCommandResolve;
import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.entity.commands.General;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.arthas.ssh.ArthasTelnetConsole;
import com.rains.graphql.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArthasCommandsMutation implements GraphQLMutationResolver {
    @Autowired
    private ArthasMachineinfoService machineService;

    private ArthasTelnetConsole telnetConsole;


    public List<General> postCommand(Long machineId, String cmd) throws IOException {
        initArthasTelnet(machineId);
        List<General> generalList = new ArrayList<>();
        telnetConsole.batchModeRun(rs -> {
            General general = new General();
            for (String r : rs) {
                if(r.contains("=")) {
                    String[] arr = r.split("=");
                    general.setKey(arr[0]);
                    general.setValue(arr[1]);
                    generalList.add(general);
                }
            }
            return generalList;
        }, cmd).subscribe();


        return generalList;
    }

    public void initArthasTelnet(long machineId) {
        ArthasMachineinfo machineInfo = machineService.getById(machineId);
        if (telnetConsole != null) {

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
