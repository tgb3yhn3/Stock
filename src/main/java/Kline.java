import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.text.*;
import java.util.*;
import java.util.List;

public class Kline extends JFrame {

    private JPanel left;//包含圖表和搜尋欄
    private JPanel right;//包含價格(high low open close)
    private JTextField info[];//顯示出價格的TextField
    private OHLCDataItem[] data;//存OHLC的地方 (OHLC=open high low close price)
    private JTextField stockNumInputField;//股票號碼輸入欄位
    private JTextField dateInputFieldStart;//起始日期輸入欄位
    private JTextField dataInputFieldEnd;//截止日期輸入欄位
    private JButton searchButton;//搜尋按鈕
    private  volumeCSV volumeFromcsv;
    public Kline(String stockSymbol) {
        super("CandlestickDemo");//set title
        this.setIconImage(new ImageIcon("imageFile\\韭菜.png").getImage());
        setSize(550,450);
        setVisible(true);
        volumeFromcsv=new volumeCSV();
        //----------------左半邊----------------------------------------------------
        left=new JPanel();
        left.setLayout(new BorderLayout());
        JPanel topBar=new JPanel();
        JLabel stockNumLabel=new JLabel("股票代號");
        JLabel dateStartLabel=new JLabel("起始日期");
        JLabel dateEndLabel=new JLabel("截止日期");
        stockNumInputField=new JTextField(10);//股票代號輸入
        dateInputFieldStart=new JTextField(10);//開始日期輸入
        dataInputFieldEnd=new JTextField(10);//結束日期輸入
        searchButton=new JButton("Search");//搜尋按鈕
        stockNumInputField.setText(stockSymbol);
        dateInputFieldStart.setText("20200614");
        dataInputFieldEnd.setText("20210614");
        topBar.add(stockNumLabel);topBar.add(stockNumInputField);
        topBar.add(dateStartLabel);topBar.add(dateInputFieldStart);
        topBar.add(dateEndLabel);topBar.add(dataInputFieldEnd);
        topBar.add(searchButton);//加入資料搜尋欄


        ChartPanel chartPanel = new ChartPanel(getChart(stockNumInputField.getText()), false);//用搜尋欄位的stockNum 做出chart
        chartPanel.setPreferredSize(new Dimension(600, 300));
        left.add(topBar,BorderLayout.NORTH);
        left.add(chartPanel,BorderLayout.CENTER);
        chartPanel.setMouseWheelEnabled(true);
        /*chartPanel.addFocusListener(new FocusAdapter() {//不知作用
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                e.getCause().toString();
            }
        });*/

        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent chartMouseEvent) {

            }
            @Override
            public void chartMouseMoved(ChartMouseEvent e) {//滑鼠在K棒上的話右邊跑出東西

                if(e.getEntity().getToolTipText()!=null){


                    //System.out.println(((((DefaultOHLCDataset) e.getChart().getXYPlot().getDataset()).getVolume(0,0))));
                    String dataGet=e.getEntity().getToolTipText();//取得K棒上的資料
                    info[0].setText("股票代號"+dataGet.substring(0,dataGet.indexOf("-->")));
                    info[1].setText("日期   : "+dataGet.substring(dataGet.indexOf("Date=")+"Date=".length(),dataGet.indexOf("上午")));
                    info[2].setText("High  : "+dataGet.substring(dataGet.indexOf("High=")+"High=".length(),dataGet.indexOf("Low=")));
                    info[3].setText("Low   : "+dataGet.substring(dataGet.indexOf("Low=")+"Low=".length(),dataGet.indexOf("Open=")));
                    info[4].setText("Open  : "+dataGet.substring(dataGet.indexOf("Open=")+"Open=".length(),dataGet.indexOf("Close=")));
                    info[5].setText("Close : "+dataGet.substring(dataGet.indexOf("Close=")+"Close=".length()));

                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy/M/d");
                    Long volume=0L;
                    try {

                        volume=volumeFromcsv.getDateVolume(sdf.parse(info[1].getText().substring(info[1].getText().indexOf(":")+1))).get(stockSymbol);
                        //System.out.println(stockSymbol+":"+volume);
                    }catch (ParseException parseException){
                        parseException.printStackTrace();
                    }
                    info[6].setText("Volume:"+volume);
                }
            }
        });


        //-----------------右半邊----------------------------------
        right=new JPanel();
        int TextFieldNum=7;//欄位數
        info=new JTextField[TextFieldNum];//TextField陣列

        right.setLayout(new GridLayout(TextFieldNum,1));

        for(int i=0;i<TextFieldNum;i++){//把資料填入TextField
            info[i]=new JTextField();
            info[i].setText(Integer.toString(i));
            info[i].setEditable(false);
            right.add(info[i]);
        }
        searchButton.addActionListener(new ActionListener() {//按下搜尋按鈕
            @Override
            public void actionPerformed(ActionEvent e) {
                chartPanel.setChart(getChart(stockNumInputField.getText()));
            }
        });
        this.setLayout(new GridLayout(1,3));
        this.add(left);
        this.add(right);

        this.pack();

    }
    private  JFreeChart getChart(String stockSymbol){//畫圖表
        DateAxis    domainAxis       = new DateAxis("Date");
        NumberAxis  rangeAxis        = new NumberAxis("Price");
        CandlestickRenderer renderer = new CandlestickRenderer();
        XYDataset   dataset          = getDataSet(stockSymbol);


        XYPlot mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

        renderer.setUseOutlinePaint(true);
        renderer.setUpPaint(Color.red);//如果是漲-> red
        renderer.setDownPaint(Color.green);//如果是跌 -> green
        renderer.setDrawVolume(true);//draw volume
        renderer.setVolumePaint(Color.black);//volume color

        renderer.setCandleWidth(3);//K棒的寬度
        renderer.setAutoWidthMethod(100);
        rangeAxis.setAutoRangeIncludesZero(false);//自動設置range


        //Now create the chart
        JFreeChart chart = new JFreeChart(stockSymbol, null, mainPlot, false);
        mainPlot.setDomainPannable(true);
        mainPlot.setRangePannable(true);
        return  chart;
    }
    protected AbstractXYDataset getDataSet(String stockSymbol) {//取得資料
        //This is the dataset we are going to create
        DefaultOHLCDataset result = null;
        //This is the data needed for the dataset
        OHLCDataItem[] data;

        //This is where we go get the data, replace with your own data source
        data = getData(stockSymbol);

        //Create a dataset, an Open, High, Low, Close dataset
        result = new DefaultOHLCDataset(stockSymbol, data);//從URL取得資料
        //result.getVolume(0,0)

        return result;
    }
    //This method uses yahoo finance to get the OHLC data
    protected OHLCDataItem[] getData(String stockSymbol) {//從URL取得資料
        List<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();
        try {

            String stockNum=stockNumInputField.getText();

            String dateStart=dateInputFieldStart.getText();
            String dateEnd=dataInputFieldEnd.getText();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
            Date startTime=ft.parse(dateStart);
            Date endTime=ft.parse(dateEnd);
//            System.out.println(dateStart);
//            System.out.println(dateEnd);
            String strUrl= "https://query1.finance.yahoo.com/v7/finance/download/"+stockNum+".TW?period1="+(startTime.getTime()/1000-86400)+"&period2="+endTime.getTime()/1000+"&interval=1d&events=history&includeAdjustedClose=true";
            URL url = new URL(strUrl);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            DateFormat df = new SimpleDateFormat("y-M-d");

            String inputLine;
            in.readLine();
            while ((inputLine = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(inputLine, ",");

                Date date       = df.parse( st.nextToken() );
                double open     = Double.parseDouble( st.nextToken() );
                double high     = Double.parseDouble( st.nextToken() );
                double low      = Double.parseDouble( st.nextToken() );
                double close    = Double.parseDouble( st.nextToken() );
                double volume   = Double.parseDouble( st.nextToken() );
                double adjClose = Double.parseDouble( st.nextToken() );//用不到

                OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
                dataItems.add(item);

            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Data from Yahoo is from newest to oldest. Reverse so it is oldest to newest
        Collections.reverse(dataItems);

        //Convert the list into an array
         data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);

        return data;
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/M/d");
        try {
            System.out.println(sdf.parse("2020/1/2"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}