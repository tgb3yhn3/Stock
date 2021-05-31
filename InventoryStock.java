//-------------------------------------庫存資訊---------------------------------

public class InventoryStock {
    private long averagePrice;      //平均成本
    private int quantity;           //張數
    public InventoryStock(long price, int quantity){
        this.averagePrice = (long)(price*1.001425);     //加入手續費
        this.quantity = quantity;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void add(int n, long price) {    //新增張數
        this.averagePrice = (long)(( averagePrice*quantity + (price*1.001425)*n) / (quantity + n));     //新成本=((舊成本*舊張數 + 新價格(加手續費)*新增張數) / 總張數
        this.quantity += n;     //新增n張
//
    }

    public void delete(int n){      //刪減張數
        if(quantity-n<=0) {
            averagePrice = 0;     //剩餘張數0以下 清空平均成本
            quantity = 0;
        }
        else quantity -= n;     //張數減少
    }

    public String toString(){
        return String.format("平均成本: %d  張數: %d", averagePrice, quantity);
    }

}
