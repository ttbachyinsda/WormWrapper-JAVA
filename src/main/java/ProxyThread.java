import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/8/9.
 */
public class ProxyThread extends TimerTask {

    @Override
    public void run() {
        ProxyChooser.getnewproxy();
    }
}
