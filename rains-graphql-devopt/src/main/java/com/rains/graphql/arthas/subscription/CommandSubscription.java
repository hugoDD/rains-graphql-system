package com.rains.graphql.arthas.subscription;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.rains.graphql.arthas.command.DashboardResolve;
import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.entity.commands.Dashboard;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.arthas.ssh.ArthasTelnetConsole;
import com.rains.graphql.common.graphql.GraphQlSubscription;
import com.rains.graphql.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Component
public class CommandSubscription implements GraphQlSubscription {
    @Autowired
    private ArthasMachineinfoService machineService;

    @Autowired
    private DashboardResolve dashboardResolve;

   private ArthasTelnetConsole telnetConsole;


    public Publisher<Dashboard> dashboard(long machineId) throws IOException, InterruptedException {
        telnetConsole = initArthasTelnet(machineId);
        Flux<Dashboard> dashboard = telnetConsole.batchModeRun(dashboardResolve.transform(), "dashboard -i 6000");

        return dashboard;
    }

    public ArthasTelnetConsole initArthasTelnet(long machineId) {
        ArthasMachineinfo machineInfo = machineService.getById(machineId);
        if (telnetConsole != null) {
            try {
                telnetConsole.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        String targetIp = "";
        if (!StringUtils.isEmpty(machineInfo.getArthasIp())) {
            targetIp = machineInfo.getArthasIp();
        } else if (!StringUtils.isEmpty(machineInfo.getArthasAgentId())) {
            targetIp = machineInfo.getArthasAgentId();
        }

         telnetConsole = new ArthasTelnetConsole(targetIp);
        return telnetConsole;
    }

    @PreDestroy
    public void destroy() throws IOException {
        if(telnetConsole!=null){
            telnetConsole.disconnect();
        }

    }

    @Override
    public void close()  {
        try {
            telnetConsole.disconnect();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }
}
