# Real Time Simulated Trading and Stock Picking Robot

## Introduction
Most of the stock market software on the market charges extra and has incomplete functions. This inspired me to make a more convenient and fully functional stock market software. This system is connected to the Taiwan Stock Exchange's open data API to capture real-time quotes, and uses a multi-threaded crawler framework to accelerate the capture of securities data, financial information and basic information from four securities information networks, and adds simulated transactions and reconciliations. Single function. In addition, a stock selection robot has been developed that can automatically select companies that meet the conditions by checking dozens of stock selection conditions.

The stock picking robot function of this system has provided great help to me in my daily stock picking, and it is also one of my most commonly used tools. We look forward to putting it on the Android or iOS stores in the future to provide greater convenience.

## Display
### Stock Chart
![image](https://github.com/tgb3yhn3/Stock/assets/76504560/5d515108-9c01-4835-b58e-a98b1e434279)

### Real Time Stock Information
![image](https://github.com/tgb3yhn3/Stock/assets/76504560/5ed86449-72c6-42cf-80e3-c80a729a0d26)

### Stock Picking Robot
![image](https://github.com/tgb3yhn3/Stock/assets/76504560/cfdbf2de-5455-4bd7-aa1f-d1a161f9524f)

## Some problems you may encounter:
Basically, you can directly use it after download the project. However, you may encounter some problems below:
#### 1. Credential issue: You need to go to the following domain to grab the credentials and install them locally.
       
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
    
##### Installation Command
```
keytool -import -alias A1 -keystore "C:\Program Files\Java\jdk-15.0.2\lib\security\cacerts" -file (檔案位址及名稱)
```

#### 2. csv file problem:
If there is any problem with stock picking robot, fundamentals of individual stocks or the data of the institutional investors, please click the update button to update the data.
   
### The problems cannot be solved:

1. The real-time API of the stock exchange is updated only once every 5 seconds, and the crawler cannot crawl too fast, and it may be blocked. If blocked, real-time data related functions cannot be used. This issue still cannot be resolved.
2. The multi-threaded crawler is affected by factors such as network speed. Currently, it is set to retry 10 times if failed to capture data. However, if it exceeds the limit, an Exception will still pop up, indicating a connection error.
3. The issue that everyone is more concerned about is the problem of blending. Because we are using API, the functions will be limited and we cannot actually simulate the real blending mechanism. If we want to simulate the real blending mechanism, we need to pay a large amount of money to rent the API. Currently, only the highest price for buying orders and the lowest price for selling orders can be used as transaction conditions.
