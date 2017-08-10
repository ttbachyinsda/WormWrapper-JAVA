import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class S_getStatus{
    public static MongoCollection<Document> collection;
    public static Document run(Document info, boolean with_proxy) {
        String ykid = info.getString("ykid");
        String roomid = info.getString("roomid");
        String portrait = info.getString("portrait");
        String gender = info.getString("gender");
        String nick = info.getString("nick");
        String id = info.getString("id");
        String level = info.getString("level");
        String online_users = info.getString("online_users");
        String rank = info.getString("rank");
        String timestamp = info.getString("timestamp");
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("ykid", ykid);
        jsonObject.append("roomid", roomid);
        jsonObject.append("portrait", portrait);
        jsonObject.append("gender", gender);
        jsonObject.append("nick", nick);
        jsonObject.append("id", id);
        jsonObject.append("level", level);
        jsonObject.append("online_users", online_users);
        jsonObject.append("rank", rank);
        jsonObject.append("timestamp", timestamp);
        //genmap(jsonObject.toString());
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Document doc = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
                .append("result", jsonObject.toString());
        collection.insertOne(doc, new SingleResultCallback<Void>() {
            @Override
            public void onResult(Void aVoid, Throwable throwable) {
                Main.havesent ++;
            }
        });
        return doc;
    }
}
