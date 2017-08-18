import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/8/9.
 */
public class ProxyThread extends TimerTask {

    @Override
    public void run() {
        if (ProxyChooser.proxymap.size() < ProxyChooser.threshold) {
            if (ProxyChooser.RUN == false) {
                System.out.println("updated " + String.valueOf(ProxyChooser.proxymap.size())+" "+String.valueOf(ProxyChooser.waitmap.size()));
                ProxyChooser.getnewproxy();
                ProxyChooser.removeredundantproxy();
            } else {
                System.out.println("waited "+ String.valueOf(ProxyChooser.proxymap.size())+" "+String.valueOf(ProxyChooser.waitmap.size()));
            }
        }
        else {
            ProxyChooser.removeredundantproxy();
            System.out.println("not updated "+String.valueOf(ProxyChooser.proxymap.size())+" "+String.valueOf(ProxyChooser.waitmap.size()));
        }
    }
}
