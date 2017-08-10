import com.mongodb.async.client.MongoCollection;
import org.bson.Document;

import javax.print.Doc;
import java.util.Map;

/**
 * Created by zjkgf on 2017/7/29.
 */
abstract public class Servant {
    protected Document info;
    protected MongoCollection<Document> collection;
    protected boolean with_proxy;
    Map<String, Object> resultmap;

    public void set(Document _info, MongoCollection<Document> _collection)
    {
        this.info = _info;
        this.collection = _collection;
        this.with_proxy = ProxyChooser.WITH_PROXY;
    }
    abstract Document run();
}
