import com.lmax.disruptor.EventFactory;
import com.mongodb.async.client.MongoCollection;
import org.bson.Document;

import javax.print.Doc;
import java.util.List;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class ServantFactory {

    private List<Servant> servantlist;
    private Document info;
    public void set(Document _info, MongoCollection<Document> _collection, boolean _with_proxy) throws ResultErrorException
    {
        info = _info;
        String[] methodlist = _info.getString("method").split(" ");
        for (String method:methodlist){
            Servant servant;
            switch (method.trim()){
                case "getboard":
                    servant = new S_getBoard();
                    break;
                case "getfans":
                    servant = new S_getFans();
                    break;
                case "getinfo":
                    servant = new S_getInfo();
                    break;
                case "getpoint":
                    servant = new S_getPoint();
                    break;
                case "goodvoice":
                    servant = new S_goodVoice();
                    break;
                case "nowpublish":
                    servant = new S_nowPublish();
                    break;
                case "onlineuser":
                    servant = new S_onlineUser();
                    break;
                case "roomuser":
                    servant = new S_roomUser();
                    break;
                case "simpleall":
                    servant = new S_simpleAll();
                    break;
                case "skill":
                    servant = new S_skill();
                    break;
                case "getstatus":
                    servant = new S_getStatus();
                    break;
                default:
                    throw new ResultErrorException("Unknown method");
            }
            servant.set(_info,_collection,_with_proxy);
            servantlist.add(servant);
        }
    }
    public void run()
    {
        for (int i=0;i<servantlist.size();i++)
        {
            Servant servant = servantlist.get(i);
            Document result = servant.run();
            Document resultinfo = info.append("result",result.getString("result"));
            if (i+1<servantlist.size())
            {
                servantlist.get(i+1).info = resultinfo;
                System.out.println(servantlist.get(i+1).info);
            } else {
                return;
            }
        }
    }
}
