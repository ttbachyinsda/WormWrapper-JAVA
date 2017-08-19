import java.io.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zjkgf on 2017/8/13.
 */
public class OutPrinter {
    public static Queue<String> resultlist = new ConcurrentLinkedQueue<>();
    public static Queue<String> resultlist2 = new ConcurrentLinkedQueue<>();
    public static String prefix = "hotlist";
    public static String prefix2 = "logic";
    public static void Print(String result){
        //System.out.println(result);
        resultlist.add(result);
    }
    public static void Print2(String result){
        //System.out.println(result);
        resultlist2.add(result);
    }
    public static void Clear(){
        int num = 0;
        try {
            String pathname = "./src/"+prefix+String.valueOf(System.currentTimeMillis())+".txt";
            System.out.println(pathname);
            FileOutputStream out = new FileOutputStream(new File(pathname));
            while (!resultlist.isEmpty())
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
    public static void Clear2(){
        int num = 0;
        try {
            String pathname = "./src/"+prefix2+String.valueOf(System.currentTimeMillis())+".txt";
            System.out.println(pathname);
            FileOutputStream out = new FileOutputStream(new File(pathname));
            while (!resultlist2.isEmpty())
            {
                num = num + 1;
                out.write(resultlist2.poll().getBytes());
                out.write("\n".getBytes());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
