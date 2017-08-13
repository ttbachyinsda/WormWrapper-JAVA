import java.io.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zjkgf on 2017/8/13.
 */
public class OutPrinter {
    public static Queue<String> resultlist = new ConcurrentLinkedQueue<>();
    public static void Print(String result){
        resultlist.add(result);
    }
    public static void Clear(){
        int num = 0;
        try {
            FileOutputStream out = new FileOutputStream(new File("./src/"+String.valueOf(System.currentTimeMillis())+".txt"));
            while (num < 10000 && !resultlist.isEmpty())
            {
                num = num + 1;
                out.write(resultlist.poll().getBytes());
                out.write("\n".getBytes());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
