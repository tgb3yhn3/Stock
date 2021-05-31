import org.jsoup.Jsoup;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class StocksGUI_SelfSelectedList extends JFrame{

    private Map<String,ArrayList<String>> userSelfSelectedListDataMap = new HashMap<>();//記錄使用者在GUI上的自選股清單

    public StocksGUI_SelfSelectedList(StocksGUI mainFrame){
        //創建頁面視窗
        super("韭菜同學會_自選股清單");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int windowWidth = 870;//設定視窗寬度
        int windowHeight = 450;//設定視窗高度
        setSize(windowWidth, windowHeight);
        setLocation(mainFrame.getX(),mainFrame.getY());//此視窗出現的位置將在主頁面的位置

        //把上市、上櫃csv檔案中的所有個股放入map，將用於搜索個股(按分類)用
        Map<String,ArrayList<String>> listedStocksMap = new HashMap<>();//讀取到的上市股票號碼資料將整理至listedStocksMap中，以便查詢類股用
        Map<String,ArrayList<String>> overTheCounterStocksMap = new HashMap<>();//讀取到的上櫃股票號碼資料將整理至overTheCounterStocksMap中，以便查詢類股用
        try {
            File listedStockNumCsv = new File("csvFile\\SelfSelectedListGUI_listedStockNum.csv");  // 上市股票號碼 CSV檔案路徑
            if(listedStockNumCsv.isFile() && listedStockNumCsv.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(listedStockNumCsv), "ms950");//讀取csv時不會出現中文亂碼
                BufferedReader reader = new BufferedReader(read);
                String readLine;//讀取到的一行內容給readLine變數
                while((readLine = reader.readLine())!=null){
                    String[] readLineData = readLine.split(",");
                    String stockClassName = readLineData[0];
                    ArrayList<String> stocks = new ArrayList<String>();
                    for(int i=1;i<readLineData.length;i++){
                        stocks.add(readLineData[i]);
                    }
                    listedStocksMap.put(stockClassName,stocks);//讀取到的內容放進map
                }
                reader.close();
            }
            File overTheCounterStockNumCsv = new File("csvFile\\SelfSelectedListGUI_overTheCounterStockNum.csv"); // 上櫃股票號碼 CSV檔案路徑
            if(overTheCounterStockNumCsv.isFile() && overTheCounterStockNumCsv.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(overTheCounterStockNumCsv), "ms950");//讀取csv時不會出現中文亂碼
                BufferedReader reader = new BufferedReader(read);
                String readLine;//讀取到的一行內容給readLine變數
                while((readLine = reader.readLine())!=null){
                    String[] readLineData = readLine.split(",");
                    String stockClassName = readLineData[0];
                    ArrayList<String> stocks = new ArrayList<String>();
                    for(int i=1;i<readLineData.length;i++){
                        stocks.add(readLineData[i]);
                    }
                    overTheCounterStocksMap.put(stockClassName,stocks);//讀取到的內容放進map
                }
                reader.close();
            }
        } catch (FileNotFoundException notFoundException) {
            notFoundException.printStackTrace();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        //創建搜索區
        JPanel searchStockPanel = new JPanel(new BorderLayout());

        //創建搜索個股(按分類)區
        JPanel searchStockByClassPanel = new JPanel();
        searchStockByClassPanel.setBorder(BorderFactory.createTitledBorder("搜索個股(按分類)"));
        JPanel stocksMainClassPanel = new JPanel();//創建總類列表(搜索個股區_第1欄)
        stocksMainClassPanel.setBorder(BorderFactory.createTitledBorder("分類"));
        JList<String> stocksMainClassList = new JList<String>(new String[]{"上市", "上櫃"});
        JScrollPane stocksMainClassListAddJSP = new JScrollPane(stocksMainClassList);//給JList加滾動條
        stocksMainClassList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//此JList內同時只能選擇一個項目
        stocksMainClassListAddJSP.setPreferredSize(new Dimension(120,240));
        stocksMainClassPanel.add(stocksMainClassListAddJSP);
        JPanel stocksClassPanel = new JPanel();//創建類股列表(搜索個股區_第2欄)
        stocksClassPanel.setBorder(BorderFactory.createTitledBorder("類股"));
        String[] listedStocks = new String[]{
                "水泥","食品","塑膠","紡織","電機","電器電纜","化學","生技醫療","玻璃","造紙",
                "鋼鐵","橡膠","汽車","半導體","電腦週邊","光電","通信網路","電子零組件","電子通路","資訊服務",
                "其它電子","建材營造","航運","觀光","金融","貿易百貨","油電燃氣","其他"};//上市
        String[] overTheCounterStocks =new String[]{
                "食品","塑膠","紡織","電機","電器電纜","化學","生技醫療","玻璃", "鋼鐵","橡膠",
                "半導體","電腦週邊","光電","通信網路","電子零組件","電子通路","資訊服務","其它電子","建材營造","航運",
                "觀光","金融","貿易百貨","油電燃氣","文創","農業科技","電子商務","其他"};//上櫃
        JList<String> stocksClassList = new JList<String>();
        JScrollPane stocksClassListAddJSP = new JScrollPane(stocksClassList);//給JList加滾動條
        stocksClassList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//此JList內同時只能選擇一個項目
        stocksClassListAddJSP.setPreferredSize(new Dimension(120,240));
        stocksClassPanel.add(stocksClassListAddJSP);
        JPanel stocksPanel = new JPanel();//創建個股列表(搜索個股區_第3欄)
        stocksPanel.setBorder(BorderFactory.createTitledBorder("個股"));
        JList<String> stocksList = new JList<String>();
        JScrollPane stocksListAddJSP = new JScrollPane(stocksList);//給JList加滾動條
        stocksList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);//此JList內同時只能選擇一個項目
        stocksListAddJSP.setPreferredSize(new Dimension(120,240));
        stocksPanel.add(stocksListAddJSP);
        searchStockByClassPanel.add(stocksMainClassPanel);//搜索個股區加入總類列表(搜索個股區_第1欄)
        searchStockByClassPanel.add(stocksClassPanel);//搜索個股區加入類股列表(搜索個股區_第2欄)
        searchStockByClassPanel.add(stocksPanel);//搜索個股區加入個股列表(搜索個股區_第3欄)
        JButton addInButton_searchStockByClass = new JButton("加入個股");
        searchStockByClassPanel.add(addInButton_searchStockByClass);
        searchStockPanel.add(searchStockByClassPanel,BorderLayout.NORTH);

        //創建搜索個股(使用者輸入)區
        JPanel searchStockByInputPanel = new JPanel();
        searchStockByInputPanel.setBorder(BorderFactory.createTitledBorder("搜索個股(請輸入股票號碼)"));
        JTextField searchStockTextField = new JTextField("",10);
        JButton addInButton_searchStockByInput = new JButton("加入個股");
        searchStockByInputPanel.add(searchStockTextField);
        searchStockByInputPanel.add(addInButton_searchStockByInput);
        searchStockPanel.add(searchStockByInputPanel,BorderLayout.SOUTH);

        //創建使用者的自選股清單區
        JPanel selfSelectedListPanel = new JPanel(new BorderLayout());
        selfSelectedListPanel.setBorder(BorderFactory.createTitledBorder("管理自選股清單"));
        JPanel functionPanel = new JPanel(new GridLayout(2,2));//編輯列表功能按鈕區

        JComboBox selfSelectedListComboBox = new JComboBox();//讓使用者選擇編輯哪一個自選股清單
        JList<String> selfSelectedList = new JList<>();//顯示使用者當前選擇的自選股清單內容
        JScrollPane selfSelectedListAddJSP = new JScrollPane(selfSelectedList);//給JList加滾動條
        selfSelectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);//此JList內同時可選擇多個項目
        selfSelectedListAddJSP.setPreferredSize(new Dimension(120,250));
        JButton addNewListButton = new JButton("新增列表");
        JButton deleteListButton = new JButton("刪除當前列表");
        JButton deleteStockFromListButton = new JButton("刪除個股");
        JButton upDateButton = new JButton("更新資料");
        functionPanel.add(addNewListButton);
        functionPanel.add(deleteListButton);
        functionPanel.add(deleteStockFromListButton);
        functionPanel.add(upDateButton);
        selfSelectedListPanel.add(selfSelectedListComboBox,BorderLayout.NORTH);
        selfSelectedListPanel.add(selfSelectedListAddJSP,BorderLayout.CENTER);
        selfSelectedListPanel.add(functionPanel,BorderLayout.SOUTH);

        //為視窗新增GUI子元件
        add(searchStockPanel);
        add(selfSelectedListPanel);

        /**以下為GUI元件註冊事件處理器**/
        //總類(上市/上櫃)JList事件
        stocksMainClassList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int choose = stocksMainClassList.getSelectedIndex();
                if(choose==0){//選擇上市
                    stocksClassList.setListData(listedStocks);
                    stocksList.setListData(new String[]{});
                }
                else if(choose==1){//選擇上櫃
                    stocksClassList.setListData(overTheCounterStocks);
                    stocksList.setListData(new String[]{});
                }
            }
        });

        //類股JList事件
        stocksClassList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    int currentMainClass = stocksMainClassList.getSelectedIndex();//當前查詢上市還是上櫃
                    if(currentMainClass==0){  //上市
                        for(String keyOF_listedStocksMap:listedStocksMap.keySet()){
                            //找到使用者選取的類股，並在stocksList列出其中的所有個股
                            if(stocksClassList.getSelectedValue().equals(keyOF_listedStocksMap)){
                                //更新JList中的資料
                                String[] stocksListData = new String[listedStocksMap.get(keyOF_listedStocksMap).size()];
                                stocksList.setListData(listedStocksMap.get(keyOF_listedStocksMap).toArray(stocksListData));
                            }
                        }
                    }
                    else if(currentMainClass==1){  //上櫃
                        for(String keyOF_overTheCounterStocksMap:overTheCounterStocksMap.keySet()){
                            //找到使用者選取的類股，並在stocksList列出其中的所有個股
                            if(stocksClassList.getSelectedValue().equals(keyOF_overTheCounterStocksMap)){
                                //更新JList中的資料
                                String[] stocksListData = new String[overTheCounterStocksMap.get(keyOF_overTheCounterStocksMap).size()];
                                stocksList.setListData(overTheCounterStocksMap.get(keyOF_overTheCounterStocksMap).toArray(stocksListData));
                            }
                        }
                    }
                }

            }
        });

        //新增列表Button事件
        addNewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName = JOptionPane.showInputDialog(StocksGUI_SelfSelectedList.this,"請輸入列表名稱");
                if(listName==null){//取消輸入(按下cancel)

                }else if(listName.equals("")){//按下ok但是沒輸入任何字
                    JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"列表名稱不得為空!");
                }else{//成功輸入
                    if(listNameIsRepeated(listName,userSelfSelectedListDataMap.keySet())){//不允許出現重複名稱列表
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"該列表名稱已經存在!");
                    }else{
                        ArrayList<String> listData = new ArrayList<>();
                        selfSelectedListComboBox.addItem(listName);
                        userSelfSelectedListDataMap.put(listName,listData);
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"新增列表成功");
                        saveDataToCSV();
                    }
                }
            }
        });

        //刪除列表Button事件
        deleteListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selfSelectedListComboBox.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"目前沒有選中列表");
                }else{
                    if(JOptionPane.showConfirmDialog(StocksGUI_SelfSelectedList.this,"是否刪除當前列表?","刪除列表",JOptionPane.YES_NO_OPTION)==0){//當選擇"yes"
                        String deletedListName = (String)selfSelectedListComboBox.getSelectedItem();
                        selfSelectedListComboBox.removeItem(selfSelectedListComboBox.getSelectedItem());
                        userSelfSelectedListDataMap.remove(deletedListName);
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"刪除列表成功!");
                        saveDataToCSV();
                    }
                }
            }
        });

        //刪除個股Button事件(刪除列表中的個股)
        deleteStockFromListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName = (String) selfSelectedListComboBox.getSelectedItem();
                if(listName==null){
                    if(selfSelectedListComboBox.getItemCount()==0){
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"沒有列表存在!");
                    }else{
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"尚未選中列表");
                    }
                }
                else{
                    List<String> deleteStockNumS = selfSelectedList.getSelectedValuesList();//使用者選中的所有個股名稱及代號
                    if(deleteStockNumS.size() == 0){
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"請在自選股清單中選取要刪除的個股");
                    }
                    else{
                        for(String deleteStockNum:deleteStockNumS){
                            userSelfSelectedListDataMap.get(listName).remove(deleteStockNum);
                            //更新JList中的資料
                            String[] listData = new String[userSelfSelectedListDataMap.get(listName).size()];
                            selfSelectedList.setListData(userSelfSelectedListDataMap.get(listName).toArray(listData));//於自選股清單的JList顯示該列表的資料*/
                        }
                        saveDataToCSV();
                    }
                }
            }
        });

        //更新資料Button事件
        upDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choose = JOptionPane.showConfirmDialog(StocksGUI_SelfSelectedList.this,"將從資料庫讀取自選股清單，目前的資料可能會遺失，確定嗎？","更新資料",JOptionPane.YES_NO_OPTION);
                if(choose==0){
                    loadDataFromCSV(selfSelectedListComboBox,selfSelectedList);
                    JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"更新資料成功!");
                }
            }
        });

        //切換列表事件
        selfSelectedListComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selfSelectedListComboBox.getItemCount()==0){
                    selfSelectedList.setListData(new String[]{});
                }else if(selfSelectedListComboBox.getItemCount()!=1){
                    String listName = (String) selfSelectedListComboBox.getSelectedItem();
                    //更新JList中的資料
                    String[] listData = new String[userSelfSelectedListDataMap.get(listName).size()];
                    selfSelectedList.setListData(userSelfSelectedListDataMap.get(listName).toArray(listData));//於自選股清單的JList顯示該列表的資料
                }
            }
        });

        //加入個股(按分類)Button事件
        addInButton_searchStockByClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName = (String) selfSelectedListComboBox.getSelectedItem();
                if(listName  ==null){
                    if(selfSelectedListComboBox.getItemCount()==0){
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"沒有列表存在!請先新增列表");
                    }else{
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"尚未選中列表");
                    }
                }
                else{
                    List<String> addInStockNumS = stocksList.getSelectedValuesList();//使用者選中的所有個股名稱及代號
                    if(addInStockNumS.size() == 0){
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"尚未選中個股");
                    }
                    else{
                        for(String addInStockNum:addInStockNumS){
                            userSelfSelectedListDataMap.get(listName).add(addInStockNum);
                            //更新JList中的資料
                            String[] listData = new String[userSelfSelectedListDataMap.get(listName).size()];
                            selfSelectedList.setListData(userSelfSelectedListDataMap.get(listName).toArray(listData));//於自選股清單的JList顯示該列表的資料
                        }
                        saveDataToCSV();
                    }
                }
            }
        });

        //加入個股(按股票號碼)Button事件
        addInButton_searchStockByInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String listName = (String) selfSelectedListComboBox.getSelectedItem();
                if(listName == null){//選中列表為空(null)
                    if(selfSelectedListComboBox.getItemCount()==0){//沒有列表存在
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"沒有列表存在!請先新增列表");
                    }else{//有列表存在，但是尚未選中列表
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"尚未選中列表");
                    }
                }
                else{
                    String targetStockNum = searchStockTextField.getText();
                    if(targetStockNum.equals("")){//輸入為空
                        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"請輸入欲搜尋的股票號碼");
                    }else{
                        if(targetStockNum.length()!=4){//檢查使用者輸入的股票號碼是否是四位股票號碼
                            JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"找不到該個股!");
                            searchStockTextField.setText("");
                        }else{//正確輸入，開始搜尋
                            searchStockByInput(listName, listedStocksMap, overTheCounterStocksMap,targetStockNum,selfSelectedList,searchStockTextField);
                        }
                    }
                }
            }
        });


        //讀取csv檔中的自選股清單資料，結果顯示在selfSelectedList這個JList
        loadDataFromCSV(selfSelectedListComboBox,selfSelectedList);
        setVisible(true);
    }

    //檢查使用者即將建立的列表名稱是否已經存在(即不能有重複名字的列表)
    public boolean listNameIsRepeated(String listName,Set<String> keySet){
        for(String key:keySet){
            if (key.equals(listName)){
                return true;
            }
        }
        return false;
    }

    //從上市/上櫃的所有個股中尋找使用者輸入的股票名稱或股票號碼
    public void searchStockByInput(String listName, Map<String,ArrayList<String>> listedStocksMap, Map<String,ArrayList<String>> overTheCounterStocksMap, String targetStockNum,JList<String> selfSelectedList,JTextField searchStockTextField){
        for(String mapKeyName:listedStocksMap.keySet()){//從上市的所有股票號碼中尋找目標股票號碼
            for(String StockNumWithName:listedStocksMap.get(mapKeyName)){
                if(StockNumWithName.contains(targetStockNum)){//找到目標股票號碼了，在自選股列表中添加目標股票號碼
                    userSelfSelectedListDataMap.get(listName).add(StockNumWithName);
                    //更新JList中的資料
                    String[] listData = new String[userSelfSelectedListDataMap.get(listName).size()];
                    selfSelectedList.setListData(userSelfSelectedListDataMap.get(listName).toArray(listData));//於自選股清單的JList顯示該列表的資料
                    saveDataToCSV();
                    return;//已經新增成功，停止尋找
                }
            }
        }
        for(String mapKeyName:overTheCounterStocksMap.keySet()){//從上櫃的所有股票號碼中尋找目標股票號碼
            for(String StockNumWithName:overTheCounterStocksMap.get(mapKeyName)){
                if(StockNumWithName.contains(targetStockNum)){//找到目標股票號碼了，在自選股列表中添加目標股票號碼
                    userSelfSelectedListDataMap.get(listName).add(StockNumWithName);
                    //更新JList中的資料
                    String[] listData = new String[userSelfSelectedListDataMap.get(listName).size()];
                    selfSelectedList.setListData(userSelfSelectedListDataMap.get(listName).toArray(listData));//於自選股清單的JList顯示該列表的資料
                    saveDataToCSV();
                    return;//已經新增成功，停止尋找
                }
            }
        }
        JOptionPane.showMessageDialog(StocksGUI_SelfSelectedList.this,"找不到該個股!");
        searchStockTextField.setText("");
        return;
    }

    //將GUI的自選股清單儲存至自選股清單csv檔
    public void saveDataToCSV(){
        try {
            File selfSelectedListsDataCsv = new File("csvFile\\selfSelectedListsData.csv");//自選股清單的csv檔
            if(selfSelectedListsDataCsv.isFile() && selfSelectedListsDataCsv.exists()){
                OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(selfSelectedListsDataCsv), "ms950");//寫入csv時不會出現中文亂碼
                BufferedWriter writer = new BufferedWriter(write);
                for(String listName:userSelfSelectedListDataMap.keySet()){
                    String lineData = listName+",";
                    for(String stockNum:userSelfSelectedListDataMap.get(listName)){
                        lineData += stockNum+",";
                    }
                    writer.write(lineData);//寫入檔案(一行代表一個自選股列表)
                    writer.newLine();//換行寫入
                }
                writer.close();//加上這行才能把之前所有寫入的東西正確的寫入到檔案中
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //將自選股清單csv檔讀取至GUI的自選股清單
    public void loadDataFromCSV(JComboBox selfSelectedListComboBox,JList<String> selfSelectedList){
        userSelfSelectedListDataMap = new HashMap<>();//清空GUI的自選股清單資料
        try{
            File selfSelectedListsDataCsv = new File("csvFile\\selfSelectedListsData.csv");//自選股清單的csv檔
            if(selfSelectedListsDataCsv.isFile() && selfSelectedListsDataCsv.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(selfSelectedListsDataCsv), "ms950");//讀取csv時不會出現中文亂碼
                BufferedReader reader = new BufferedReader(read);
                String readLine;//讀取到的一行內容給readLine變數
                while((readLine = reader.readLine())!=null){
                    String[] readLineData = readLine.split(",");
                    String listName = readLineData[0];//列表名稱
                    if(!listName.equals("")){
                        ArrayList<String> listData = new ArrayList();
                        for(int i=1;i<readLineData.length;i++){
                            listData.add(readLineData[i]);
                        }
                        userSelfSelectedListDataMap.put(listName,listData);
                        selfSelectedListComboBox.addItem(listName);
                        String[] listData1 = new String[listData.size()];//列表內容
                        selfSelectedList.setListData(listData.toArray(listData1));//於自選股清單的JList顯示該列表的資料
                    }
                }
                reader.close();
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
    
}
