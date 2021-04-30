import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ChivesReunion{
    public static void main(String[] args)throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException{
        SelfSelectedStocks myList = new SelfSelectedStocks();
        FindStockData stockData;

        Scanner input = new Scanner(System.in);
        String stockNum, yyyyMMdd;
        System.out.println("Please choose the functions:");
        System.out.println("1. 查詢上市櫃股");
        System.out.println("2. 自選股清單");
        System.out.println("3. 到價通知:");
        System.out.println("4. 模擬下單");
        System.out.println("5. 選股機器人");
        System.out.println("6. 更新資料庫");
        System.out.println("7. 結束");
        int choose = input.nextInt();
        while(choose!=7){
            if(choose==1){
                System.out.println("Please enter the stock number and date:");
                stockNum = input.next();
                //yyyyMMdd = input.next();
                stockData = new FindStockData(stockNum);
                Stocks myStock = stockData.getInfo();
                System.out.println(myStock);
            }
            else if(choose==2){
                System.out.println("Please choose the functions:");
                System.out.println("1. 加入股票");
                System.out.println("2. 刪除股票");
                System.out.println("3. 顯示自選股");
                System.out.println("4. 選取個股");
                System.out.println("5. 返回");
                int chooseListFunc = input.nextInt();
                while(chooseListFunc!=5){
                    if(chooseListFunc==1){
                        System.out.println("Please enter the stock number:");
                        stockNum = input.next();
                        myList.addElm(stockNum);
                    }
                    else if(chooseListFunc==2){
                        System.out.println("Please enter the stock number:");
                        stockNum = input.next();
                        myList.removeElm(stockNum);
                    }
                    else if(chooseListFunc==3){
                        myList.printList();
                    }
                    else if(chooseListFunc==4){
                        System.out.println("Please enter the index:");
                        int index = input.nextInt();
                        myList.getStockData(index);
                    }
                    System.out.println("Please choose the functions:");
                    System.out.println("1. 加入股票");
                    System.out.println("2. 刪除股票");
                    System.out.println("3. 顯示自選股");
                    System.out.println("4. 選取個股");
                    System.out.println("5. 返回");
                    chooseListFunc = input.nextInt();
                }

            }
            else if(choose==3){
                
            }
            else if(choose==4){
                
            }
            else if(choose==5){
                
            }
            else if(choose==6){
                Fundamental fund = new Fundamental();
                fund.updateRevenue();
            }
            System.out.println("Please choose the functions:");
            System.out.println("1. 查詢上市櫃股");
            System.out.println("2. 自選股清單");
            System.out.println("3. 到價通知:");
            System.out.println("4. 模擬下單");
            System.out.println("5. 選股機器人");
            System.out.println("6. 更新資料庫");
            System.out.println("7. 結束");
            choose = input.nextInt();
        }
    }
}