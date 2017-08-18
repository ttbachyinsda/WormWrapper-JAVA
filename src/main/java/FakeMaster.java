import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import net.dongliu.requests.Requests;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.Timer;


/**
 * Created by zjkgf on 2017/8/5.
 */
public class FakeMaster {
    public static boolean MASTERSTOPFLAG = false;
    public static void getHotList(){
        JSONObject jsonObject;
        JSONParser jsonParser;
        String accresult;
        while (true) {
            try {
                String index_url = "http://120.55.238.158/api/live/simpleall";
                accresult = Requests.get(index_url).send().readToText();
                jsonParser = new JSONParser();
                jsonObject = (JSONObject) jsonParser.parse(accresult);
                JSONArray jsonArray = (JSONArray)jsonObject.get("lives");
                String tsstr = String.valueOf(System.currentTimeMillis());
                for (int i=0;i<jsonArray.size();i++)
                {
                    //System.out.println(i);
                    JSONObject creator = (JSONObject)((JSONObject)jsonArray.get(i)).get("creator");
                    long online_users = (Long) ((JSONObject)jsonArray.get(i)).get("online_users");
                    String portrait = (String) creator.get("portrait");
                    long gender = (Long) creator.get("gender");
                    String nick = (String) creator.get("nick");
                    long id = (Long) creator.get("id");
                    long level = (Long) creator.get("level");
                    String ykid = String.valueOf(id);
                    String location = (String) creator.get("location");
                    OutPrinter.Print(tsstr+"|#|"+String.valueOf(i+1)+"|#|"
                    +ykid+"|#|"+location+"|#|"+String.valueOf(gender)+"|#|"+String.valueOf(level)+"|#|"+String.valueOf(online_users));
                }
                break;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    class HotlistThread extends Thread{
        @Override
        public void run() {
            getHotList();
        }
    }
    public void call()
    {
        while (true){
            System.out.println("a new thread start");
            new HotlistThread().start();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
