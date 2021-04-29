import java.util.ArrayList;
public class SelfSelectedStocks{
    private ArrayList<String> list;     //自選股清單

    public SelfSelectedStocks(){
        list = new ArrayList<String>();
    }
    public void addElm(String stockNum){        //增加股票代號
        list.add(stockNum);
    }
    public void removeElm(String stockNum){     //刪除指定股票代號
        list.remove(stockNum);
    }
    public ArrayList<String> getList(){     //取得列表
        return list;
    }
    public void printList(){        //輸出列表
        for (String stockNum : list) {
            System.out.println(stockNum);
        }
    }
    public void getStockData(int index){       //取得股票資訊
        String stockNum = list.get(index);
        FindStockData stockData = new FindStockData(stockNum);
        Stocks myStock = stockData.getInfo();
        //System.out.println("代號\t股名\t成交價\t成交量\t漲跌幅\t開盤價\t最高\t最低");
        System.out.println(myStock);
    }
}