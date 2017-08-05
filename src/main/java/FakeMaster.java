import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import net.dongliu.requests.Requests;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Set;
import java.util.Timer;

/**
 * Created by zjkgf on 2017/8/5.
 */
public class FakeMaster {
    public static boolean MASTERSTOPFLAG = false;
    public static void getHotList(MongoCollection<Document> collection){
        JSONObject jsonObject;
        JSONParser jsonParser;
        String accresult;
        while (true) {
            try {
                String index_url = "http://120.55.238.158/api/live/simpleall";
                accresult = Requests.get(index_url).send().readToText();
                jsonParser = new JSONParser();
                jsonObject = (JSONObject) jsonParser.parse(accresult);
                //System.out.println(accresult);
                collection.insertOne(new Document("timestamp", System.currentTimeMillis())
                        .append("data", accresult), new SingleResultCallback<Void>() {
                    @Override
                    public void onResult(Void aVoid, Throwable throwable) {

                    }
                });
                break;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    class HotlistThread extends Thread{
        public MongoCollection<Document> collection;
        public HotlistThread(MongoCollection<Document> collection){
            this.collection = collection;
        }
        @Override
        public void run() {
            getHotList(collection);
        }
    }
    public void call(MongoCollection<Document> collection)
    {
        while (true){
            System.out.println("a new thread start");
            new HotlistThread(collection).start();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
