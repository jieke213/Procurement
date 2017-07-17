package cn.net.caas.procurement.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.AboutActivity;
import cn.net.caas.procurement.ContactUsActivity;
import cn.net.caas.procurement.LoginActivity;
import cn.net.caas.procurement.MainActivity;
import cn.net.caas.procurement.OrderManagerActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.SettingActivity;
import cn.net.caas.procurement.adapter.MineAdapter;
import cn.net.caas.procurement.adapter.MineCommonAdapter;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.entity.Mine;
import cn.net.caas.procurement.entity.MineCommon;
import cn.net.caas.procurement.util.NetworkUtil;
import cn.net.caas.procurement.util.VolleyUtil;
import cn.net.caas.procurement.view.MyGridView;

/**
 * 我的
 * Created by wjj on 2017/4/6.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    private View view;

    private MainActivity mainActivity;

    private List<Mine> list;
    private MineAdapter adapter;
    private List<Integer> list_Id;
    private ArrayList<String> list_childName4OrderManager;
    private ArrayList<Integer> list_childId4OrderManager;


    private SharedPreferences sp;
    private String access_token;

    private MyGridView gv_mine;
    private ImageView btn_setting_mine;

//    private GridView gv_common;
//    private ListView lv_common;
    private LinearLayout layout_common;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine,container,false);

        mainActivity= (MainActivity) getActivity();

        initView();

        initData();

        //从本地获取access_token
        sp=mainActivity.getSharedPreferences(Constants.LOGIN_INFO,mainActivity.MODE_PRIVATE);
        access_token = sp.getString(Constants.ACCESS_TOKEN, "error");
        if (access_token==null || access_token.equals("error")){
            toast("请先登录！");
            Intent intent=new Intent(mainActivity, LoginActivity.class);
            startActivityForResult(intent,001);
        }else{
            showMenu(access_token);
        }

        return view;
    }

    //初始化控件
    private void initView() {
        gv_mine= (MyGridView) view.findViewById(R.id.gv_mine);
        gv_mine.setOnItemClickListener(this);
        btn_setting_mine= (ImageView) view.findViewById(R.id.btn_setting_mine);
        btn_setting_mine.setOnClickListener(this);
//        gv_common= (GridView) view.findViewById(R.id.gv_common);
//        lv_common= (ListView) view.findViewById(R.id.lv_common);
        layout_common= (LinearLayout) view.findViewById(R.id.layout_common);
    }

//    private void initData() {
//        List<Mine> list=new ArrayList<>();
//        list.add(new Mine("收货地址",R.drawable.mine_address));
//        list.add(new Mine("我的收藏",R.drawable.mine_collection));
//        list.add(new Mine("联系客服",R.drawable.mine_service));
//        list.add(new Mine("意见反馈",R.drawable.mine_feedback));
//        list.add(new Mine("帮助中心",R.drawable.mine_help));
//        list.add(new Mine("关于",R.drawable.mine_about));
//        MineAdapter adapter=new MineAdapter(mainActivity,list);
//        gv_common.setAdapter(adapter);
//        gv_common.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                switch (i){
//                    case 0:
//                        Log.i("123","收货地址");
//                        toast("收货地址");
//                        break;
//                    case 1:
//                        Log.i("123","我的收藏");
//                        toast("我的收藏");
//                        break;
//                    case 2:
//                        Log.i("123","联系客服");
//                        toast("联系客服");
//                        break;
//                    case 3:
//                        Log.i("123","意见反馈");
//                        toast("意见反馈");
//                        break;
//                    case 4:
//                        Log.i("123","帮助中心");
//                        toast("帮助中心");
//                        break;
//                    case 5:
//                        Log.i("123","关于");
//                        toast("关于");
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }

    private void initData() {
        for (int i = 0; i < 6; i++) {
            View row = LayoutInflater.from(mainActivity).inflate(R.layout.gridviewitem_minecommon, null);
            ImageView image= (ImageView) row.findViewById(R.id.image_griditem_minecommon);
            TextView tv= (TextView) row.findViewById(R.id.tv_griditem_minecommon);
            ImageView arrow= (ImageView) row.findViewById(R.id.arrow_griditem_minecommon);
            switch (i){
                case 0:
                    image.setImageResource(R.drawable.mine_address);
                    tv.setText("收货地址");
                    arrow.setImageResource(R.drawable.arrow_enter);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toast("收货地址");
                        }
                    });
                    break;
                case 1:
                    image.setImageResource(R.drawable.mine_collection);
                    tv.setText("我的收藏");
                    arrow.setImageResource(R.drawable.arrow_enter);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toast("我的收藏");
                        }
                    });
                    break;
                case 2:
                    image.setImageResource(R.drawable.mine_service);
                    tv.setText("联系我们");
                    arrow.setImageResource(R.drawable.arrow_enter);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mainActivity, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    image.setImageResource(R.drawable.mine_feedback);
                    tv.setText("意见反馈");
                    arrow.setImageResource(R.drawable.arrow_enter);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toast("意见反馈");
                        }
                    });
                    break;
                case 4:
                    image.setImageResource(R.drawable.mine_help);
                    tv.setText("帮助中心");
                    arrow.setImageResource(R.drawable.arrow_enter);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toast("帮助中心");
                        }
                    });
                    break;
                case 5:
                    image.setImageResource(R.drawable.mine_about);
                    tv.setText("关于");
                    arrow.setImageResource(R.drawable.arrow_enter);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(mainActivity, AboutActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
            }
            layout_common.addView(row);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_setting_mine:
                Intent intent=new Intent(mainActivity, SettingActivity.class);
                startActivityForResult(intent,003);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (list_Id.get(i)){
            case Constants.DATA_ANALYSE:
                toast("数据分析");
                break;
            case Constants.ORDER_MANAGER:
                Intent intent=new Intent(mainActivity, OrderManagerActivity.class);
                intent.putIntegerArrayListExtra("list_childId4OrderManager",list_childId4OrderManager);
                intent.putStringArrayListExtra("list_childName4OrderManager",list_childName4OrderManager);
                startActivity(intent);
                break;
            case Constants.BID_MANAGER:
                toast("竞价管理");
                break;
            case Constants.PERSON_INFORMATION_MANAGER:
                toast("个人信息管理");
                break;
            default:
                break;
        }


    }

    //从服务端获取菜单
    private void showMenu(String token) {
        VolleyUtil.get(Constants.MENU_URL + "access_token=" + token, new VolleyUtil.Listener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                list=new ArrayList<>();
                list_Id =new ArrayList<>();
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray == null){
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.optJSONObject(i);
                    int id = data.optInt("id");
                    String name = data.optString("name");

                    if (id==67){
                        list.add(new Mine(name,R.mipmap.sjfx));
                    }else if (id==72){
                        list.add(new Mine(name,R.mipmap.ddspgzsz));
                    }else if (id==13){
                        list.add(new Mine(name,R.mipmap.ddgl));
                    }else if (id==17){
                        list.add(new Mine(name,R.mipmap.jjgl));
                    }else if (id==22){
                        list.add(new Mine(name,R.mipmap.grxxgl));
                    }else{
                        list.add(new Mine(name,R.drawable.pic_order_up));
                    }
//                    list.add(new Mine(name,R.drawable.pic_order_up));
                    list_Id.add(id);

                    //把所有订单管理的子菜单装入List集合中
                    if (id == Constants.ORDER_MANAGER){
                        JSONArray childs = data.optJSONArray("child");
                        list_childId4OrderManager=new ArrayList<>();
                        list_childName4OrderManager =new ArrayList<>();
                        for (int j = 0; j < childs.length(); j++) {
                            JSONObject child = childs.optJSONObject(j);
                            int childId = child.optInt("id");
                            String childName = child.optString("name");
                            Log.i("123","childId: "+childId);
                            Log.i("123","childName: "+childName);
                            list_childName4OrderManager.add(childName);
                            list_childId4OrderManager.add(childId);
                        }
                    }
                }
                adapter=new MineAdapter(mainActivity,list);
                gv_mine.setAdapter(adapter);
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                toast("访问服务器失败！");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("123","MineFragment---onActivityResult");
        if (requestCode==001 && resultCode==002){
            String token = data.getStringExtra("token");
            Log.i("123","MineFragment--token: "+token);
            showMenu(token);
        }
        if (requestCode==003 && resultCode==004){
            //从本地获取access_token
            sp=mainActivity.getSharedPreferences(Constants.LOGIN_INFO,mainActivity.MODE_PRIVATE);
            access_token = sp.getString(Constants.ACCESS_TOKEN, "error");
            if (access_token==null || access_token.equals("error")){
                toast("请先登录！");
                Intent intent=new Intent(mainActivity, LoginActivity.class);
                startActivityForResult(intent,001);
            }else{
                showMenu(access_token);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("123","MineFragment---onResume");
        boolean isHasNetwork = NetworkUtil.isNetworkAvailable(mainActivity);
        if (!isHasNetwork){
            toast("无法连接网络!");
        }else {

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            Log.i("123","MineFragment2隐藏");
        }else{
            Log.i("123","MineFragment2显示");
            boolean isHasNetwork = NetworkUtil.isNetworkAvailable(mainActivity);
            if (!isHasNetwork){
                toast("无法连接网络!");
            }else {

            }

            //从本地获取access_token
            sp=mainActivity.getSharedPreferences(Constants.LOGIN_INFO,mainActivity.MODE_PRIVATE);
            access_token = sp.getString(Constants.ACCESS_TOKEN, "error");
            if (access_token==null || access_token.equals("error")){
                toast("请先登录！");
                Intent intent=new Intent(mainActivity, LoginActivity.class);
                startActivityForResult(intent,001);
            }else{
                showMenu(access_token);
            }
        }
    }

    private void toast(String str){
        Toast.makeText(mainActivity, str, Toast.LENGTH_SHORT).show();
    }

}
