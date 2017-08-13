import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/8/13.
 */
public class PrinterThread extends TimerTask {

    @Override
    public void run() {
        OutPrinter.Clear();
    }
}
