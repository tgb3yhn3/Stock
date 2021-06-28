import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.io.IOException;

//--------------------------------------------------主程式------------------------------------

public class ChivesReunion{
    public static void main(String[] args)throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException, IOException{
        MainFrame mainFrame = new MainFrame();//啟動GUI主畫面
        /*VolumeCSV tmp=new VolumeCSV();
        Calendar today=Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR,-2);
        tmp.dataGetter(today.getTime());
        tmp.initialize();
        tmp.writeInCSV(365);*/
    }
}
