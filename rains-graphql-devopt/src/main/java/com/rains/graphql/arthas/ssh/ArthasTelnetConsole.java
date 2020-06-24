package com.rains.graphql.arthas.ssh;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetOptionHandler;
import org.apache.commons.net.telnet.WindowSizeOptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Slf4j
public class ArthasTelnetConsole {
    private static final String PROMPT = "[arthas@"; // [arthas@49603]$
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000; // 5000 ms

    private static final byte CTRL_C = 0x03;
    final TelnetClient telnet = new TelnetClient();
    private boolean help = false;
    private String targetIp = "127.0.0.1";
    private int port = 3658;
    private int timeout = DEFAULT_CONNECTION_TIMEOUT;


    private String header;

    private String pid;


    public ArthasTelnetConsole() {
        init();
    }

    public ArthasTelnetConsole(String targetIp) {
        this.targetIp = targetIp;
        init();
    }

    public ArthasTelnetConsole(String targetIp, int port) {
        this.targetIp = targetIp;
        this.port = port;
        init();
    }

    public ArthasTelnetConsole(String targetIp, int port, int timeout) {
        this.targetIp = targetIp;
        this.port = port;
        this.timeout = timeout;
        init();
    }


    protected void init() {

        telnet.setConnectTimeout(this.timeout);
        try {
            telnet.connect(this.getTargetIp(), this.getPort());

            // send init terminal size
            TelnetOptionHandler sizeOpt = new WindowSizeOptionHandler(120, 100, true, true, false, false);
            telnet.addOptionHandler(sizeOpt);

            final InputStream inputStream = telnet.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            header = readOutStr(in);
            // getPid(header);
            //System.out.print(header);
        } catch (IOException | InvalidTelnetOptionException e) {
            log.error("Connect to telnet server error: {} :{} ", this.getTargetIp()
                    , this.getPort());

        }


    }

    public void reconnect() {
        if (telnet == null || !telnet.isAvailable()) {
            return;
        }
        try {
            telnet.getOutputStream().write("version\n".getBytes());
            telnet.getOutputStream().flush();

            final InputStream inputStream = telnet.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            readOutStr(in);
        } catch (IOException e) {
            try {
                telnet.disconnect();
            } catch (IOException e1) {
                log.error(e1.getMessage(), e1);
            }
            init();
        }

    }

    public void disconnect() throws IOException {
        if (telnet == null || !telnet.isAvailable()) {
            return;
        }
        try {
            telnet.getOutputStream().write("quit\n".getBytes());
            telnet.getOutputStream().flush();
        } finally {
            telnet.disconnect();
        }
    }


    public synchronized <T> Flux<T> batchModeRun(Function<List<String>, T> transform, String command)
            throws IOException {

        final String cmd = command + " | plaintext\n";
        final InputStream inputStream = telnet.getInputStream();
        final OutputStream outputStream = telnet.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

//        Flux<T> flux = Flux.fromArray(commands).doOnNext(command -> {
//            try {
        outputStream.write(cmd.getBytes());
        outputStream.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).map(cmd -> readOutStr(in)).map(transform);

        Flux<T> flux = Flux.create((FluxSink<List<String>> emitter) -> readOutStr(emitter, in)).map(transform);

        //.doFinally((signalType)-> {
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });


        return flux;

    }

    private void getPid(String header) throws IOException {
        StringReader reader = new StringReader(header);
        List<String> list = IOUtils.readLines(reader);
        list.stream().filter(c -> c.startsWith("pid")).forEach(c -> pid = c.replace("pid", "").trim());
    }

    private synchronized String readOutStr(final BufferedReader in) {
        String lineStr = "";
        try {
            StringBuilder line = new StringBuilder();
            int b = -1;
            while (true) {
                b = in.read();
                //System.out.print(Character.toChars(b));


                if (b == -1 || line.indexOf(PROMPT) > -1) {
                    lineStr = line.toString();
                    line.setLength(0);
                    break;
                }
                line.appendCodePoint(b);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return lineStr;
    }


    private synchronized void readOutStr(FluxSink<List<String>> emitter, final BufferedReader in) {
        //String lineStr = "";
        try {
            StringBuilder line = new StringBuilder();
            int b = -1;

            BufferedReader bufferedReader = IOUtils.toBufferedReader(in);
            List<String> emitterList = null;
            List<String> nextList = new ArrayList<>();

            String command = bufferedReader.readLine();
            String first = bufferedReader.readLine();
            nextList.add(command);
            nextList.add(first);
            while (true) {
                b = bufferedReader.read();
                System.out.print(Character.toChars(b));

                if (line.indexOf(first) > 0) {
                    nextList.addAll(IOUtils.readLines(new StringReader(line.toString())));
                    nextList.remove(nextList.size() - 1);
                    emitterList = nextList;
                    emitter.next(emitterList);
                    nextList.clear();
                    nextList.add(command);
                    nextList.add(first);
                    line.setLength(0);
                    // nextList.add(first);
                } else if (b == -1 || line.indexOf(PROMPT) > -1) {
                    nextList.addAll(IOUtils.readLines(new StringReader(line.toString())));
                    emitterList = nextList;
                    emitter.next(emitterList);
                    line.setLength(0);
                    break;
                }
                line.appendCodePoint(b);
                //line.append(bufLine).append("\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

//    public List<String> batchModeRun(String... commands)
//            throws IOException, InterruptedException {
//        if (commands.length == 0) {
//            return new ArrayList<>();
//        }
//
//        final InputStream inputStream = telnet.getInputStream();
//        final OutputStream outputStream = telnet.getOutputStream();
//        final List<String> rs = new ArrayList<>();
//
//
//        final BlockingQueue<String> receviedPromptQueue = new LinkedBlockingQueue<String>(1);
//        Thread printResultThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//
//                    StringBuilder line = new StringBuilder();
//
//                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//                    int b = -1;
//                    while (true) {
//                        b = in.read();
//                        if (b == -1) {
//                            break;
//                        }
//                        line.appendCodePoint(b);
//
//                        // 检查到有 [arthas@ 时，意味着可以执行下一个命令了
//                        int index = line.indexOf(PROMPT);
//                        if (index > 0) {
//                            String[] rsline = line.toString().split("\n");
//                            rs.addAll(Arrays.asList(rsline));
//                            line.delete(0, index + PROMPT.length());
//                            receviedPromptQueue.put("");
//                        }
//                        System.out.print(Character.toChars(b));
//                    }
//                } catch (Exception e) {
//                    // ignore
//                }
//
//            }
//        });
//
//
//        printResultThread.start();
//
//        for (String command : commands) {
//            if (command.trim().isEmpty()) {
//                continue;
//            }
//            receviedPromptQueue.take();
//            // send command to server
//            outputStream.write((command + " | plaintext\n").getBytes());
//            outputStream.flush();
//        }
//
//        // 读到最后一个命令执行后的 prompt ，可以直接发 quit命令了。
//        receviedPromptQueue.take();
//        outputStream.write("quit\n".getBytes());
//        outputStream.flush();
//
//        return rs;
//
//    }


    public String getTargetIp() {
        return targetIp;
    }


    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public int getPort() {
        return port;
    }


    public void setPort(int port) {
        this.port = port;
    }


}
