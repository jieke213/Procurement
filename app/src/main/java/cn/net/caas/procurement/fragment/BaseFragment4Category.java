package cn.net.caas.procurement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.net.caas.procurement.MainActivity;
import cn.net.caas.procurement.R;
import cn.net.caas.procurement.adapter.CategoryContentAdapter;
import cn.net.caas.procurement.constant.Constants;
import cn.net.caas.procurement.entity.MyCategory;

/**
 * Created by wjj on 2017/3/17.
 */
public class BaseFragment4Category extends Fragment {
    private View view;

    private MainActivity mainActivity;

    private static BaseFragment4Category instance=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base4category, null);

        mainActivity= (MainActivity) getActivity();

        return view;
    }

    public static BaseFragment4Category newInstance(String tag){
        switch (tag){
            case Constants.CATEGORY_REAGENT:
                instance=new CategoryReagentFragment();
                break;
            case Constants.CATEGORY_CONSUMABLES:
                instance=new CategoryConsumablesFragment();
                break;
            case Constants.CATEGORY_INSTRUMENT:
                instance=new CategoryInstrumentFragment();
                break;
            case Constants.CATEGORY_AGRICULTURAL:
                instance=new CategoryAgriculturalFragment();
                break;
            case Constants.CATEGORY_TECSERVICE:
                instance=new CategoryTecServiceFragment();
                break;
            case Constants.CATEGORY_OFFICE:
                instance=new CategoryOfficeFragment();
                break;
        }
        return instance;
    }

}
