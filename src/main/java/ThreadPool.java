import net.sf.ehcache.search.aggregator.Max;
import org.bson.Document;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zjkgf on 2017/8/9.
 */
public class ThreadPool {
    public static final int MaxThreadNum = 200;
    public static ExecutorService pool = Executors.newFixedThreadPool(MaxThreadNum);
    public static ExecutorService pool2 = Executors.newFixedThreadPool(MaxThreadNum);
    public static ExecutorService pool3 = Executors.newFixedThreadPool(MaxThreadNum);
    public static int TotalTrynum = 0;
    public static int MaxTrynum = 0;
}
