import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/8/9.
 */
public class ProxyThread extends TimerTask {

    @Override
    public void run() {
        if (ProxyChooser.proxymap.size() < 350) {
                System.out.println("updated " + String.valueOf(ProxyChooser.proxymap.size()));
                ProxyChooser.getnewproxy();
        }
        else {
            ProxyChooser.removeredundantproxy();
            System.out.println("not updated "+String.valueOf(ProxyChooser.proxymap.size()));
        }
    }
}
