import org.json.*;
public class FindStockData{
    
    private String stockNum;

    public FindStockData(String stockNum){
        this.stockNum = stockNum;
    } 

    public boolean isTse(){     //判斷是否為上市
        Connection myConnection = new Connection("https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_" + stockNum +  ".tw", "UTF-8");
        String urlData = myConnection.getUrlData();
        if(urlData.indexOf("[")+1!=urlData.indexOf("]")) return true;
        return false;
    }
    public Stocks getInfo(){
        Connection myConnection;
        if(isTse()) myConnection = new Connection("https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_" + stockNum +  ".tw", "UTF-8");    //建立上市公司URL連線
        else myConnection = new Connection("https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=otc_" + stockNum +  ".tw", "UTF-8");       //建立上櫃公司URL連線
        String urlData = myConnection.getUrlData();
        String subUrlData = urlData.substring(urlData.indexOf("[") + 1, urlData.indexOf("]"));      //去頭尾
        JSONObject json = new JSONObject(subUrlData);       //轉為JSON
        String name = json.getString("n");      //股名
        String price = json.getString("pz");      //成交價
        String volume = json.getString("v");        //成交量
        String openingPrice = json.getString("o");      //開盤價
        String maxPrice = json.getString("h");      //最高
        String minPrice = json.getString("l");      //最低
        String upDownRate = String.format("%.2f", 100 * (Double.parseDouble(price) - Double.parseDouble(json.getString("y"))) / Double.parseDouble(json.getString("y")) );      //漲跌幅
	    Stocks theStock = new Stocks(name, stockNum, price, volume, openingPrice, maxPrice, minPrice, upDownRate);
        return theStock;
    }
}