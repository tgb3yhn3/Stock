public class PriceVolume {
    private String stockNum;//股票代號
    private Double price;//收盤價
    private Long volume;//成交量

    public PriceVolume(String stockNum, Double price, Long volume) {
        this.stockNum = stockNum;
        this.price = price;
        this.volume = volume;
    }

    public String getStockNum() {
        return stockNum;
    }

    public Double getPrice() {
        return price;
    }

    public Long getVolume() {
        return volume;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "priceVolume{"  +
                ", price=" + price +
                ", volume=" + volume +
                '}';
    }
}
