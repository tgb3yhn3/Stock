//----------------------------------------對帳單--------------------------------

public class Statement {
    private final String stockNum;      //股票代號
    private final long handlingFee;     //手續費(*0.001425)
    private final long transactionTax;  //交易稅(*0.003)
    private final long price;           //價格
    private final int quantity;         //張數
    private final long total;           //應收付金額

    public Statement(String stockNum, long price, int quantity, long handlingFee, long transactionTax){
        this.stockNum = stockNum;
        this.price = price;
        this.quantity = quantity;
        this.handlingFee = handlingFee;
        this.transactionTax = transactionTax;
        if (handlingFee==0)  total = (price - transactionTax) * quantity;    //賣單，應收金額
        else total = -(price + handlingFee) * quantity;      //買單，應付金額
    }

    public String getStockNum() {
        return stockNum;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getHandlingFee() {
        return handlingFee;
    }

    public long getTotal() {
        return total;
    }

    public long getPrice() {
        return price;
    }

    public long getTransactionTax() {
        return transactionTax;
    }

    public String toString(){
        return String.format("%s    手續費: %d     交易稅: %d     價格: %d      張數: %d      應收付金額: %d", stockNum, handlingFee, transactionTax,price,quantity,total);
    }
}
