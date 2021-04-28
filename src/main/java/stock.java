import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;

public class stock {
    public static void main(String[] args) throws InterruptedException{

            String[] CodeStr = {"2458","0050","2330","0051"};//鍵入股票代號，支數不限
            String theURL = "";
            URL u;
            URLConnection myConn;
            BufferedReader theHtml;
            String name = "";
            String nowTime = "";
            String Recommand = "";
            String nowPrice = "";
            String nowBuyPrice = "";
            String nowSellPrice = "";
            String nowChange = "";
            String nowDiffRate = "";
            String nowQuantities = "";
            String yesterdayPrice = "";
            String opening = "";
            String high = "";
            String low = "";
            String finalURL="https://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_"+ CodeStr[0]+".tw";

        for(int Code = 1 ; Code < CodeStr.length ; Code++ ) {
            finalURL+="|"+"tse_"+CodeStr[Code]+".tw";
        }
        finalURL+="&json=1&delay=0";
        Connection mycon = new Connection(finalURL);
//json格式拆裝
        for(int z = 0 ; z < 1 ; z++){//執行次數只要可以cover開盤到收盤的時間即可
                String theWholePackage=mycon.getUrlData();
                JSONObject json = new JSONObject(theWholePackage);
                String dataArray[]=datahandler.dataprocess(json.get("msgArray").toString().substring(1,json.get("msgArray").toString().length()-1),CodeStr.length);
                System.out.println("代號\t時間\t成交價\t買價\t賣價\t漲跌\t漲跌幅\t成交量\t昨收\t開盤\t最高\t最低");
                for(int Code = 0 ; Code < CodeStr.length ; Code++ ){
                        //取字
                        //System.out.println( json.get("msgArray").toString().substring(1,json.get("msgArray").toString().length()-1));
                        JSONObject json2 =new JSONObject(dataArray[Code]);
                        System.out.printf("%s %7s %s %.5s  %5s%n", json2.get("c"),json2.get("n"),json2.get("t"),json2.get("z"),json2.get("v"));
                        //,((Float.parseFloat(json2.get("z").toString())-Float.parseFloat(json2.get("y").toString()))/Float.parseFloat(json2.get("y").toString())*100)
                }
                System.out.println();
                Thread.sleep(1000);//停一秒
            }
        }
    }
