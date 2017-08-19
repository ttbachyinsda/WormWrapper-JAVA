import com.mongodb.async.SingleResultCallback;
import net.dongliu.requests.Requests;
import org.bson.Document;
import org.javatuples.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class Master {
    public static boolean MASTERSTOPFLAG = false;
    public static void pushit(Set<String> methodlist, Document rawdoc, int num){
        for (String element:methodlist){
            Main.shouldsend ++;
            Document document = new Document(rawdoc);
            document.append("method", element);
            //ServantEventMain.ringBuffer.publishEvent(ServantEventMain::translate, document, ServantEventMain.collectionlist);
            ThreadPool.pool.execute(new ServantFactory(document));
        }
    }
    public static void getList(Set<String> methodlist, int politetime, int methodnum){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("outputlist.txt")));
            String data = null;
            String tsstr = String.valueOf(System.currentTimeMillis());
            System.out.println("push "+ ThreadPool.pool.isTerminated()+" "+ThreadPool.pool.toString());
            System.out.println("push "+ ThreadPool.pool2.isTerminated()+" "+ThreadPool.pool2.toString());
            System.out.println("push "+ ThreadPool.pool3.isTerminated()+" "+ThreadPool.pool3.toString());
            while((data = br.readLine())!=null)
            {
                Document document = new Document("ykid",data).append("ts", tsstr);
                pushit(methodlist,document,0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    //修改gethotlist函数的相关方法，可以将热门列表修改为指定列表
    public static void getHotList(Set<String> methodlist, int politetime, int methodnum){
        Timestamp tss = new Timestamp(System.currentTimeMillis());
        System.out.println(tss.toString());
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
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            Document document = new Document("ykid",ykid).append("ts", tsstr);
            for (int j=0;j<2;j++)
                pushit(methodlist,document,i);
        }
    }
    public static void call(Set<String> methodlist, int delaytime, int politetime, int maxtime, int methodnum)
    {
        if (maxtime == -1) {
            int nowtime = 0;
            while (true) {
                System.out.println("now begin "+String.valueOf(nowtime)+","+ String.valueOf(ThreadPool.MaxTrynum) + " " + String.valueOf(ThreadPool.TotalTrynum));
                for (String element:ProxyChooser.proxymap.keySet()){
                    System.out.println(element+" "+String.valueOf(ProxyChooser.proxymap.get(element)));
                    //ProxyChooser.proxymap.replace(element, Pair.with(ProxyChooser.proxymap.get(element).getValue0(),0));
                }
                new TaskExecutor(methodlist, delaytime, politetime, maxtime, methodnum).start();
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
                System.out.println("now begin "+String.valueOf(i)+","+ String.valueOf(ThreadPool.MaxTrynum) + " " + String.valueOf(ThreadPool.TotalTrynum));
                new TaskExecutor(methodlist, delaytime, politetime, maxtime, methodnum).start();
                try {
                    Thread.sleep(delaytime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
