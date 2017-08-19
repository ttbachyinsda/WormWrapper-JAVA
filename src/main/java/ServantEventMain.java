import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class ServantEventMain {
    public static MongoDatabase database;
    public static void initialcollection(boolean cleanflag)
    {
        S_getBoard.collection = database.getCollection("getboard");
        S_getFans.collection = database.getCollection("getfans");
        S_getInfo.collection = database.getCollection("getinfo");
        S_getPoint.collection = database.getCollection("getpoint");
        S_getStatus.collection = database.getCollection("getstatus");
        S_goodVoice.collection = database.getCollection("goodvoice");
        S_nowPublish.collection = database.getCollection("nowpublish");
        S_onlineUser.collection = database.getCollection("onlineuser");
        S_roomUser.collection = database.getCollection("roomuser");
        S_simpleAll.collection = database.getCollection("simpleall");
        S_skill.collection = database.getCollection("skill");
        S_temp.collection = database.getCollection("temp");
        if (cleanflag) {
            S_getBoard.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_getFans.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_getInfo.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_getPoint.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_getStatus.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_goodVoice.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_nowPublish.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_onlineUser.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_roomUser.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_simpleAll.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_skill.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
            S_temp.collection.deleteMany(new Document(), new SingleResultCallback<DeleteResult>() {
                @Override
                public void onResult(DeleteResult deleteResult, Throwable throwable) {

                }
            });
        }
    }
//    public static void handleEvent(ServantFactory servantFactory, long sequence, boolean endOfBatch)
//    {
//        servantFactory.run();
//    }
//    public static void translate(ServantFactory servantFactory, long sequence, Document info, Map<String, MongoCollection<Document>> collectionlist)
//    {
//        try {
//            servantFactory.set(info, collectionlist);
//        } catch (ResultErrorException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void readyrun() throws Exception
//    {
//        int bufferSize = 1024;
//        //ExecutorService executor = Executors.newFixedThreadPool(100);
//        //disruptor = new Disruptor<>(ServantFactory::new, bufferSize, executor);
//        disruptor = new Disruptor<>(ServantFactory::new, bufferSize, DaemonThreadFactory.INSTANCE);
//        disruptor.handleEventsWith(ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent,ServantEventMain::handleEvent);
//        //disruptor.handleEventsWith(ServantEventMain::handleEvent);
//        disruptor.start();
//        ringBuffer = disruptor.getRingBuffer();
//    }
    public static void forcestop()
    {

    }
}
