package com.ypz.supportquicknews.moduleFunction.humorousMoment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ypz.supportquicknews.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kingadmin on 2018/4/4.
 */

public class HumorousMomentFragment extends Fragment {

    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.humorous_moment_fragment,container,false);
        recyclerView = view.findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<HumorousItem> humorousItems = Arrays.asList(
                new HumorousItem("漫画","精美二次元的享受",getActivity().getDrawable(R.drawable.cmoic_ic)),
                new HumorousItem("微信精选","精选般的微信必备信息",getActivity().getDrawable(R.drawable.cmoic_ic)));
        HumorousAdapter humorousAdapter = new HumorousAdapter(humorousItems,getContext());
        recyclerView.setAdapter(humorousAdapter);
    }
}
