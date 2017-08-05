import java.util.Set;
import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/8/1.
 */
public class TaskExecutor extends TimerTask {
    private Set<String> methodlist;
    private int delaytime;
    private int politetime;
    private int maxtime;
    private int nowtime;
    public TaskExecutor(Set<String> methodlist, int delaytime, int politetime, int maxtime)
    {
        this.methodlist = methodlist;
        this.delaytime = delaytime;
        this.politetime = politetime;
        this.maxtime = maxtime;
        this.nowtime = 0;
    }
    @Override
    public void run() {
        this.nowtime += 1;
        Master.getHotList(methodlist, politetime);
        if (this.nowtime >= this.maxtime)
        {
            this.cancel();
        }
    }
}
