import javax.print.Doc;
import java.util.*;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class Main {
    public static int shouldsend;
    public static int havesent;
    public static void main(String[] args){
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
//        rootLogger.setLevel(Level.OFF);
//        MongoClient mongoClient = MongoClients.create("mongodb://127.0.0.1");
//        MongoDatabase database = mongoClient.getDatabase("user");
        TimerTask pt = new ProxyThread();
        Timer c = new Timer();
        c.schedule(pt,0,10000);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (ProxyChooser.proxymap.size()<350){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        TimerTask dt = new PrinterThread();
        Timer d = new Timer();
        d.schedule(dt,1800000,1800000);
//        ServantEventMain.database = database;
//        ServantEventMain.initialcollection(false);
//        try {
//            ServantEventMain.readyrun();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Set<String> methodlist = new HashSet<>();
        //methodlist.add("getboard");
        //methodlist.add("nowpublish onlineuser getfans getpoint");
        //methodlist.add("nowpublish");
        //methodlist.add("getinfo");
        //methodlist.add("getpoint");
        //methodlist.add("getstatus");
        //methodlist.add("goodvoice");
        methodlist.add("onlineuser getfans getpoint");
        //methodlist.add("roomuser");
        //methodlist.add("simpleall");
        //methodlist.add("skill");
        //methodlist.add("temp");
        shouldsend = 0;
        havesent = 0;
        int methodnum = 4;
        FakeMaster.call(methodlist,60000,60,-1, methodnum);
        //Master.getList(methodlist,60,methodnum);
//        new FakeMaster().call(collection);
//        SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
//            @Override
//            public void onResult(final Void result, final Throwable t) {
//                System.out.println("Operation Finished!");
//            }
//        };
//        Block<Document> printBlock = new Block<Document>() {
//            @Override
//            public void apply(final Document document) {
//                System.out.println(document.toJson());
//            }
//        };
//        collection.find().sort(Sorts.ascending("ykid")).forEach(printBlock, callbackWhenFinished);
//        collection.count(new SingleResultCallback<Long>() {
//            @Override
//            public void onResult(Long aLong, Throwable throwable) {
//                System.out.println(aLong);
//            }
//        });
        while (true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        ThreadPool.pool.shutdown();
//        while (!ThreadPool.pool.isTerminated())
//        {
//            try {
//                Thread.sleep(2000);
//                System.out.println(ThreadPool.MaxTrynum);
//                System.out.println(ThreadPool.TotalTrynum);
////                System.out.println(shouldsend);
////                System.out.println("aaa");
////                System.out.println(havesent);
////                System.out.println("bbb");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("Finished");
//        c.cancel();
    }
}
