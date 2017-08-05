import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.Block;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
/**
 * Created by zjkgf on 2017/7/29.
 */
public class Main {
    public static int shouldsend;
    public static int havesent;
    public static void main(String[] args){
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);
        MongoClient mongoClient = MongoClients.create("mongodb://128.199.182.66");
        MongoDatabase database = mongoClient.getDatabase("user");
        MongoCollection<Document> collection = database.getCollection("userlist");
//        collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
//            @Override
//            public void onResult(DeleteResult deleteResult, Throwable throwable) {
//
//            }
//        });
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ServantEventMain.collection = collection;
//        try {
//            ServantEventMain.readyrun();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Set<String> methodlist = new HashSet<>();
        //methodlist.add("getboard");
//        methodlist.add("getfans");
        //methodlist.add("getinfo");
//        methodlist.add("getpoint");
//        methodlist.add("getstatus");
        //methodlist.add("goodvoice");
        //methodlist.add("nowpublish");
        //methodlist.add("onlineuser");
        //methodlist.add("roomuser");
        //methodlist.add("simpleall");
        //methodlist.add("skill");
        shouldsend = 0;
        havesent = 0;
//        Master.call(methodlist,20000,60,2);
        new FakeMaster().call(collection);
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
        for(;;)
        {
            try {
                Thread.sleep(2000);
//                System.out.println(shouldsend);
//                System.out.println("aaa");
//                System.out.println(havesent);
//                System.out.println("bbb");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
