import com.mongodb.async.SingleResultCallback;
import org.bson.Document;

import javax.print.Doc;
import java.io.IOException;
import java.net.*;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class S_getPoint extends Servant{

    Document run() {
        String ykid = info.getString("ykid");
        String index_url = "http://120.55.238.158/api/statistic/inout?uid=251464826&id=" + ykid;
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
                String inx = "\"gold\": \\d+";
                String outx = "\"point\": \\d+";
                Pattern pin = Pattern.compile(inx);
                Pattern pout = Pattern.compile(outx);
                Matcher min = pin.matcher(accresult);
                Matcher mout = pout.matcher(accresult);
                if (!min.find()) throw new ResultErrorException("Result of getpoint inx is Error. "+accresult);
                if (!mout.find()) throw new ResultErrorException("Result of getpoint outx is Error. "+accresult);
                String[] sin = min.group().split(":");
                String[] sout = mout.group().split(":");
                String result = sin[sin.length-1]+" "+sout[sout.length-1];
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                Document doc = new Document("timestamp", ts.toString()).append("ykid", ykid)
                        .append("method", "getpoint").append("result", result.trim());
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
                    String result = "";
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc = new Document("timestamp", ts.toString()).append("ykid", ykid)
                            .append("method", "getpoint").append("result", result.trim());
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
