import com.baomidou.mybatisplus.extension.api.R;
import com.rains.graphql.common.utils.StringUtils;
import com.taobao.arthas.common.UsageRender;
import com.taobao.middleware.cli.CLI;
import com.taobao.middleware.cli.UsageMessageFormatter;
import com.taobao.middleware.cli.annotations.*;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**
 * @author ralf0131 2016-12-29 11:55.
 * @author hengyunabc 2018-11-01
 */
@Name("arthas-client")
@Summary("Arthas Telnet Client")
@Description("EXAMPLES:\n" + "  java -jar arthas-client.jar 127.0.0.1 3658\n"
        + "  java -jar arthas-client.jar -c 'dashboard -n 1' \n"
        + "  java -jar arthas-client.jar -f batch.as 127.0.0.1\n")
public class TelnetConsole {
    private static final String PROMPT = "[arthas@"; // [arthas@49603]$
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000; // 5000 ms

    private static final byte CTRL_C = 0x03;

    private boolean help = false;

    private String targetIp = "127.0.0.1";
    private int port = 3658;

    private String command;
    private String batchFile;

    private Integer width = null;
    private Integer height = null;

    public TelnetConsole() {
    }

    private static List<String> readLines(File batchFile) {
        List<String> list = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(batchFile));
            String line = br.readLine();
            while (line != null) {
                list.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return list;
    }

    public static void main(String[] args) throws IOException {


        com.taobao.arthas.client.TelnetConsole telnetConsole = new com.taobao.arthas.client.TelnetConsole();
        telnetConsole.setTargetIp("172.25.191.228");

        try {


            final TelnetClient telnet = new TelnetClient();
            telnet.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);


            try {
                telnet.connect(telnetConsole.getTargetIp(), telnetConsole.getPort());
            } catch (IOException e) {
                System.out.println("Connect to telnet server error: " + telnetConsole.getTargetIp() + " "
                        + telnetConsole.getPort());
                throw e;
            }

            Function<List<String>, String> handler =  (rs)->{
                System.out.println(rs);
                return rs.get(rs.size()-1);
            };

            List<String> list = batchModeRun(telnet,"help");
            System.out.println(list);
            telnet.disconnect();
        } catch (Throwable e) {

            System.exit(1);
        }

    }

    private static List<String> batchModeRun(TelnetClient telnet, String... commands)
            throws IOException, InterruptedException {
        if (commands.length == 0) {
            return new ArrayList<>();
        }

        final InputStream inputStream = telnet.getInputStream();
        final OutputStream outputStream = telnet.getOutputStream();
        final List<String> rs = new ArrayList<>();


        final BlockingQueue<String> receviedPromptQueue = new LinkedBlockingQueue<String>(1);
        Thread printResultThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    StringBuilder line = new StringBuilder();

                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    int b = -1;
                    while (true) {
                        b = in.read();
                        if (b == -1) {
                            break;
                        }
                        line.appendCodePoint(b);

                        // 检查到有 [arthas@ 时，意味着可以执行下一个命令了
                        int index = line.indexOf(PROMPT);
                        if (index > 0) {
                           String[] rsline =  line.toString().split("\n");
                            rs.addAll(Arrays.asList(rsline));
                            line.delete(0, index + PROMPT.length());
                            receviedPromptQueue.put("");
                        }
                        System.out.print(Character.toChars(b));
                    }
                } catch (Exception e) {
                    // ignore
                }

            }
        });

        printResultThread.start();

        for (String command : commands) {
            if (command.trim().isEmpty()) {
                continue;
            }
            receviedPromptQueue.take();
            // send command to server
            outputStream.write((command + " | plaintext\n").getBytes());
            outputStream.flush();
        }

        // 读到最后一个命令执行后的 prompt ，可以直接发 quit命令了。
        receviedPromptQueue.take();
        outputStream.write("quit\n".getBytes());
        outputStream.flush();

        return rs;

    }



    @Option(shortName = "h", longName = "height")
    @Description("The terminal height")
    public void setheight(int height) {
        this.height = height;
    }

    public String getTargetIp() {
        return targetIp;
    }

    @Argument(argName = "target-ip", index = 0, required = false)
    @Description("Target ip")
    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public int getPort() {
        return port;
    }

    @Argument(argName = "port", index = 1, required = false)
    @Description("The remote server port")
    public void setPort(int port) {
        this.port = port;
    }

    public String getCommand() {
        return command;
    }

    @Option(shortName = "c", longName = "command")
    @Description("Command to execute, multiple commands separated by ;")
    public void setCommand(String command) {
        this.command = command;
    }

    public String getBatchFile() {
        return batchFile;
    }

    @Option(shortName = "f", longName = "batch-file")
    @Description("The batch file to execute")
    public void setBatchFile(String batchFile) {
        this.batchFile = batchFile;
    }

    public Integer getWidth() {
        return width;
    }

    @Option(shortName = "w", longName = "width")
    @Description("The terminal width")
    public void setWidth(int width) {
        this.width = width;
    }

    public Integer getheight() {
        return height;
    }

    public boolean isHelp() {
        return help;
    }

    @Option(longName = "help", flag = true)
    @Description("Print usage")
    public void setHelp(boolean help) {
        this.help = help;
    }

}
