package com.ericyl.example.ui.fragment.home;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericyl.example.R;
import com.ericyl.example.databinding.FragmentBarcodeInHomeBinding;
import com.ericyl.example.event.JumpNavPageEvent;
import com.ericyl.example.model.ui.BarcodeInHomeModel;
import com.ericyl.example.util.AppProperties;
import com.ericyl.example.util.BusProvider;

import java.util.HashMap;
import java.util.Map;


public class BarcodeInHomeFragment extends Fragment implements View.OnClickListener {

    private static final Map<String, BarcodeInHomeModel> map = new HashMap<String, BarcodeInHomeModel>() {
        {
            put("build", new BarcodeInHomeModel(AppProperties.getContext(), R.string.build_barcode, R.string.build, R.color.white, R.color.blue));
            put("scan", new BarcodeInHomeModel(AppProperties.getContext(), R.string.distinguish_barcode, R.string.scan, R.color.white, R.color.orange));
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentBarcodeInHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcode_in_home, container, false);
        binding.setMap(map);
        binding.setClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        int tabIdRes;
        switch (view.getId()) {
            default:
            case R.id.layout_build:
                tabIdRes = R.id.tab_build_barcode;
                break;
            case R.id.layout_scan:
                tabIdRes = R.id.tab_scan_barcode;
                break;
        }
        BusProvider.getInstance().post(new JumpNavPageEvent(R.id.nav_barcode, tabIdRes));
    }
}
