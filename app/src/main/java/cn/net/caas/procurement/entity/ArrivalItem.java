package cn.net.caas.procurement.entity;

/**
 * Created by wjj on 2017/4/14.
 */
public class ArrivalItem {
    private int itemId;
    private int arrivalQuantity;

    public ArrivalItem(int itemId, int arrivalQuantity) {
        this.itemId = itemId;
        this.arrivalQuantity = arrivalQuantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getArrivalQuantity() {
        return arrivalQuantity;
    }

    public void setArrivalQuantity(int arrivalQuantity) {
        this.arrivalQuantity = arrivalQuantity;
    }
}
