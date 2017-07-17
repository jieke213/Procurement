package cn.net.caas.procurement.entity;

/**
 * Created by wjj on 2017/4/14.
 */
public class MyPartArrivalGoods {
    private int itemId;
    private int resId;
    private String goodsName;//商品名
    private String supplierPartId;//货号
    private String brandName;//品牌
    private String sku;//单位
    private String specifications;//规格
    private String cycle;//货期
    private String unitPriceStr;//单价
    private int quantity;//数量
    private int arrivalQuantity;//到货的数量
    private int status;//状态

    public MyPartArrivalGoods(int itemId, int resId, String goodsName, String supplierPartId, String brandName, String sku,
                              String specifications, String cycle, String unitPriceStr, int quantity,int arrivalQuantity,int status) {
        this.itemId = itemId;
        this.resId = resId;
        this.goodsName = goodsName;
        this.supplierPartId = supplierPartId;
        this.brandName = brandName;
        this.sku = sku;
        this.specifications = specifications;
        this.cycle = cycle;
        this.unitPriceStr = unitPriceStr;
        this.quantity = quantity;
        this.arrivalQuantity=arrivalQuantity;
        this.status=status;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getSupplierPartId() {
        return supplierPartId;
    }

    public void setSupplierPartId(String supplierPartId) {
        this.supplierPartId = supplierPartId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getUnitPriceStr() {
        return unitPriceStr;
    }

    public void setUnitPriceStr(String unitPriceStr) {
        this.unitPriceStr = unitPriceStr;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getArrivalQuantity() {
        return arrivalQuantity;
    }

    public void setArrivalQuantity(int arrivalQuantity) {
        this.arrivalQuantity = arrivalQuantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
