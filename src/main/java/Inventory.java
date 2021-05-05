import java.util.Map;
import java.util.HashMap;

//--------------------------------------------庫存，存放多筆庫存資訊------------------------------

public class Inventory {
    private Map<String, InventoryStock> inventoryList;        //庫存list，key為股票代號，value為庫存資訊

    public Inventory(){
        inventoryList = new HashMap<String, InventoryStock>();
    }
    public Statement addStock(String stockNum, long buyPrice, int quantity){        //買入股票
        if(inventoryList.containsKey(stockNum)) inventoryList.get(stockNum).add(quantity, buyPrice);    //已有同代號之庫存
        else inventoryList.put(stockNum, new InventoryStock(buyPrice, quantity));     //沒有同代號庫存，新增一筆
        return new Statement(stockNum, buyPrice, quantity, (long)(buyPrice*0.001425), 0);   //回傳對帳單
    }

    public Statement deleteStock(String stockNum, long sellPrice, int quantity){    //賣出股票
        if(inventoryList.containsKey(stockNum)){      //有庫存
            InventoryStock tmp = inventoryList.get(stockNum);
            tmp.delete(quantity);   //刪減張數
            if(tmp.getQuantity()==0) inventoryList.remove(stockNum);  //庫存賣完，刪除物件
            return new Statement(stockNum, sellPrice, quantity, 0, (long)(sellPrice*0.003));    //回傳對帳單
        }
        else{       //無庫存
            System.out.println("庫存不足");
            return null;
        }
    }

    public void showInventory(){      //顯示全部庫存之均價和張數
        for (String keys : inventoryList.keySet()) System.out.println(keys + " " + inventoryList.get(keys));
    }
}
