import java.util.Set;
import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/8/1.
 */
public class TaskExecutor extends Thread {
    private Set<String> methodlist;
    private int delaytime;
    private int politetime;
    private int maxtime;
    private int nowtime;
    private int methodnum;
    public TaskExecutor(Set<String> methodlist, int delaytime, int politetime, int maxtime, int methodnum)
    {
        this.methodlist = methodlist;
        this.delaytime = delaytime;
        this.politetime = politetime;
        this.maxtime = maxtime;
        this.nowtime = 0;
        this.methodnum = methodnum;
    }
    @Override
    public void run() {
        Master.getHotList(methodlist, politetime, methodnum);
    }
}
