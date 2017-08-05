import net.dongliu.requests.Requests;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class Master {
    public static boolean MASTERSTOPFLAG = false;
    public static void pushit(Set<String> methodlist, Document rawdoc, String ispub){
        if (false){
            for (String element:methodlist){
                if (element.equals("getstatus")){
                    Main.shouldsend ++;
                    Document document = rawdoc;
                    document.append("method", "getstatus");
                    ServantEventMain.ringBuffer.publishEvent(ServantEventMain::translate, document, ServantEventMain.collection, false);
                }
            }
        } else if (true){
            for (String element:methodlist){
                Main.shouldsend ++;
                Document document = rawdoc;
                document.append("method", element);
                ServantEventMain.ringBuffer.publishEvent(ServantEventMain::translate, document, ServantEventMain.collection, false);
            }
        } else {
            System.out.println("PARAM ERROR!!!");
        }
    }
    public static void getHotList(Set<String> methodlist, int politetime){
        JSONObject jsonObject;
        JSONParser jsonParser;
        String accresult;
        while (true) {
            try {
                String index_url = "http://120.55.238.158/api/live/simpleall";
                accresult = Requests.get(index_url).send().readToText();
                jsonParser = new JSONParser();
                jsonObject = (JSONObject) jsonParser.parse(accresult);
                break;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        JSONArray jsonArray = (JSONArray)jsonObject.get("lives");
        for (int i=0;i<jsonArray.size();i++)
        {
            JSONObject creator = (JSONObject)((JSONObject)jsonArray.get(i)).get("creator");
            long online_users = (Long) ((JSONObject)jsonArray.get(i)).get("online_users");
            String portrait = (String) creator.get("portrait");
            long gender = (Long) creator.get("gender");
            String nick = (String) creator.get("nick");
            long id = (Long) creator.get("id");
            long level = (Long) creator.get("level");
            System.out.println(i+" ^^^ "+online_users+" "+portrait+" "+gender+" "+nick+" "+id+" "+level);
            String ykid = String.valueOf(id);
            String[] param = Weapon.isLive(ykid);
            Document document = new Document("ykid",ykid).append("roomid",param[1]).append("portrait",portrait)
                    .append("gender",String.valueOf(gender)).append("nick",nick).append("online_users",String.valueOf(online_users))
                    .append("level",String.valueOf("level"));
            pushit(methodlist,document,param[0]);
            try {
                Thread.sleep(politetime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void call(Set<String> methodlist, int delaytime, int politetime, int maxtime)
    {
        TaskExecutor task = new TaskExecutor(methodlist, delaytime, politetime, maxtime);
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = delaytime;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }
}
