import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//------------*--------------------------個股即時資訊--------------------------------

public class RealTimeInfo {
    private final String url;           //證交所API之URL
    private final String stockNum;      //股票代號
    private String stockName;           //股票名稱
    private String price;               //成交價
    private String volume;              //成交量
    private String openingPrice;        //開盤價
    private String maxPrice;            //當日最高
    private String minPrice;            //當日最低
    private String upDownRate;          //漲跌幅
    private List<String> buyPrice;      //五檔內盤價
    private List<String> sellPrice;     //五檔外盤價
    private List<String> buyVolume;     //五檔內盤量
    private List<String> sellVolume;    //五檔外盤量

    public RealTimeInfo(String stockNum){
        //判斷是上市或上櫃 決定url
        this.stockNum = stockNum;
        Connection myConnection = new Connection("https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_" + stockNum +  ".tw&json=1&delay=0", "UTF-8");
        String urlData = myConnection.getUrlData();
        if(urlData.indexOf("[")+1!=urlData.indexOf("]")) this.url = "https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_" + stockNum + ".tw&json=1&delay=0";
        else this.url = "https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=otc_" + stockNum + ".tw&json=1&delay=0";

        buyPrice = new ArrayList<String>();
        sellPrice = new ArrayList<String>();
        buyVolume = new ArrayList<String>();
        sellVolume = new ArrayList<String>();
    }
    public void getInfo(){
        Connection myConnection = new Connection(url, "UTF-8");  //建立URL連線
        String urlData = myConnection.getUrlData();
        String subUrlData = urlData.substring(urlData.indexOf("[") + 1, urlData.indexOf("]"));      //去頭尾
        JSONObject json = new JSONObject(subUrlData);       //轉為JSON
        //System.out.println(subUrlData);

        this.stockName = json.getString("n");      //股名
        this.price = json.getString("pz");      //成交價
        this.volume = json.getString("v");        //成交量
        this.openingPrice = json.getString("o");      //開盤價
        this.maxPrice = json.getString("h");      //最高
        this.minPrice = json.getString("l");      //最低
        //this.upDownRate = String.format("%.2f", 100 * (Double.parseDouble(price) - Double.parseDouble(json.getString("y"))) / Double.parseDouble(json.getString("y")) );      //漲跌幅

        String bPrice = json.getString("b");      //內盤買價
        this.buyPrice = Arrays.asList(bPrice.split("\\s*_\\s*"));

        String sPrice = json.getString("a");     //外盤賣價
        this.sellPrice = Arrays.asList(sPrice.split("\\s*_\\s*"));

        String bVolume = json.getString("g");      //內盤量
        this.buyVolume = Arrays.asList(bVolume.split("\\s*_\\s*"));

        String sVolume = json.getString("f");      //外盤量
        this.sellVolume = Arrays.asList(sVolume.split("\\s*_\\s*"));
        this.price = buyPrice.get(0);
    }

    public String toString(){
        return String.format("%s%n成交價:%s%n成交量:%s%n漲跌幅:%s%n開盤價:%s%n最高:%s%n最低:%s%n", this.stockName, this.price, this.volume, this.upDownRate, this.openingPrice, this.maxPrice, this.minPrice);
    }
    public String getStockNum(){
        return stockNum;
    }
    public String getStockName(){
        return stockName;
    }
    public double getBuyTopPrice(){
        return Double.parseDouble(buyPrice.get(0));
    }
    public double getSellTopPrice(){
        return Double.parseDouble(sellPrice.get(0));
    }
    public String getPrice(){
        return price;
    }
    public String getVolume(){
        return volume;
    }
    public String getOpeningPrice(){
        return openingPrice;
    }
    public String getMaxPrice(){
        return maxPrice;
    }
    public String getMinPrice(){
        return minPrice;
    }
    public String getUpDownRate(){
        return upDownRate;
    }
    public List<String> getBuyPrice(){ return buyPrice; }
    public List<String> getSellPrice(){ return sellPrice; }
    public List<String> getBuyVolume(){ return buyVolume; }
    public List<String> getSellVolume(){ return sellVolume; }
}
