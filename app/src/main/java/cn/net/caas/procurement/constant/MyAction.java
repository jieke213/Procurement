package cn.net.caas.procurement.constant;

/**
 * Created by wjj on 2017/4/27.
 */
public class MyAction {
    //采购员--我的订单
    public static final String APPLYCLAIMS = "com.action.applyclaims";
    public static final String CANCEL_ORDER = "com.action.cancelorder";


    //课题组长--报销审核
    public static final String LEADER_TOCLAIME_PASS = "com.action_leadertoclaimepass";
    public static final String LEADER_TOCLAIME_REFUSE = "com.action_leadertoclaimerefuse";


    //课题组长--采购审核
    public static final String LEADER_BUY_PASS = "com.action.leaderbuypass";
    public static final String LEADER_BUY_REFUSE = "com.action.leaderbuyrefuse";


    //研究所--报销审核
    public static final String INSTIUTE_TOCLAIME_PASS = "com.action.instiutetoclaimepass";
    public static final String INSTIUTE_TOCLAIME_REFUSE = "com.action.instiutetoclaimerefuse";


    //研究所--采购审核
    public static final String INSTIUTE_BUY_PASS = "com.action.instiutebuypass";
    public static final String INSTIUTE_BUY_REFUSE = "com.action.instiutebuyrefuse";


    //验货员--验货员订单
    public static final String CHECK_PASS = "com.action.checkpass";
    public static final String PARTARRIVAL = "com.action.partarrival";
    public static final String COMPLETEREFUND = "com.action.completerefund";


    //财务--财务订单
    public static final String CAIWU_PASS = "com.action.caiwupass";
    public static final String CAIWU_REFUSE = "com.action.caiwurefuse";
}
