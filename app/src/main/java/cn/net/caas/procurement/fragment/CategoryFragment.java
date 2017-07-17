package cn.net.caas.procurement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.MainActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.adapter.CategoryListAdapter;
import cn.net.caas.procurement.constant.Constants;

/**
 * 分类
 * Created by wjj on 2017/3/6.
 */
public class CategoryFragment extends BaseFragment{
    private View view;
    private MainActivity mainActivity;

    private ListView lv_category;

    private List<String> list=null;
    private CategoryListAdapter adapter=null;

    private FragmentManager fragmentManager;

    private CategoryReagentFragment reagentFragment;
    private CategoryConsumablesFragment consumablesFragment;
    private CategoryInstrumentFragment instrumentFragment;
    private CategoryAgriculturalFragment agriculturalFragment;
    private CategoryTecServiceFragment tecServiceFragment;
    private CategoryOfficeFragment officeFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);

        mainActivity= (MainActivity) getActivity();
        fragmentManager = mainActivity.getSupportFragmentManager();

        initView();

        initData();

        setDefaultSelection();


        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                hideFragments(transaction);
                switch (i){
                    case 0:
                        Log.i("123","实验试剂");
                        if (reagentFragment==null){
                            reagentFragment= (CategoryReagentFragment) BaseFragment4Category.newInstance(Constants.CATEGORY_REAGENT);
                        }
                        if (!reagentFragment.isAdded()){
                            transaction.add(R.id.framelayout_category,reagentFragment);
                        } else{
                            transaction.show(reagentFragment);
                        }
                        break;
                    case 1:
                        Log.i("123","实验耗材");
                        if (consumablesFragment==null){
                            consumablesFragment= (CategoryConsumablesFragment) BaseFragment4Category.newInstance(Constants.CATEGORY_CONSUMABLES);
                        }
                        if (!consumablesFragment.isAdded()){
                            transaction.add(R.id.framelayout_category,consumablesFragment);
                        } else{
                            transaction.show(consumablesFragment);
                        }
                        break;
                    case 2:
                        Log.i("123","仪器设备");
                        if (instrumentFragment==null){
                            instrumentFragment= (CategoryInstrumentFragment) BaseFragment4Category.newInstance(Constants.CATEGORY_INSTRUMENT);
                        }
                        if (!instrumentFragment.isAdded()){
                            transaction.add(R.id.framelayout_category,instrumentFragment);
                        } else{
                            transaction.show(instrumentFragment);
                        }
                        break;
                    case 3:
                        Log.i("123","农资农机");
                        if (agriculturalFragment==null){
                            agriculturalFragment= (CategoryAgriculturalFragment) BaseFragment4Category.newInstance(Constants.CATEGORY_AGRICULTURAL);
                        }
                        if (!agriculturalFragment.isAdded()){
                            transaction.add(R.id.framelayout_category,agriculturalFragment);
                        } else{
                            transaction.show(agriculturalFragment);
                        }
                        break;
                    case 4:
                        Log.i("123","技术服务");
                        if (tecServiceFragment==null){
                            tecServiceFragment= (CategoryTecServiceFragment) BaseFragment4Category.newInstance(Constants.CATEGORY_TECSERVICE);
                        }
                        if (!tecServiceFragment.isAdded()){
                            transaction.add(R.id.framelayout_category,tecServiceFragment);
                        } else{
                            transaction.show(tecServiceFragment);
                        }
                        break;
                    case 5:
                        Log.i("123","办公用品");
                        if (officeFragment==null){
                            officeFragment= (CategoryOfficeFragment) BaseFragment4Category.newInstance(Constants.CATEGORY_OFFICE);
                        }
                        if (!officeFragment.isAdded()){
                            transaction.add(R.id.framelayout_category,officeFragment);
                        } else{
                            transaction.show(officeFragment);
                        }
                        break;
                    default:
                        break;
                }
                transaction.commit();
                adapter.setSelectItem(i);
            }
        });

        return view;
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (reagentFragment!=null){
            transaction.hide(reagentFragment);
        }
        if (consumablesFragment!=null){
            transaction.hide(consumablesFragment);
        }
        if (instrumentFragment!=null){
            transaction.hide(instrumentFragment);
        }
        if (agriculturalFragment!=null){
            transaction.hide(agriculturalFragment);
        }
        if (tecServiceFragment!=null){
            transaction.hide(tecServiceFragment);
        }
        if (officeFragment!=null){
            transaction.hide(officeFragment);
        }
    }

    private void setDefaultSelection() {
        Log.i("123","实验试剂");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (reagentFragment==null){
            reagentFragment= (CategoryReagentFragment) BaseFragment4Category.newInstance(Constants.CATEGORY_REAGENT);
        }
        if (!reagentFragment.isAdded()){
            transaction.add(R.id.framelayout_category,reagentFragment);
        } else{
            transaction.show(reagentFragment);
        }
        transaction.commit();
    }


    private void initView() {
        lv_category= (ListView) view.findViewById(R.id.lv_category);
    }

    private void initData() {
        list=new ArrayList<>();
        list.add("实验试剂");
        list.add("实验耗材");
        list.add("仪器设备");
        list.add("农资农机");
        list.add("技术服务");
        list.add("办公用品");
        adapter=new CategoryListAdapter(mainActivity,list);
        lv_category.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currentFragmentTag= Constants.FRAGMENT_FLAG_OTHER;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            Log.i("123","OtherFragment隐藏");
        }else{
            Log.i("123","OtherFragment显示");
        }
    }

}
