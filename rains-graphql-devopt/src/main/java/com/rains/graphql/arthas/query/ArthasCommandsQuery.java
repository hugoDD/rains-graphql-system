package com.rains.graphql.arthas.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.rains.graphql.arthas.command.GeneralCommandResolve;
import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.entity.commands.*;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.arthas.ssh.ArthasTelnetConsole;
import com.rains.graphql.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ArthasCommandsQuery implements GraphQLQueryResolver {

    protected final static String REGEX = "\\s+";
    protected static final String PROMPT = "[arthas@";
    @Autowired
    private ArthasMachineinfoService machineService;

    private ArthasTelnetConsole telnetConsole;

    @Autowired
    private GeneralCommandResolve commandResolve;

    public List<MBean> mbeanCommand(Long machineId, String cmd) throws IOException {
        initArthasTelnet(machineId);
        List<MBean> list = new ArrayList<>();

        telnetConsole.batchModeRun(rs -> {
            rs.remove(rs.size() - 1);
            rs.remove(0);
            rs.stream().collect(Collectors.groupingBy(s -> s.split(":")[0], Collectors.mapping(t -> t.split(":")[1], Collectors.toList()))).forEach((k, v) -> {
                MBean bean = new MBean();
                bean.setMBeanPre(k);
                bean.setMBeanSuffix(v);
                list.add(bean);
            });
            return list;
        }, cmd).subscribe();


        return list;
    }

    public List<ThreadInfo> threadInfo(Long machineId, String cmd) throws IOException {
        initArthasTelnet(machineId);
        List<ThreadInfo> list = new ArrayList<>();
        Flux<List<ThreadInfo>> flux = telnetConsole.batchModeRun(rs -> {
            rs.remove(rs.size() - 1);
            rs.remove(rs.size() - 1);
            Iterator<String> it = rs.iterator();
            it.next();//commond
            String header = it.next();
            while (it.hasNext()) {
                if (StringUtils.isEmpty(header)) {
                    continue;
                }
                ThreadInfo bean = new ThreadInfo();
                bean.setName(header.substring(header.indexOf("\"") + 1, header.lastIndexOf("\"")));
                String[] arr = header.substring(header.lastIndexOf("\"") + 1).trim().split(" ");
                bean.setId(Long.parseLong(arr[0].replace("Id=", "")));
                bean.setCpu(arr[1].replace("cpuUsage=", ""));
                bean.setState(arr[2]);
                if (arr.length > 3 && "on".equals(arr[3])) {
                    bean.setLockName(arr[4]);
                } else if (arr.length > 3 && arr[3].contains("in")) {
                    bean.setInNative(true);
                }
                List<String> stackTrace = new ArrayList<>();
                while (it.hasNext()) {
                    String t = it.next();
                    if (t.contains("\"")) {
                        header = t;
                        break;
                    }

                    stackTrace.add(t);

                }
                bean.setStackTrace(stackTrace);
                list.add(bean);
            }
            return list;
        }, cmd);
        flux.subscribe();
        return list;
    }

    public List<LoggerBean> loggerCommand(Long machineId, String cmd) throws IOException {
        initArthasTelnet(machineId);
        List<LoggerBean> loggerBeansList = new ArrayList<>();

        if (cmd.contains("--level")) {
            telnetConsole.simpleCmdRun(cmd);
            cmd = "logger";
        }

        Function<List<String>, List<LoggerBean>> transform = rs -> {
            rs.remove(rs.size() - 1);
            Iterator<String> it = rs.iterator();
            it.next();//commond
            String header = it.next();
            List<Integer> poit = chartHeaderPoit(header.trim());
            LoggerBean bean = new LoggerBean();
            List<LoggerLevel> levels = new ArrayList<>();
            bean.setName(header.substring(poit.get(1)).trim());
            while (it.hasNext()) {
                String s = it.next();
                if (s.trim().startsWith("name")) {
                    bean = new LoggerBean();
                    bean.setName(s.substring(poit.get(1)).trim());//header
                    it.next();//class
                    levels = new ArrayList<>();
                }
                it.next();//classLoader
                it.next();//classLoaderHash
                s = it.next();// level
                LoggerLevel level = new LoggerLevel();
                level.setInstanceId("machinelId:" + machineId);
                level.setConfiguredLevel(s.substring(poit.get(1)).trim());
                s = it.next();// effectiveLevel
                level.setEffectiveLevel(s.substring(poit.get(1)).trim());
                levels.add(level);
                bean.setLevel(levels);
                while (StringUtils.isNotEmpty(it.next())) {

                }

                loggerBeansList.add(bean);

            }

            return loggerBeansList;
        };

        telnetConsole.batchModeRun(transform, cmd).subscribe();

        return loggerBeansList;
    }

    //
//    Affect(row-cnt:1) cost in 46 ms.
    public List<General> generalCommand(Long machineId, String cmd) throws IOException {
        initArthasTelnet(machineId);
        List<General> generalList = new ArrayList<>();
        if (cmd.contains("mbean")) {
            telnetConsole.batchModeRun(rs -> {
                rs.remove(rs.size() - 1);
                rs.remove(0);
                Iterator<String> it = rs.iterator();
                String header = it.next();
                List<Integer> headerIndex = chartHeaderPoit(header);
                it.next();
                General tmp = null;
                while (it.hasNext()) {
                    String s = it.next();
                    if (StringUtils.isEmpty(s)) {
                        continue;
                    }

                    String key = s.substring(0, headerIndex.get(1) - 1).trim();
                    if (StringUtils.isEmpty(key) && tmp != null) {
                        tmp.setValue(tmp.getValue() + s.substring(headerIndex.get(1)).trim());
                    } else {
                        General general = new General();
                        tmp = general;
                        general.setKey(s.substring(0, headerIndex.get(1) - 1).trim());
                        general.setValue(s.substring(headerIndex.get(1)).trim());
                        generalList.add(general);
                    }

                }
                return generalList;
            }, cmd).subscribe();
        } else {
            telnetConsole.batchModeRun(commandResolve.transform(), cmd).subscribe(generalList::addAll);
        }

        return generalList;
    }

    public List<Bean> scCommand(Long machineId, String cmd) throws IOException {
        initArthasTelnet(machineId);
        List<Bean> beans = new ArrayList<>();
        Function<List<String>, List<Bean>> transform = null;
        String preStr = "declaring-class";
        if (cmd.startsWith("jad")) {
            transform = rs -> {
                rs.remove(rs.size() - 1);
                rs.remove(rs.size() - 1);

                StringBuilder sb = new StringBuilder();
                Iterator<String> it = rs.iterator();
                it.next();
                while (it.hasNext()) {
                    String s = it.next();
                    if (StringUtils.isEmpty(s)) {
                        continue;
                    }
                    sb.append(s).append(System.lineSeparator());
                }
                Bean bean = new Bean();
                bean.setDetail(sb.toString());
                beans.add(bean);
                return beans;
            };
        } else if (cmd.startsWith("sc -d")) {
            transform = rs -> {
                StringBuilder sb = new StringBuilder();
                rs.remove(rs.size() - 1);
                for (String s : rs) {
                    sb.append(s).append(System.lineSeparator());
                }
                Bean bean = new Bean();
                bean.setDetail(sb.toString());
                System.out.println(sb.toString());
                beans.add(bean);
                return beans;
            };
        } else if (cmd.startsWith("sm -d")) {
            transform = rs -> {
                rs.remove(rs.size() - 1);
                rs.remove(rs.size() - 1);

                StringBuilder sb = new StringBuilder();
                Iterator<String> it = rs.iterator();
                it.next();
                String header = it.next();
                List<Integer> index = chartHeaderPoit(header);
                List<Method> methods = new ArrayList<>();
                Method method = new Method();
                method.setDeclaringClass(header.substring(index.get(1)).trim());
                while (it.hasNext()) {
                    String s = it.next();
                    if (StringUtils.isEmpty(s)) {
                        continue;
                    }
                    if (s.indexOf(preStr) > -1) {
                        header = s;
                        index = chartHeaderPoit(header);
                        method = new Method();
                        method.setDeclaringClass(s.substring(index.get(1)).trim());
                        s = it.next();
                    }
                    method.setMethodName(s.substring(index.get(1)).trim());
                    method.setModifier(it.next().substring(index.get(1)).trim());
                    method.setAnnotation(it.next().substring(index.get(1)).trim());
                    List<String> params = new ArrayList<>();
                    params.add(it.next().substring(index.get(1)).trim());
                    while (it.hasNext()) {
                        s = it.next();
                        if (StringUtils.isNotEmpty(s.substring(index.get(0), index.get(1)))) {
                            break;
                        }
                        params.add(s.substring(index.get(1)).trim());
                    }
                    method.setParameters(params);
//                s = it.next();
                    if ("return".equals(s.substring(index.get(0), index.get(1)).trim())) {
                        method.setReturnObj(s.substring(index.get(1)).trim());
                        s = it.next();
                    }

                    method.setExceptions(s.substring(index.get(1)).trim());
                    method.setClassLoaderHash(it.next().substring(index.get(1)).trim());

                    methods.add(method);
                }
                Bean bean = new Bean();
                bean.setMethods(methods);
                bean.setDetail(sb.toString());
                beans.add(bean);
                return beans;
            };
        } else if (cmd.startsWith("sm")) {
            transform = rs -> {
                rs.remove(rs.size() - 1);
                rs.remove(rs.size() - 1);
                Set<String> methodSign = new HashSet<>();
                for (String s : rs) {
                    String[] arr = s.split(REGEX);
                    if (arr.length == 2 && !arr[1].contains("$")) {
                        methodSign.add(arr[1]);
                    }
                }
                Bean bean = new Bean();
                bean.setMethodSign(methodSign);
                beans.add(bean);
                return beans;
            };
        } else {
            transform = rs -> {
                rs.remove(0);
                rs.remove(rs.size() - 1);
                rs.remove(rs.size() - 1);
                for (String s : rs) {
                    if (s.contains("$$EnhancerBySpringCGLIB$$")) {
                        continue;
                    }
                    Bean bean = new Bean();
                    bean.setName(s);
                    bean.setSimpleName(s.substring(s.lastIndexOf(".") + 1));
                    beans.add(bean);
                }
                return beans;
            };

        }
        telnetConsole.batchModeRun(transform, cmd).subscribe();
        telnetConsole.disconnect();
        return beans.stream().distinct().collect(Collectors.toList());
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

    private String getValue(Iterator<String> iterator) {
        String s = iterator.next();
        String[] arr = s.split(REGEX);
        return arr[1];
    }

    private Boolean getBoolean(Iterator<String> iterator) {
        String s = iterator.next();
        String[] arr = s.split(REGEX);
        return Boolean.valueOf(arr[1]);
    }


    public void initArthasTelnet(long machineId) {
        ArthasMachineinfo machineInfo = machineService.getById(machineId);
//        if (telnetConsole != null ) {
//
//            telnetConsole.reconnect();
//        } else {
        String targetIp = "";
        if (StringUtils.isNotEmpty(machineInfo.getArthasIp())) {
            targetIp = machineInfo.getArthasIp();
        } else if (StringUtils.isNotEmpty(machineInfo.getArthasAgentId())) {
            targetIp = machineInfo.getArthasAgentId();
        }
        telnetConsole = new ArthasTelnetConsole(targetIp);
//        }
    }
}
