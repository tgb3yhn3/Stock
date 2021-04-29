import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

public class Connection{
    private String stringURL;
    private String encoding;
    public Connection(String stringURL, String encoding){
        this.stringURL = stringURL;
        this.encoding = encoding;
    }

    public String getUrlData(){
        //下載網路資料
        String urlData = "";
        try{
            //1.產生URL定址物件
            URL myUrl = new URL(stringURL);
            //2.連線
            URLConnection myConn = myUrl.openConnection();
            myConn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //3.取得輸出串流
            InputStream in = myConn.getInputStream();
            //4.抓取串流到Buffer物件
            BufferedInputStream bis = new BufferedInputStream(in);
            ByteArrayBuffer baf = new ByteArrayBuffer(bis.available());
            int data = 0;
            while((data = bis.read())!=-1){ //讀取BufferedInputStream中資料
                baf.append((byte)data); //將資料置入ByteArrayBuffer中
            }
            //5.設定編碼
            urlData = EncodingUtils.getString(baf.toByteArray(), encoding); //轉換為字串
            in.close();
            return urlData;
        } 
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}


//URL:https://goodinfo.tw/StockInfo/StockDetail.asp?STOCK_ID=