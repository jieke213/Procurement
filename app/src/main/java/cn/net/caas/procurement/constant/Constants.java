package cn.net.caas.procurement.constant;

/**
 * Created by wjj on 2017/1/20.
 */
public class Constants {
    public static final String JPUSH_KEY="key";
    public static final String JPUSH_ORDER="order";

    public static final String LOGIN_INFO="loginInfo";
    public static final String ACCESS_TOKEN="access_token";
    public static final String TOKEN_SAVE_TIME="startSaveTime";
    public static final String USERNAME="username";
    public static final String PASSWORD="password";

    public static final String ACCOUNT_URL="http://b.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=b4e487fb7bf082022d92963973c09c" +
            "d0/77094b36acaf2eddba632a31851001e938019353.jpg";

    public static final String LOGIN_BASE_URL="http://106.14.104.106/platform/app/login.lf?";
    public static final String ORDER_BASE_URL="http://106.14.104.106/platform/app/order/main/get.lf?";

    public static final String TEAMNAME_URL="http://106.14.104.106/platform/app/dept/teams/get.lf?";//获取课题组
    public static final String LEADER_PASS="http://106.14.104.106/platform/app/order/op/leaderPass.lf?";//课题组长审核通过
    public static final String LEADER_REFUSE="http://106.14.104.106/platform/app/order/op/leaderRefuse.lf?";//课题组长拒绝
    public static final String INSTIUTE_PASS="http://106.14.104.106/platform/app/order/op/instiutePass.lf?";//研究所审核通过
    public static final String INSTIUTE_REFUSE="http://106.14.104.106/platform/app/order/op/instiuteRefuse.lf?";//研究所拒绝
    public static final String NOTIFICATION="http://106.14.104.106/platform/app/msg/get.lf?";//获取通知





    //获取菜单
    public static final String MENU_URL="http://106.14.104.106/platform/app/menu/get.lf?";//获取菜单

    //组长/首席采购审核
    public static final String ORDER_LEADER_REVIEW="http://106.14.104.106/platform/app/order/leader/review.lf?";//组长/首席采购审核-待审批订单
    public static final String ORDER_MAIN_DEPT="http://106.14.104.106/platform/app/order/main/dept/get.lf?";//组长/首席采购审核-所有订单

    //组长/首席报销审核
    public static final String ORDER_LEADER_TOCLAIME="http://106.14.104.106/platform/app/order/leader/toClaime.lf?";//组长/首席报销审核 - 待审批订单
    public static final String ORDER_SUB_DEPT="http://106.14.104.106/platform/app/order/sub/dept/get.lf?";//组长/首席报销审核 - 所有订单

    //验货人订单
    public static final String ORDER_CHECKER_PARTARRIVAL="http://106.14.104.106/platform/app/order/checker/partArrival.lf?";//验货人订单-部分到货订单
    public static final String ORDER_CHECKER_TOCHECK="http://106.14.104.106/platform/app/order/checker/tocheck.lf?";//验货人订单-待验货订单
    public static final String ORDER_DETAIL="http://106.14.104.106/platform/app/order/detail.lf";//验货人订单-扫码验货-扫码查询接口

    //我的订单
    public static final String ORDER_BUYER_NOTPASS="http://106.14.104.106/platform/app/order/buyer/notPass.lf?";//我的订单-失败订单
    public static final String ORDER_BUYER_ALL="http://106.14.104.106/platform/app/order/buyer/all.lf?";//我的订单-所有订单
    public static final String ORDER_BUYER_UNCLAIMED="http://106.14.104.106/platform/app/order/buyer/unclaimed.lf?";//我的订单-未报销订单
    public static final String ORDER_BUYER_DELIVERED="http://106.14.104.106/platform/app/order/buyer/delivered.lf?";//我的订单-未到货订单

    //所领导采购审核
    public static final String ORDER_BOSS_REVIEW="http://106.14.104.106/platform/app/order/boss/review.lf?";//所领导采购审核-待审批订单

    //所领导报销审核
    public static final String ORDER_BOSS_TOCLAIME="http://106.14.104.106/platform/app/order/boss/toClaime.lf?";//所领导报销审核  - 待审批订单

    //财务报销(同上)


    /**
     * 数据分析
     */
    public static final int DATA_ANALYSE=67;//数据分析
    public static final int DATA_ANALYSE_KTJTJ=70;//课题级统计
    public static final int DATA_ANALYSE_GRTJ=71;//个人统计

    /**
     * 订单管理
     */
    public static final int ORDER_MANAGER=13;//订单管理

    //订单管理（课题组长1/2 测试账号）
    public static final int ORDER_MANAGER_WDDD=14;//我的订单
    public static final int ORDER_MANAGER_ZZCGSH=15;//组长/首席采购审核
    public static final int ORDER_MANAGER_ZZBXSH=25;//组长/首席报销审核

