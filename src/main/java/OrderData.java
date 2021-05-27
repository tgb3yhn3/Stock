public class OrderData{ //到價通知 委託買賣單
    private String stockNum;
    private String price;
    private String choose;

    public OrderData(String stockNum, String price, String choose){
        this.stockNum = stockNum;
        this.price = price;
        this.choose = choose;
    }

    public String getStockNum() {
        return stockNum;
    }

    public String getChoose() {
        return choose;
    }

    public String getPrice() {
        return price;
    }
}