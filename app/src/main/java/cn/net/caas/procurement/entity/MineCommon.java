package cn.net.caas.procurement.entity;

/**
 * Created by wjj on 2017/6/23.
 */
public class MineCommon {
    private String name;
    private int resId;
    private int arrow_enter;

    public MineCommon(String name, int resId, int arrow_enter) {
        this.name = name;
        this.resId = resId;
        this.arrow_enter = arrow_enter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getArrow_enter() {
        return arrow_enter;
    }

    public void setArrow_enter(int arrow_enter) {
        this.arrow_enter = arrow_enter;
    }
}
