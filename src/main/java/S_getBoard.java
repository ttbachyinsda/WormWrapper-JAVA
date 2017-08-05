import com.mongodb.async.SingleResultCallback;
import net.dongliu.requests.Requests;
import org.bson.Document;
import org.json.JSONObject;

import java.net.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class S_getBoard extends Servant{

    Document run() {
        String ykid = info.getString("ykid");
        StringBuffer result = new StringBuffer();
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        List<String> lin = new ArrayList<String>();
        List<String> lout = new ArrayList<String>();
        int start = 0;
        while (true) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("count", "20");
            jsonObject.put("id", ykid);
            jsonObject.put("request_id", "251464826");
            jsonObject.put("start", start);
            String index_url = "http://service.inke.com/api/day_bill_board/board";
            String accresult = Requests.post(index_url).body(jsonObject.toString()).send().readToText();
            if (accresult.indexOf("\"count\":0") != -1)
                break;
            String inx = "\"contribution\":\\d+";
            String outx = "\"id\":\\d+";
            Pattern pin = Pattern.compile(inx);
            Pattern pout = Pattern.compile(outx);
            Matcher min = pin.matcher(accresult);
            Matcher mout = pout.matcher(accresult);
            while (min.find())
            {
                String[] sin = min.group().split(":");
                lin.add(sin[sin.length-1]);
            }
            while (mout.find())
            {
                String[] sout = mout.group().split(":");
                lout.add(sout[sout.length-1]);
            }
            start += 20;
        }
        for (String element: lin){
            result.append(element);
            result.append(' ');
        }
        result.append(':');
        for (String element: lout){
            result.append(element);
            result.append(' ');
        }
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Document doc = new Document("timestamp", ts.toString()).append("ykid", ykid)
                .append("method", "getboard").append("result", result.toString().trim());
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
