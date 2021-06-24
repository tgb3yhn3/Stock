import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.security.cert.X509Certificate;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import javax.net.ssl.*;

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
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            //忽略憑證檢驗
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
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