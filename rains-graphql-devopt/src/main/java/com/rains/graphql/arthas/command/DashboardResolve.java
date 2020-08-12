package com.rains.graphql.arthas.command;

import com.rains.graphql.arthas.entity.commands.*;
import com.rains.graphql.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
public class DashboardResolve extends BaseCmdResolve{



    private ThreadInfo resolveThead(String[] names, Map<String, Integer> index, String line) {
        //ID          NAME                                  GROUP                    PRIORITY    STATE       %CPU         TIME        INTERRUPTED  DAEMON


        try {
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(Long.parseLong(line.substring(index.get(names[0]), index.get(names[1]) - 1).trim()));
            threadInfo.setName(line.substring(index.get(names[1]), index.get(names[2]) - 1).trim());
            threadInfo.setGroup(line.substring(index.get(names[2]), index.get(names[3]) - 1).trim());
            threadInfo.setPriority(Integer.parseInt(line.substring(index.get(names[3]), index.get(names[4]) - 1).trim()));
            String state = line.substring(index.get(names[4]), index.get(names[5]) - 1).trim();
            state = "TIMED_WAI".equals(state)?"TIMED_WAITING":state;
            threadInfo.setState(state);
            threadInfo.setCpu(line.substring(index.get(names[5]), index.get(names[6]) - 1).trim());
            threadInfo.setTime(line.substring(index.get(names[6]), index.get(names[7]) - 1).trim());
            threadInfo.setInterrupted(Boolean.valueOf(line.substring(index.get(names[7]), index.get(names[8]) - 1).trim()));
            threadInfo.setDaemon(Boolean.valueOf(line.substring(index.get(names[8])).trim()));

            return threadInfo;
        } catch (RuntimeException e) {
            log.error("error line :{}", line);
            log.error(e.getMessage(), e);
        }
        return null;
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


    public Function<List<String>, Dashboard> transform() {
        Function<List<String>, Dashboard> map = lineList -> {
            Dashboard dashboard = new Dashboard();

            // System.out.println(rs);
            // InputStream inputStream = IOUtils.toInputStream(rs, "UTF-8");
            //StringReader strReader = new StringReader(rs);

            // final BufferedReader reader = IOUtils.toBufferedReader(strReader);
            lineList.removeIf(StringUtils::isEmpty);

         //   Collections.reverse(lineList);
           Iterator<String> lineIterator = lineList.iterator();
//
//            String line = lineIterator.next();
            dashboard.setCommand(lineIterator.next());

            String header = lineIterator.next();
            Map<String, Integer> threadIndex = chartPoit(header);
            String[] names = header.split(REGEX);

            List<ThreadInfo> threadList = new ArrayList<>();
            List<String[]> memoryList = new ArrayList<>();
            List<String[]> runtimeList = new ArrayList<>();

            String flag = "Thread";

            while (lineIterator.hasNext()) {

               String line = lineIterator.next();
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
                        if (threadInfo != null) {
                            threadList.add(threadInfo);
                        }
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


            return dashboard;
        };
        return map;
    }



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
}
