import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.mongodb.async.client.MongoCollection;
import org.bson.Document;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class ServantEventMain {
    public static RingBuffer<ServantFactory> ringBuffer;
    public static MongoCollection<Document> collection;
    public static Disruptor<ServantFactory> disruptor;
    public static void handleEvent(ServantFactory servantFactory, long sequence, boolean endOfBatch)
    {
        servantFactory.run();
    }
    public static void translate(ServantFactory servantFactory, long sequence, Document info, MongoCollection<Document> collection, boolean with_proxy)
    {
        try {
            servantFactory.set(info, collection, with_proxy);
        } catch (ResultErrorException e) {
            e.printStackTrace();
        }
    }
    public static void readyrun() throws Exception
    {
        int bufferSize = 131072;
        disruptor = new Disruptor<>(ServantFactory::new, bufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(ServantEventMain::handleEvent);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }
    public static void forcestop()
    {
        disruptor.halt();
    }
}
