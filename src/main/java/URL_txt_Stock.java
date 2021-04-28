import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;


public class URL_txt_Stock{
    static List<String> col01 = new ArrayList<String>();
    static List<String> col03 = new ArrayList<String>();
    static String urlData = null;

    public static void main(String[] args){
        //下載網路資料

        String stockNum = "2458";
        String stringURL = "https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_"+ stockNum+".tw&json=1&delay=0" ;
        //String stringURL = "https://www.cmoney.tw/follow/channel/stock-2458?chart=d&type=Personal";
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
            //5.轉換為UTF-8編碼
            urlData = EncodingUtils.getString(baf.toByteArray(), "UTF-8"); //轉換為字串
            //System.out.println(urlData);
        } catch(Exception e){
            e.printStackTrace();
        }
        //2.切出所需要的資料
        Parser(urlData, stockNum);
        //3.輸出資料
        //輸出標題:
        System.out.println("股票代碼\t成交");
        //輸出內容
        for(int i = 0; i < col01.size(); i++){
            System.out.println(col01.get(i).toString() + "\t" + col03.get(i)); 
        }
    }
    //切出所需的資料

    public static void Parser(String urlData, String stockNum){
        String temp = null;
        int start = 0, end = 0, counter = 0;
        do{
            start = urlData.indexOf("<nobr><a class='link_blue' style='font-size:14pt;font-weight:bold;' href='StockDetail.asp?STOCK_ID=" + stockNum, end + 1);
            end = urlData.indexOf("</a></nobr>", start + 1);
           // https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_0050.tw&json=1&delay=0&_=1619444911003
            String name = urlData.substring(start+115, end);
            
            start = urlData.indexOf("<td style='font-weight:bold;color:red'>", end + 1);
            end = urlData.indexOf("</td>", start + 1);
            String price = urlData.substring(start + 39,end);
            //col01.add(temp);

            /*start = urlData.indexOf("<td align=\"center\" bgcolor=\"#FFFfff\" nowrap><b>", end + 1);
            end = urlData.indexOf("</a><br>", start + 1);
            temp = urlData.substring(start + 47, end);
            col03.add(temp);*/

            counter++;
        } while(counter <1);
    }
}
//<a class="link_blue" style="font-size:14pt;font-weight:bold;" href="StockDetail.asp?STOCK_ID=2458">2458&nbsp;義隆</a>
//href="StockDetail.asp?STOCK_ID=2458">2458&nbsp;義隆</a>