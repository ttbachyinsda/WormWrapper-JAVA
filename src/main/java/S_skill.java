/**
 * Created by zjkgf on 2017/7/31.
 */
import com.mongodb.async.SingleResultCallback;
import org.bson.Document;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class S_skill extends Servant{

    Document run() {
        String ykid = info.getString("ykid");
        String index_url = "http://service.inke.com/api/live/themesearch?uid=251464826&keyword=AFCC0BC263924F20";
        int try_num = 0;
        int max_num = 100;
        while (true){
            try{
                String[] random_proxy = ProxyChooser.chooseproxy();
                URL url = new URL(index_url);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(random_proxy[0], Integer.parseInt(random_proxy[1])));
                URLConnection urlConnection;
                if (with_proxy)
                    urlConnection = url.openConnection(proxy);
                else
                    urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(500);
                urlConnection.setRequestProperty("User-Agent", ProxyChooser.chooseagent());
                Scanner scan = new Scanner(urlConnection.getInputStream());
                StringBuffer accresult = new StringBuffer();
                while (scan.hasNextLine()){
                    accresult.append(scan.nextLine());
                    accresult.append("\n");
                }
                String result;
                if (accresult.indexOf("\"id\": "+ykid) != -1)
                    result = "True";
                else
                    result = "False";
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                Document doc = new Document("timestamp", ts.toString()).append("ykid", ykid)
                        .append("method", "skill").append("result", result.trim());
                collection.insertOne(doc, new SingleResultCallback<Void>() {
                    @Override
                    public void onResult(Void aVoid, Throwable throwable) {
                        Main.havesent ++;
                    }
                });
                return doc;
            }catch (Exception e) {
                e.printStackTrace();
                try_num += 1;
                if (try_num >= max_num+1) {
                    String result = "False";
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc = new Document("timestamp", ts.toString()).append("ykid", ykid)
                            .append("method", "skill").append("result", result.trim());
                    collection.insertOne(doc, new SingleResultCallback<Void>() {
                        @Override
                        public void onResult(Void aVoid, Throwable throwable) {
                            Main.havesent ++;
                        }
                    });
                    return doc;
                }
                if (try_num == 1 || try_num >= max_num) {
                    ProxyChooser.getnewproxy();
                }
            }
        }
    }
    public static void main(String[] args){
    }
}