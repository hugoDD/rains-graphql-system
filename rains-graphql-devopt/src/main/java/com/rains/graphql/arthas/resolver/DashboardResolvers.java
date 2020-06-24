package com.rains.graphql.arthas.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.rains.graphql.arthas.entity.commands.Dashboard;
import com.rains.graphql.arthas.entity.commands.ThreadInfo;
import com.rains.graphql.arthas.entity.commands.ThreadStatistical;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DashboardResolvers implements GraphQLResolver<Dashboard> {

    private static long maxthread = 0L;



    public ThreadStatistical getThreadStatistical(Dashboard dashboard){
        ThreadStatistical statistical = new ThreadStatistical();
        statistical.setLive(dashboard.getThreads().size());
        long count =dashboard.getThreads().stream().filter(ThreadInfo::getDaemon).count();
        statistical.setDaemon(count);

        maxthread =maxthread> dashboard.getThreads().size()?maxthread:dashboard.getThreads().size();
        statistical.setPeak(maxthread);
        return statistical;
    }
}