    //订单管理（所长/副所长 测试账号）
    public static final int ORDER_MANAGER_SLDCGSH=23;//所领导采购审核
    public static final int ORDER_MANAGER_SLDBXSH=26;//所领导报销审核

    //订单管理（财务测试账号）
    public static final int ORDER_MANAGER_CWBX=27;//财务报销

    //订单管理（验货员测试账号）
    public static final int ORDER_MANAGER_YHRDD=24;//验货人订单

    //订单管理（采购员测试账号）
    //同上


    /**
     * 竞价管理
     */
    public static final int BID_MANAGER=17;//竞价管理

    //
    public static final int PERSON_INFORMATION_MANAGER=22;//个人信息管理


    /**
     * -----------------------------------------------操作------------------------------------------------------------------
     */
    public static final String  ORDER_OP_CHECKPASS="http://106.14.104.106/platform/app/order/op/checkPass.lf?";//验货通过
    public static final String  ORDER_OP_PARTARRIVAL="http://106.14.104.106/platform/app/order/op/partArrival.lf?";//部分到货
    public static final String  ORDER_OP_COMPLETEREFUND="http://106.14.104.106/platform/app/order/op/completeRefund.lf?";//全部退货
    public static final String  ORDER_OP_REPLACE="http://106.14.104.106/platform/app/order/op/replace.lf?";//验货人换货


    public static final String  ORDER_OP_COMPLETECLAIMS="http://106.14.104.106/platform/app/order/op/completeClaims.lf?";//财务报销通过
    public static final String  ORDER_OP_NOTCOMPLETECLAIMS="http://106.14.104.106/platform/app/order/op/notCompleteClaims.lf?";//财务报销不通过


    public static final String  ORDER_OP_BUYERCANCEL="http://106.14.104.106/platform/app/order/buyer/cancle.lf?";//采购员取消订单
    public static final String  ORDER_OP_BUYERAPPLYCLAIMS="http://106.14.104.106/platform/app/order/op/applyClaims.lf?";//采购员申请报销


    public static final String  ORDER_OP_LEADERCLAIMSPASS="http://106.14.104.106/platform/app/order/op/leaderReviewClaimsPass.lf?";//课题组报销通过
    public static final String  ORDER_OP_LEADERCLAIMSREFUSE="http://106.14.104.106/platform/app/order/op/leaderReviewClaimsRefuse.lf?";//课题组报销不通过
    public static final String  ORDER_OP_INSTIUTECLAIMSPASS="http://106.14.104.106/platform/app/order/op/instiuteReviewClaimsPass.lf?";//研究所报销通过
    public static final String  ORDER_OP_INSTIUTECLAIMSREFUSE="http://106.14.104.106/platform/app/order/op/instiuteReviewClaimsRefuse.lf?";//研究所报销不通过



    //fragment切换的标识
    public static final String FRAGMENT_FLAG_HOME="首页";
    public static final String FRAGMENT_FLAG_OTHER="其他";
    public static final String FRAGMENT_FLAG_CART="购物车";
    public static final String FRAGMENT_FLAG_ORDER="我的订单";
    public static final String FRAGMENT_FLAG_MINE="我的";

    public static final String CATEGORY_REAGENT="实验试剂";
    public static final String CATEGORY_CONSUMABLES="实验耗材";
    public static final String CATEGORY_INSTRUMENT="仪器设备";
    public static final String CATEGORY_AGRICULTURAL="农资农机";
    public static final String CATEGORY_TECSERVICE="技术服务";
    public static final String CATEGORY_OFFICE="办公用品";


    /**
     * 课题组长审核中
     */
    public static final int LEADER_REVIEW = 0;
    /**
     * 研究所审核中
     */
    public static final int INSTIUTE_REVIEW = 1;
    /**
     * 审核通过
     */
    public static final int REVIEW_PASS = 2;
    /**
     * 订单确认
     */
    public static final int CONFIRM_ORDER = 3;
    /**
     * 已发货
     */
    public static final int DELIVERED = 4;
    /**
     * 验货中(部分到货)
     */
    public static final int CHECKING = 5;
    /**
     * 订单完成（验货通过）
     */
    public static final int ORDER_DONE = 6;
    /**
     * 课题组长报销审核中
     */
    public static final int LEADER_REVIEW_EXPENSES_CLAIMS = 7;
    /**
     * 研究所报销审核中
     */
    public static final int INSTIUTE_REVIEW_EXPENSES_CLAIMS = 8;
    /**
     * 报销中
     */
    public static final int CLAIMING = 9;
    /**
     * 完成报销
     */
    public static final int DONE_EXPENSES_CLAIMS = 10;
    /**
     * 审核未通过
     */
    public static final int REVIEW_NOT_PASS = -1;
    /**
     * 取消订单
     */
    public static final int CANCEL_ORDER = -2;
    /**
     * 已退货
     */
    public static final int REFUNDED_PRODUCTS = -3;

    /**
     * 报销未通过
     */
    public static final int REVIEW_EXPENSES_CLAIMS_NOT_PASS = -4;
}
