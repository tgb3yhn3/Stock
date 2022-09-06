# Real Time模擬交易及交易策略制定選股機器人
下載完此專題後，基本上可以直接使用，但可能會碰到以下問題:
1. 憑證問題: 需要到以下網域抓取憑證，安裝於本地
       
       康和證券:
       https://concords.moneydj.com/Customfile/index.htm
       
       Yahoo Finance:
       https://finance.yahoo.com/
       
       臺灣證交所:
       https://www.twse.com.tw/zh/
       
       證券櫃買中心:
       https://www.tpex.org.tw/web/index.php?l=zh-tw
       
       富聯網:
       https://ww2.money-link.com.tw/
    
    安裝辦法: 透過cmd輸入以下命令
    
    keytool -import -alias A1 -keystore "C:\Program Files\Java\jdk-15.0.2\lib\security\cacerts" -file (檔案位址及名稱)


2. csv檔案問題:
    若選股機器人出任何問題，或者個股基本面或三大法人資料出問題，請按更新按鈕更新資料
   
無法解決問題:
   1. 證交所即時API 5秒才更新一次，且爬蟲不能抓太快，有機會被封，被封則無法使用即時資料相關功能。此問題仍無法解決。
   2. 透過多線程爬蟲很吃網速等影響因素，目前設定抓取資料失敗重試10次，但若超過了仍會跳出Exception，代表連線出錯。
   3. 大家較有疑慮的問題是搓合的問題，因為我們使用的是API，功能會受限制，無法實際模擬真正的搓合機制，若要模擬真實的需付一大筆金額去租用API。目前只能以買盤最高價，賣盤最低價來當作成交條件
