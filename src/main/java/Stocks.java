
//---------------------------------------------個股----------------------------------

public class Stocks{
    private final String stockNum;
    private RealTimeInfo realTime;                  //即時資訊
    private InstitutionalInvestors institution;     //三大法人買賣超

    public Stocks(String stockNum){
        this.stockNum = stockNum;
        realTime = new RealTimeInfo(stockNum);
        institution = new InstitutionalInvestors(stockNum);
    }

    public void updateRealTime(){       //更新即時資訊
        realTime.getInfo();
    }

    public void updateInstitution(){        //更新三大法人買賣超
        institution.getInfo();
    }

    public void realTime() throws InterruptedException{     //即時資訊，五秒更新一次，持續更新
        while(true){
            Thread.sleep(5000);
            realTime.getInfo();
            System.out.println(this.toString());
        }
    }

    public String toString(){
        return stockNum + realTime;
    }

    public RealTimeInfo getRealTime(){
        return realTime;
    }       //取得即時資訊
    public void getNews(){      //前往新聞區
        try { 
            String url = "https://tw.stock.yahoo.com/q/h?s=" + stockNum;
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