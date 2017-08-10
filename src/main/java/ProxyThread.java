import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/8/9.
 */
public class ProxyThread extends TimerTask {

    @Override
    public void run() {
        if (ProxyChooser.proxymap.size() < 500)
            ProxyChooser.getnewproxy();
        else {
            ProxyChooser.removeredundantproxy();
            System.out.println("not updated "+ProxyChooser.proxymap.size());
        }
    }
}
