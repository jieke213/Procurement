package cn.net.caas.procurement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.MainActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.adapter.CategoryContentAdapter;
import cn.net.caas.procurement.entity.MyCategory;

/**
 * 分类（办公用品）
 * Created by wjj on 2017/3/17.
 */
public class CategoryOfficeFragment extends BaseFragment4Category {
    private View view;
    private MainActivity mainActivity;

    private GridView gv_category_office;
    private CategoryContentAdapter adapter;

    private List<MyCategory> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_category_office,null);

        mainActivity= (MainActivity) getActivity();

        initView();

        initData();

        gv_category_office.setAdapter(adapter);

        return view;
    }

    private void initView() {
        gv_category_office= (GridView) view.findViewById(R.id.gv_category_office);
    }

    private void initData() {
        list=new ArrayList<>();
        list.add(new MyCategory("DNA测序仪",R.mipmap.category1));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category2));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category3));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category4));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category1));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category2));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category3));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category4));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category1));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category2));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category3));
        list.add(new MyCategory("DNA测序仪",R.mipmap.category4));
        adapter=new CategoryContentAdapter(mainActivity,list);
    }
}
