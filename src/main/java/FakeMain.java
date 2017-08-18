import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/8/16.
 */
public class FakeMain {
    public static void main(String args[]){
        OutPrinter.prefix = "Fake";
        TimerTask dt = new PrinterThread();
        Timer d = new Timer();
        d.schedule(dt,120000,120000);
        new FakeMaster().call();
        for (;;){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
