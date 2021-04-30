import java.util.ArrayList;

public class Stocks{
    private final String name;
    private final String stockNum;
    private String price;
    private String volume;
    private String openingPrice;
    private String maxPrice;
    private String minPrice;
    private String upDownRate;

    public Stocks(String name, String stockNum, String price, String volume, String openingPrice, String maxPrice, String minPrice, String upDownRate){
        this.name = name;
        this.stockNum = stockNum;
        this.price = price;
        this.volume = volume;
        this.openingPrice = openingPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.upDownRate = upDownRate;
    }
    public String getName(){
        return name;
    }
    public String getStockNum(){
        return stockNum;
    }
    public String getPrice(){
        return price;
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
    public void setPrice(String price){
        this.price = price;
    }
    public void setOpeningPrice(String openingPrice){
        this.openingPrice = openingPrice;
    }
    public void setMaxPrice(String maxPrice){
        this.maxPrice = maxPrice;
    }
    public void setMinPrice(String minPrice){
        this.minPrice = minPrice;
    }
    public void setUpDownRate(String upDownRate){
        this.upDownRate = upDownRate;
    }
    public String toString(){
        return String.format("%s%s%n成交價:%s%n成交量:%s%n漲跌幅:%s%n開盤價:%s%n最高:%s%n最低:%s%n", stockNum, name, price, volume, upDownRate, openingPrice, maxPrice, minPrice);
    }

    public void getNews(){      //前往新聞區
        try { 
            String url = "https://goodinfo.tw/StockInfo/StockAnnounceList.asp?START_DT=2021%2F3%2F27&END_DT=2021%2F4%2F26&STOCK_ID=" + stockNum; 
            java.net.URI uri = java.net.URI.create(url); 
            // 獲取當前系統桌面擴充套件 
            java.awt.Desktop dp = java.awt.Desktop.getDesktop(); 
            // 判斷系統桌面是否支援要執行的功能 
            if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                dp.browse(uri);// 獲取系統預設瀏覽器開啟連結 
            } 
        } 
        catch (java.lang.NullPointerException e) { 
            // 此為uri為空時丟擲異常 
            e.printStackTrace(); 
        } 
        catch (java.io.IOException e) { 
            // 此為無法獲取系統預設瀏覽器 
            e.printStackTrace(); 
        }
    } 
}