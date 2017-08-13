import net.dongliu.requests.Requests;
import org.bson.Document;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.font.TrueTypeFont;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class Weapon {
    public static boolean skill(String ykid){
        String index_url = "http://service.inke.com/api/live/themesearch?uid=251464826&keyword=AFCC0BC263924F20";
        String accresult = Requests.get(index_url).send().readToText();
        if (accresult.indexOf("\"id\": "+ykid) != -1)
            return true;
        else
            return false;
    }
    public static boolean goodVoice(String ykid){
        String index_url = "http://service.inke.com/api/live/themesearch?uid=251464826&keyword=666ABA8214206E5B";
        String accresult = Requests.get(index_url).send().readToText();
        if (accresult.indexOf("\"id\": "+ykid) != -1)
            return true;
        else
            return false;
    }
    public static boolean simpleAll(String ykid){
        String index_url = "http://120.55.238.158/api/live/simpleall";
        String accresult = Requests.get(index_url).send().readToText();
        if (accresult.indexOf("\"id\": "+ykid) != -1)
            return true;
        else
            return false;
    }
    @NotNull
    public static String nowPublish(String ykid){
        String index_url = "http://service.inke.com/api/live/now_publish?cv=IK3.7.20_Android&uid=251464826&id=" + ykid;
        String accresult = Requests.get(index_url).send().readToText();
        System.out.println(accresult);
        if (accresult.indexOf("live") != -1)
            return accresult.trim();
        else
            return "";
    }
    @NotNull
    public static String onlineUser(String roomid){
        String index_url = "http://120.55.238.158/api/live/info?uid=251464826&id=" + roomid;
        String accresult = Requests.get(index_url).send().readToText();
        if (accresult == null) accresult = "";
        StringBuffer result = new StringBuffer();
        List<String> lin = new ArrayList<String>();
        String inx = "\"online_users\": \\d+";
        Pattern pin = Pattern.compile(inx);
        Matcher min = pin.matcher(accresult);

        while (min.find())
        {
            String[] sin = min.group().split(" ");
            lin.add(sin[sin.length-1]);
        }
        for (String element:lin){
            result.append(element);
            result.append(' ');
        }
        return result.toString().trim();
    }
    public static String[] isLive(String ykid){
        String[] params = new String[2];
        String result = nowPublish(ykid);
        if (result.equals("")){
            params[0] = "False";
        } else {
            String inx = ",\"creator\":(.*?),\"id\":\"(.*?)\",";
            Pattern pin = Pattern.compile(inx);
            Matcher min = pin.matcher(result);
            params[0] = "False";
            while (min.find()){
                params[0] = "True";
                params[1] = min.group(2);
                break;
            }
        }
        return params;
    }
    public static String[] isLive_Str(String result){
        String[] params = new String[2];
        if (result.equals("")){
            params[0] = "False";
        } else {
            String inx = ",\"creator\":(.*?),\"id\":\"(.*?)\",";
            Pattern pin = Pattern.compile(inx);
            Matcher min = pin.matcher(result);
            params[0] = "False";
            while (min.find()){
                params[0] = "True";
                params[1] = min.group(2);
                break;
            }
        }
        return params;
    }
    public static String getroomid(Document document){
        if (document.containsKey("roomid"))
        {
            return document.getString("roomid");
        } else {
            String result = document.getString("nowpublish");
            String[] params = new String[2];
            if (result.equals("")){
                params[0] = "False";
                return "";
            } else {
                String inx = ",\"creator\":(.*?),\"id\":\"(.*?)\",";
                Pattern pin = Pattern.compile(inx);
                Matcher min = pin.matcher(result);
                params[0] = "False";
                while (min.find()){
                    params[0] = "True";
                    params[1] = min.group(2);
                    break;
                }
                return params[1];
            }
        }
    }
    public static String getOnlineUser(int delay, String roomid, boolean ispub, String name, String s){
        if (ispub){
            String online = onlineUser(roomid);
            return online;
        }
        return "0";
    }
    public static void main(String[] args){
//        Servant a = new S_nowPublish();
//        a.info = new Document("ykid", "12345678");
//        a.with_proxy = true;
//        a.run();
    }
}
