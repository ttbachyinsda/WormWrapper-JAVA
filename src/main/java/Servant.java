import com.mongodb.async.client.MongoCollection;
import org.bson.Document;

import javax.print.Doc;

/**
 * Created by zjkgf on 2017/7/29.
 */
abstract public class Servant {
    protected Document info;
    protected MongoCollection<Document> collection;
    protected boolean with_proxy;
    public void set(Document _info, MongoCollection<Document> _collection, boolean _with_proxy)
    {
        this.info = _info;
        this.collection = _collection;
        this.with_proxy = _with_proxy;
    }
    abstract Document run();
}
