import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import net.dongliu.requests.Parameter;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.bson.Document;

import javax.print.Doc;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class S_roomUser{
    public static MongoCollection<Document> collection;
    public static Document run(Document info, boolean with_proxy) {
        Set<String> havechoice = new HashSet<>();
        String ykid = info.getString("ykid");
        String roomid = Weapon.getroomid(info);
        String index_url = "http://120.55.238.158/api/live/users?uid=251464826&count=20&id=" + roomid;
        int start = 0;
        StringBuffer result = new StringBuffer();
        while (true) {
            int try_num = 0;
            int max_num = 100;
            String realurl = index_url + "&start="+start;
            String accresult = "";
            String[] random_proxy = ProxyChooser.chooseproxy(havechoice);
            havechoice.add(random_proxy[2]);
            while (true) {
                try {

                    RawResponse tap = Requests.get(index_url).headers(Parameter.of("User-Agent", ProxyChooser.chooseagent())).timeout(500).proxy(Proxies.httpProxy(random_proxy[0], Integer.parseInt(random_proxy[1]))).send();
                    if (tap.getStatusCode() != 200)
                    {
                        throw new ResultErrorException("code error");
                    }
                    accresult = tap.readToText();
//                    if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
//                        ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])-1);
                    break;
                } catch (Exception e) {
                    //e.printStackTrace();
//                    if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
//                        ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])+1);
                    try_num += 1;
                    if (try_num >= max_num + 1) {
                        break;
                    }
                }
            }
            if (accresult.indexOf("emotion") == -1)
                break;
            result.append(accresult);
            start += 20;
        }
        //genmap(result.toString());
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Document doc = new Document("timestamp", info.getString("ts")).append("ykid", ykid).append("roomid", roomid)
                .append("result", result.toString().trim());
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