import com.mongodb.async.SingleResultCallback;
import org.bson.Document;

import javax.print.Doc;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class S_onlineUser extends Servant{

    Document run() {
        String ykid = info.getString("ykid");
        String roomid = Weapon.getroomid(info);
        String index_url = "http://120.55.238.158/api/live/info?uid=251464826&id=" + roomid;
        int start = 0;
        StringBuffer result = new StringBuffer();
        int try_num = 0;
        int max_num = 100;
        String realurl = index_url + "&start="+start;
        StringBuffer accresult = new StringBuffer();
        while (true) {
            try {
                String[] random_proxy = ProxyChooser.chooseproxy();
                URL url = new URL(realurl);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(random_proxy[0], Integer.parseInt(random_proxy[1])));
                URLConnection urlConnection;
                if (with_proxy)
                    urlConnection = url.openConnection(proxy);
                else
                    urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(500);
                urlConnection.setRequestProperty("User-Agent", ProxyChooser.chooseagent());
                Scanner scan = new Scanner(urlConnection.getInputStream());
                while (scan.hasNextLine()) {
                    accresult.append(scan.nextLine());
                    accresult.append("\n");
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
                try_num += 1;
                if (try_num >= max_num + 1) {
                    break;
                }
                if (try_num == 1 || try_num >= max_num) {
                    ProxyChooser.getnewproxy();
                }
            }
        }
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
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Document doc = new Document("timestamp", ts.toString()).append("ykid", ykid).append("roomid", roomid)
                .append("method", "onlineuser").append("result", result.toString().trim());
        collection.insertOne(doc, new SingleResultCallback<Void>() {
            @Override
            public void onResult(Void aVoid, Throwable throwable) {
                Main.havesent ++;
            }
        });
        return doc;
    }
    public static void main(String[] args){
    }
}