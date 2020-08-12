import com.rains.graphql.arthas.entity.commands.*;
import com.rains.graphql.arthas.ssh.ArthasTelnetConsole;
import com.rains.graphql.common.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArthasTelnetConsoleTest {
    private final static String REGEX = "\\s+";
    //"EXAMPLES:\n
    // java -jar arthas-client.jar 127.0.0.1 3658\n
    // java -jar arthas-client.jar -c 'dashboard -n 1' \n
    // java -jar arthas-client.jar -f batch.as 127.0.0.1\n"
    String ip = "172.25.191.228";
  //  String ip = "172.26.239.129";
    private int port = 3658;

    @Test
    public void connect() throws IOException, InterruptedException {
        ArthasTelnetConsole telnetConsole = new ArthasTelnetConsole(ip);
        telnetConsole.batchModeRun(rs -> rs, "mbean").subscribe(System.out::print);
        // System.out.println("=================================================黄金分割线==============================================================");
//        telnetConsole.cmdRun( "monitor -c 5 com.ly.mp.auth.service.LoginService sysconfig").subscribe(System.out::print);
//        List<MBean> list = new ArrayList<>();
//        telnetConsole.batchModeRun(rs -> {
//            System.out.println(rs);
//            rs.remove(rs.size() - 1);
//            rs.remove(0);
////            rs.stream().collect(Collectors.groupingBy(s -> s.split(":")[0], Collectors.mapping(t -> t.split(":")[1], Collectors.toList()))).forEach((k, v) -> {
////                MBean bean = new MBean();
////                bean.setMBeanPre(k);
////                bean.setMBeanSuffix(v);
////
////            });
//            return list;
//        }, "mbean");
//        System.out.println(list);

        telnetConsole.disconnect();
    }


    @Test
    public void runCmd() throws IOException {
        ArthasTelnetConsole telnetConsole = new ArthasTelnetConsole(ip);
        List<General> list = new ArrayList<>();
       // telnetConsole.simpleCmdRun("logger --name ROOT --level warn");
        Flux<List<General>> flux = telnetConsole.batchModeRun(rs -> {
            rs.remove(rs.size()-1);
            rs.remove(0);

          Iterator<String> it =  rs.iterator();
          String header = it.next();
          List<Integer> headerIndex = chartHeaderPoit(header);

          it.next();
            General tmp =null;
            while(it.hasNext()){
              String s = it.next();
              if(StringUtils.isEmpty(s)){
                  continue;
              }
              General general = new General();

              String key = s.substring(0,headerIndex.get(1)-1).trim();
              if(StringUtils.isEmpty(key) && tmp!=null){
                  general.setValue(tmp.getValue()+s.substring(headerIndex.get(1)).trim());
              }else {
                  tmp = general;
                  general.setKey(s.substring(0,headerIndex.get(1)-1).trim());
                  general.setValue(s.substring(headerIndex.get(1)).trim());
              }

              list.add(general);
          }
            return list;
        }, "mbean com.alibaba.druid:type=DruidDataSource,id=mysql_mp");

        flux.subscribe();
        System.out.println(list);

//        System.out.println(list);
//        fluxcmd.subscribe(c->{
//            System.out.println(c);
//        });
        telnetConsole.disconnect();
        //StepVerifier.create(dashboard).expectNext()

    }

    protected List<Integer> chartHeaderPoit(String header) {
        String[] names = header.trim().split(REGEX);
        List<Integer> poits = new ArrayList<>();
        for (String name : names) {
            int index = header.indexOf(name);
            poits.add(index);

        }
        return poits;

    }


    /*os.name                                                                    Linux
    os.version                                                                 2.6.32-358.el6.x86_64
    java.version                                                               1.8.0_65
    java.home                                                                  /usr/java/jdk1.8.0_65/jre
    systemload.average                                                         0.08
    processors                                                                 4
    uptime                                                                     175553s*/
    private RuntimeInfo resolveRuntime(List<String[]> values) {

        RuntimeInfo info = new RuntimeInfo();
        info.setOsName(values.get(0)[1]);
        info.setOsVersion(values.get(1)[1]);
        info.setJavaVersion(values.get(2)[1]);
        info.setJavaHome(values.get(3)[1]);
        info.setSystemloadAverage(values.get(4)[1]);
        info.setProcessors(values.get(5)[1]);
        info.setUptime(values.get(6)[1]);

        return info;
    }


    private ThreadInfo resolveThead(String[] names, Map<String, Integer> index, String line) {
        //ID          NAME                                  GROUP                    PRIORITY    STATE       %CPU         TIME        INTERRUPTED  DAEMON


        ThreadInfo threadInfo = new ThreadInfo();
        threadInfo.setId(Long.parseLong(line.substring(index.get(names[0]), index.get(names[1]) - 1).trim()));
        threadInfo.setName(line.substring(index.get(names[1]), index.get(names[2]) - 1).trim());
        threadInfo.setGroup(line.substring(index.get(names[2]), index.get(names[3]) - 1).trim());
        threadInfo.setPriority(Integer.parseInt(line.substring(index.get(names[3]), index.get(names[4]) - 1).trim()));
        threadInfo.setState(line.substring(index.get(names[4]), index.get(names[5]) - 1).trim());
        threadInfo.setCpu(line.substring(index.get(names[5]), index.get(names[6]) - 1).trim());
        threadInfo.setTime(line.substring(index.get(names[6]), index.get(names[7]) - 1).trim());
        threadInfo.setInterrupted(Boolean.valueOf(line.substring(index.get(names[7]), index.get(names[8]) - 1).trim()));
        threadInfo.setDaemon(Boolean.valueOf(line.substring(index.get(names[8])).trim()));

        return threadInfo;
    }

    private JvmMemory resolveMemory(List<String[]> values) {
        JvmMemory jvmMemory = new JvmMemory();

        GC gc = new GC();

        Memory heap = new Memory();
        String[] arr = values.get(0);
        heap.setUsed(arr[1]);
        heap.setTotal(arr[2]);
        heap.setMax(arr[3]);
        heap.setUsage(arr[4]);
        gc.setScavengeCount(Long.parseLong(arr[6]));

        Memory edenSpace = new Memory();
        arr = values.get(1);
        edenSpace.setUsed(arr[1]);
        edenSpace.setTotal(arr[2]);
        edenSpace.setMax(arr[3]);
        edenSpace.setUsage(arr[4]);
        gc.setScavengeTime(Integer.parseInt(arr[6]));

        Memory survivorSpace = new Memory();
        arr = values.get(2);
        survivorSpace.setUsed(arr[1]);
        survivorSpace.setTotal(arr[2]);
        survivorSpace.setMax(arr[3]);
        survivorSpace.setUsage(arr[4]);
        gc.setMarksweepCount(Integer.parseInt(arr[6]));

        Memory oldGen = new Memory();
        arr = values.get(3);
        oldGen.setUsed(arr[1]);
        oldGen.setTotal(arr[2]);
        oldGen.setMax(arr[3]);
        oldGen.setUsage(arr[4]);
        gc.setMarksweepTime(Integer.parseInt(arr[6]));

        Memory nonHeap = new Memory();
        arr = values.get(4);
        nonHeap.setUsed(arr[1]);
        nonHeap.setTotal(arr[2]);
        nonHeap.setMax(arr[3]);
        nonHeap.setUsage(arr[4]);

        Memory codeCache = new Memory();
        arr = values.get(5);
        codeCache.setUsed(arr[1]);
        codeCache.setTotal(arr[2]);
        codeCache.setMax(arr[3]);
        codeCache.setUsage(arr[4]);

        Memory metaspace = new Memory();
        arr = values.get(6);
        metaspace.setUsed(arr[1]);
        metaspace.setTotal(arr[2]);
        metaspace.setMax(arr[3]);
        metaspace.setUsage(arr[4]);

        Memory compressedClassSpace = new Memory();
        arr = values.get(7);
        compressedClassSpace.setUsed(arr[1]);
        compressedClassSpace.setTotal(arr[2]);
        compressedClassSpace.setMax(arr[3]);
        compressedClassSpace.setUsage(arr[4]);

        Memory direct = new Memory();
        arr = values.get(8);
        direct.setUsed(arr[1]);
        direct.setTotal(arr[2]);
        direct.setMax(arr[3]);
        direct.setUsage(arr[4]);

        Memory mapped = new Memory();
        arr = values.get(9);
        mapped.setUsed(arr[1]);
        mapped.setTotal(arr[2]);
        mapped.setMax(arr[3]);
        mapped.setUsage(arr[4]);

        jvmMemory.setHeap(heap);
        jvmMemory.setEdenSpace(edenSpace);
        jvmMemory.setSurvivorSpace(survivorSpace);
        jvmMemory.setOldGen(oldGen);
        jvmMemory.setNonheap(nonHeap);
        jvmMemory.setCodeCache(codeCache);
        jvmMemory.setMetaspace(metaspace);
        jvmMemory.setCompressedClassSpace(compressedClassSpace);
        jvmMemory.setDirect(direct);
        jvmMemory.setMapped(mapped);

        jvmMemory.setGc(gc);

        return jvmMemory;
    }


    private Function<String, Dashboard> mapFun() {
        Function<String, Dashboard> map = rs -> {
            Dashboard dashboard = new Dashboard();
            try {
                // InputStream inputStream = IOUtils.toInputStream(rs, "UTF-8");
                StringReader strReader = new StringReader(rs);

                // final BufferedReader reader = IOUtils.toBufferedReader(strReader);
                List<String> lineList = IOUtils.readLines(strReader);
                Iterator<String> lineIterator = lineList.iterator();
                String line = lineIterator.next();
                dashboard.setCommand(line);
                line = lineIterator.next();
                Map<String, Integer> threadIndex = chartPoit(line);
                String[] names = line.split(REGEX);
                List<ThreadInfo> threadList = new ArrayList<>();
                List<String[]> memoryList = new ArrayList<>();
                List<String[]> runtimeList = new ArrayList<>();

                String flag = "Thread";

                while (lineIterator.hasNext()) {

                    line = lineIterator.next();
                    if (null == line) {
                        break;
                    }
                    if (StringUtils.isEmpty(line)) {
                        continue;
                    }
                    if (line.startsWith("Process ends after")) {
                        break;
                    }
                    if (line.startsWith("Memory") && "Thread".equals(flag)) {
                        names = line.split(REGEX);
                        line = lineIterator.next();
                        flag = "Memory";
                    } else if (line.startsWith("Runtime") && "Memory".equals(flag)) {
                        names = line.split(REGEX);
                        line = lineIterator.next();
                        flag = "Runtime";
                    }
                    String[] arr = line.split(REGEX);
                    switch (flag) {
                        case "Thread": {
                            ThreadInfo threadInfo = resolveThead(names, threadIndex, line);
                            threadList.add(threadInfo);
                            break;
                        }
                        case "Memory":
                            memoryList.add(arr);
                            break;
                        case "Runtime": {
                            runtimeList.add(arr);
                            break;
                        }
                    }


                }
                JvmMemory jvmMemory = resolveMemory(memoryList);
                RuntimeInfo runtimeInfo = resolveRuntime(runtimeList);

                dashboard.setThreads(threadList);
                dashboard.setMemory(jvmMemory);
                dashboard.setRuntimeInfo(runtimeInfo);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return dashboard;
        };
        return map;
    }

    private Map<String, Integer> chartPoit(String header) {
        String[] names = header.split(REGEX);
        Map<String, Integer> indexMap = new HashMap<>();
        for (String name : names) {
            int index = header.indexOf(name);
            indexMap.put(name, index);

        }
        return indexMap;

    }
}
