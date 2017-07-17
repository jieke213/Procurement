package cn.net.caas.procurement.constant;

/**
 * Created by wjj on 2017/4/26.
 */
public class ResultCode {
    //我的订单
    public static final int APPLYCLAIMS_MYORDER = -1;
    public static final int CANCEL_ORDER = -12;//取消订单


    //验货员订单
    public static final int CHECKPASS = -2;//验货通过
    public static final int PARTARRIVAL = -3;//部分到货
    public static final int COMPLETEREFUND = -4;//全部退货


    //课题组长报销审核
    public static final int LEADER_TOCLAIME_PASS = -8;//课题组报销通过
    public static final int LEADER_TOCLAIME_REFUSE = -9;//课题组报销拒绝


    //研究所报销审核
    public static final int INSTIUTE_TOCLAIME_PASS = -10;//研究所报销审核通过
    public static final int INSTIUTE_TOCLAIME_REFUSE = -11;//研究所报销审核拒绝


    //财务报销
    public static final int CAIWU_TOCLAIME_PASS = -13;//财务报销通过
    public static final int CAIWU_TOCLAIME_REFUSE = -14;//财务报销拒绝


    //研究所采购审核
    public static final int INSTIUTE_BUY_PASS = -15;//研究所采购审核通过
    public static final int INSTIUTE_BUY_REFUSE = -16;//研究所采购审核拒绝


    //课题组长采购审核
    public static final int LEADER_BUY_PASS = -17;//课题组长采购审核通过
    public static final int LEADER_BUY_REFUSE = -18;//课题组长采购审核拒绝

}
