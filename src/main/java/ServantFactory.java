import com.lmax.disruptor.EventFactory;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class ServantFactory implements Runnable {

    private Document info;
    public ServantFactory(Document _info)
    {
        set(_info);
    }
    public void set(Document _info)
    {
        info = _info;
    }


    @Override
    public void run() {
        String[] methodlist = info.getString("method").split(" ");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Document tempinfo = new Document(info);
        for (String method:methodlist){
            //System.out.println(method);
            switch (method.trim()){
                case "getboard":
                    tempinfo = S_getBoard.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "getfans":
                    tempinfo = S_getFans.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "getinfo":
                    tempinfo = S_getInfo.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "getpoint":
                    tempinfo = S_getPoint.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "goodvoice":
                    tempinfo = S_goodVoice.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "nowpublish":
                    tempinfo = S_nowPublish.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "onlineuser":
                    tempinfo = S_onlineUser.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "roomuser":
                    tempinfo = S_roomUser.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "simpleall":
                    tempinfo = S_simpleAll.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "skill":
                    tempinfo = S_skill.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "getstatus":
                    tempinfo = S_getStatus.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                case "temp":
                    tempinfo = S_temp.run(tempinfo, ProxyChooser.WITH_PROXY);
                    break;
                default:
                    System.out.println("Unknown method " + method);
            }
        }
    }
}
