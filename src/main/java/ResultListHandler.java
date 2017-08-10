import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zjkgf on 2017/8/9.
 */
public class ResultListHandler {
    public static List<Map<String, Object>> totallist;
    public static Map<String, Object> GetNew(){
        Map<String, Object> temp = new HashMap<>();
        totallist.add(temp);
        return temp;
    }
    public static void main(String[] args){
        for (int i=0;i<1000;i++)
        {
            Map<String, Object> tmp = GetNew();
        }
    }
}
