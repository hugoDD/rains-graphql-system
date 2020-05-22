import com.taobao.arthas.client.TelnetConsole;
import org.junit.Test;

public class TelnetConsoleTest {
    //"EXAMPLES:\n
    // java -jar arthas-client.jar 127.0.0.1 3658\n
    // java -jar arthas-client.jar -c 'dashboard -n 1' \n
    // java -jar arthas-client.jar -f batch.as 127.0.0.1\n"
    String ip = "172.25.191.228";
    private int port = 3658;
    @Test
    public void connect(){
        TelnetConsole telnetConsole = new TelnetConsole();
        telnetConsole.setTargetIp(ip);
        telnetConsole.setCommand("dashboard -n 1");

    }
}
