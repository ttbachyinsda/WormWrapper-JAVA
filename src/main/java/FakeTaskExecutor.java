import java.util.Set;

/**
 * Created by zjkgf on 2017/8/18.
 */
public class FakeTaskExecutor extends Thread {
    private Set<String> methodlist;
    private int delaytime;
    private int politetime;
    private int maxtime;
    private int nowtime;
    private int methodnum;
    public FakeTaskExecutor(Set<String> methodlist, int delaytime, int politetime, int maxtime, int methodnum)
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
        FakeMaster.getHotList(methodlist, politetime, methodnum);
    }
}
