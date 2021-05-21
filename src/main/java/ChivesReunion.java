import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

//--------------------------------------------------主程式------------------------------------

public class ChivesReunion{
    public static void main(String[] args)throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException{
        SelfSelectedList myList = new SelfSelectedList();       //自選股清單
        Inventory myInventory = new Inventory();                //庫存
        StatementList myStatement = new StatementList();
        Stocks stock;
        StocksGUI stocksGUI = new StocksGUI();//啟動GUI主畫面
        Scanner input = new Scanner(System.in);
        String stockNum, yyyyMMdd, name;
        int index;

        System.out.println("Please choose the functions:");
        System.out.println("1. 查詢上市櫃股");
        System.out.println("2. 自選股清單");
        System.out.println("3. 帳務查詢");
        System.out.println("4. 模擬下單");
        System.out.println("5. 選股機器人");
        System.out.println("6. 更新資料庫");
        System.out.println("7. 結束");
        int choose = input.nextInt();
        while(choose!=7){
            if(choose==1){
                System.out.println("Please enter the stock number:");
                stockNum = input.next();
                //yyyyMMdd = input.next();
                stock = new Stocks(stockNum);
                stock.updateRealTime();
                stock.updateInstitution();
                stock.getHistoricalPrice();
                //stock.realTime();;
                System.out.println(stock);
            }

            //----------------------------------------自選股清單---------------------------------------
            else if(choose==2){
                System.out.println("Please choose the functions:");
                System.out.printf("1. 新增列表%n2. 刪除列表%n3. 選擇列表%n4. 返回%n");
                int chooseListFunc = input.nextInt();
                while(chooseListFunc!=4){
                    //新增列表
                    if(chooseListFunc==1){
                        System.out.println("Please enter the list name:");
                        name = input.next();
                        myList.addList(name);
                    }
                    //刪除列表
                    else if(chooseListFunc==2){
                        System.out.println("Please enter the list index:");
                        myList.showList();
                        index = input.nextInt();
                        myList.deleteList(index);
                    }
                    //選擇列表
                    else if(chooseListFunc==3){
                        System.out.println("Please enter the list index:");
                        myList.showList();
                        index = input.nextInt();
                        SelfSelectedStocks list  = myList.getList(index);
                        System.out.printf("1. 加入股票%n2. 刪除股票%n3. 顯示列表%n4. 選取個股%n5. 更改列表名稱%n6. 返回%n");
                        int chooseListFuncIn = input.nextInt();
                        while(chooseListFuncIn!=6){
                            //加入股票
                            if(chooseListFuncIn==1){
                                System.out.println("Please enter the stock number:");
                                stockNum = input.next();
                                list.addElm(stockNum);
                            }
                            //刪除股票
                            else if(chooseListFuncIn==2){
                                System.out.println("Please enter the stock number:");
                                list.showList();
                                stockNum = input.next();
                                list.removeElm(stockNum);
                            }
                            //顯示列表
                            else if(chooseListFuncIn==3){
                                list.showList();
                            }
                            //選取個股
                            else if(chooseListFuncIn==4){
                                list.showList();
                                System.out.println("Please enter the index:");
                                index = input.nextInt();
                                stock = list.getStock(index);
                                System.out.println(stock);
                            }
                            //更改列表名稱
                            else if(chooseListFuncIn==5){
                                System.out.println("Please enter the list name:");
                                name = input.next();
                                list.setName(name);
                            }
                            System.out.printf("1. 加入股票%n2. 刪除股票%n3. 顯示列表%n4. 選取個股%n5. 更改列表名稱%n6. 返回%n");
                            chooseListFuncIn = input.nextInt();
                        }
                    }

                    System.out.println("Please choose the functions:");
                    System.out.printf("1. 新增列表%n2. 刪除列表%n3. 選擇列表%n4. 返回%n");
                    chooseListFunc = input.nextInt();
                }

            }
            //----------------------------------------帳務查詢---------------------------------------
            else if(choose==3){
                myStatement.addStatement(myInventory.addStock("2458", 100000, 2));
                myStatement.addStatement(myInventory.deleteStock("2458", 120000, 1));
                System.out.println("Please choose the functions:");
                System.out.printf("1. 庫存%n2. 對帳單%n3. 返回%n");
                int chooseAccountFunc = input.nextInt();
                while(chooseAccountFunc!=3){
                    if(chooseAccountFunc==1){
                        myInventory.showInventory();
                    }
                    else if(chooseAccountFunc==2){
                        myStatement.showStatement();
                    }
                    System.out.printf("1. 庫存%n2. 對帳單%n3. 返回%n");
                    chooseAccountFunc = input.nextInt();
                }
            }
            //----------------------------------------模擬下單---------------------------------------
            else if(choose==4){
            }
            ////--------------------------------------選股機器人---------------------------------------
            else if(choose==5){

            }
            //----------------------------------------更新營收，財報---------------------------------------
            else if(choose==6){
                Fundamental fund = new Fundamental();
                fund.updateRevenue();
            }
            System.out.println("Please choose the functions:");
            System.out.println("1. 查詢上市櫃股");
            System.out.println("2. 自選股清單");
            System.out.println("3. 帳務查詢");
            System.out.println("4. 模擬下單");
            System.out.println("5. 選股機器人");
            System.out.println("6. 更新資料庫");
            System.out.println("7. 結束");
            choose = input.nextInt();
        }
    }
}
