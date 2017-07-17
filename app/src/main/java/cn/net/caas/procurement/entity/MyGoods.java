package cn.net.caas.procurement.entity;

/**
 * Created by wjj on 2017/2/13.
 */
public class MyGoods {
    private String buyerNick;//采购人
    private String teamName;//课题组
    private String leaderNick;//审批人
    private String projFeeFrom;//经费来源
    private String itemName;//商品名
    private String unitPriceStr;//单价
    private int quantity;//单个商品的数量

    public MyGoods(String buyerNick, String teamName, String leaderNick, String projFeeFrom, String itemName,
                   String unitPriceStr, int quantity) {
        this.buyerNick = buyerNick;
        this.teamName = teamName;
        this.leaderNick = leaderNick;
        this.projFeeFrom = projFeeFrom;
        this.itemName = itemName;
        this.unitPriceStr = unitPriceStr;
        this.quantity = quantity;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLeaderNick() {
        return leaderNick;
    }

    public void setLeaderNick(String leaderNick) {
        this.leaderNick = leaderNick;
    }

    public String getProjFeeFrom() {
        return projFeeFrom;
    }

    public void setProjFeeFrom(String projFeeFrom) {
        this.projFeeFrom = projFeeFrom;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

}
