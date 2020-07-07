package com.sel.smartfood.ui.info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sel.smartfood.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private TextView switchInfoTv;
    private boolean isHistory = true;
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchInfoTv = view.findViewById(R.id.tv_switch_info);
        switchInfoTv.setOnClickListener(v -> {
            if (isHistory){
                switchInfoTv.setText("Xem chi tiết\nlịch sử");
                getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_container_info, new DetailInfoFragment())
                .addToBackStack(null)
                .commit();
            }
            else{
                switchInfoTv.setText("Xem chi tiết\nthông tin");
                getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_container_info, new HistoryFragment())
                .addToBackStack(null)
                .commit();
            }
            isHistory = !isHistory;
        });
    }
}
