import java.util.ArrayList;
import java.util.List;

//-----------------------------------------三大法人買賣超------------------------------------

public class InstitutionalInvestors {
    private List<String> date;              //日期
    private List<String> foreignInvestor;   //外資買賣超
    private List<String> investmentTrust;   //投信買賣超
    private List<String> dealer;            //自營商買賣超
    private final String url;

    public InstitutionalInvestors(String stockNum){
        url = "https://stock.wearn.com/netbuy.asp?year=110&month=05&kind=" + stockNum;
        date = new ArrayList<String>();
        foreignInvestor = new ArrayList<String>();
        investmentTrust = new ArrayList<String>();
        dealer = new ArrayList<String>();
    }

    public void getInfo(){
        Connection myConnection = new Connection(url, "big5");  //建立URL連線
        String urlData = myConnection.getUrlData();
        int start = 0, end = 0;
        end = urlData.indexOf("<tr class=\"stockalllistbg2\">");
        for(int i = 0; i < 20; i++){     //近20個交易日的三大法人買賣超
            start = urlData.indexOf("<td align=\"center\">", end) + 19;
            end = urlData.indexOf("</td>", start + 1);
            date.add(urlData.substring(start, end));        //日期

            start = urlData.indexOf("<td align=\"right\">", end) + 18;
            end = urlData.indexOf("&nbsp;", start + 1);
            investmentTrust.add(urlData.substring(start, end));     //投信

            start = urlData.indexOf("<td align=\"right\">", end) + 18;
            end = urlData.indexOf("&nbsp;", start + 1);
            dealer.add(urlData.substring(start, end));      //自營商

            start = urlData.indexOf("<td align=\"right\">", end) + 18;
            end = urlData.indexOf("&nbsp;", start + 1);
            foreignInvestor.add(urlData.substring(start, end));     //外資

            //test
            //System.out.printf("%s: 外資: %s, 投信: %s, 自營商: %s%n", date, foreign, trust, dealer);
        }
        //test
        //for(int i = 0; i < date.size(); i++) System.out.printf("%s: 外資: %s, 投信: %s, 自營商: %s%n", date.get(i), foreignInvestor.get(i), investmentTrust.get(i), dealer.get(i));

    }
}