import java.util.ArrayList;
import java.util.List;

public class StringReverse {
    public static List<String[]> reverse( List<String[]>origin){
        List<String[]>reverse=new ArrayList<>();
        for(int i=0;i<origin.size();i++){
            reverse.add(origin.get(origin.size()-i-1));
        }
        return reverse;
    }
}
