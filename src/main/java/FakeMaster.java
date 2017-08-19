import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import net.dongliu.requests.Requests;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.print.Doc;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by zjkgf on 2017/8/5.
 */
public class FakeMaster {
    public static boolean MASTERSTOPFLAG = false;
    public static Map<String, Document> nowset = new HashMap<>();
    public static void pushit(Set<String> methodlist, Document rawdoc, int num){
        for (String element:methodlist){
            Main.shouldsend ++;
            Document document = new Document(rawdoc);
            document.append("method", element);
            //ServantEventMain.ringBuffer.publishEvent(ServantEventMain::translate, document, ServantEventMain.collectionlist);
            ThreadPool.pool.execute(new ServantFactory(document));
        }
    }
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
    public static void getHotList(Set<String> methodlist, int politetime, int methodnum){
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
                    String roomid = (String) ((JSONObject)jsonArray.get(i)).get("id");
                    long level = (Long) creator.get("level");
                    String ykid = String.valueOf(id);
                    String location = (String) creator.get("location");
//                    System.out.println(tsstr+"|#|"+String.valueOf(i+1)+"|#|"
//                           +ykid+"|#|"+location+"|#|"+String.valueOf(gender)+"|#|"+String.valueOf(level)+"|#|"+String.valueOf(online_users)+"|#|"+roomid);
                    OutPrinter.Print(tsstr+"|#|"+String.valueOf(i+1)+"|#|"
                            +ykid+"|#|"+location+"|#|"+String.valueOf(gender)+"|#|"+String.valueOf(level)+"|#|"+String.valueOf(online_users)+"|#|"+roomid);
                    if (!nowset.keySet().contains(ykid) && nowset.keySet().size() < 1500) {
                        Document document = new Document("ykid", ykid).append("ts", tsstr).append("roomid", roomid);
                        nowset.put(ykid, document);
                    }
                }
                for (String element: nowset.keySet()){
                    pushit(methodlist, nowset.get(element), 0);
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
    public static void call(Set<String> methodlist, int delaytime, int politetime, int maxtime, int methodnum)
    {
        if (maxtime == -1) {
            int nowtime = 0;
            while (true) {
                System.out.println("now begin "+String.valueOf(nowtime)+", "+ String.valueOf(ThreadPool.MaxTrynum) + " " + String.valueOf(ThreadPool.TotalTrynum));
                    //ProxyChooser.proxymap.replace(element, Pair.with(ProxyChooser.proxymap.get(element).getValue0(),0));
                System.out.println("push "+ ThreadPool.pool.isTerminated()+" "+ThreadPool.pool.toString()+" "+nowset.size());
                new FakeTaskExecutor(methodlist, delaytime, politetime, maxtime, methodnum).start();
                try {
                    Thread.sleep(delaytime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nowtime += 1;
            }
        }
        else {
            for (int i=0;i<maxtime;i++){
                System.out.println("now begin "+String.valueOf(i)+", "+ String.valueOf(ThreadPool.MaxTrynum) + " " + String.valueOf(ThreadPool.TotalTrynum));
                new FakeTaskExecutor(methodlist, delaytime, politetime, maxtime, methodnum).start();
                try {
                    Thread.sleep(delaytime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
