package net.swordie.ms.loaders.containerclasses;

public class CashItemInfo {
    private int itemId;

    private int count;

    private int price;

    private int sn;

    public CashItemInfo(int sn, int itemId, int count, int price) {
        this.sn = sn;
        this.itemId = itemId;
        this.count = count;
        this.price = price;
    }

    public int getSn(){
        return sn;
    }

    public int getItemId() {
        return itemId;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}
