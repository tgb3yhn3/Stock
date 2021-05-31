import java.util.ArrayList;
import java.util.List;

//-----------------------------------自選股列表-----------------------

public class SelfSelectedStocks{
    private String name;           //列表名稱
    private List<String> list;     //股票列表

    public SelfSelectedStocks(String name){
        this.name = name;
        list = new ArrayList<String>();
    }
    public void addElm(String stockNum){        //增加股票代號
        list.add(stockNum);
    }       //增加股票代號
    public void removeElm(String stockNum){     //刪除指定股票代號
        list.remove(stockNum);
    }       //刪除股票代號
    public List<String> getList(){     //取得列表
        return list;
    }       //取得列表
    public void showList(){        //輸出列表
        System.out.println(name + ":");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d: %s%n", i+1, list.get(i));
        }
        System.out.println();
    }


    public void setName(String name){       //設定列表名稱
        this.name = name;
    }

    public String getName(){
        return name;
    }
}