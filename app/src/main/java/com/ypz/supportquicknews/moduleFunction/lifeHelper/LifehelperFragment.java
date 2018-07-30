package com.ypz.supportquicknews.moduleFunction.lifeHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ypz.supportquicknews.R;
import com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.showJoke.Main3Activity;
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.findRecipes.FindRecipesActivity;

/**
 * Created by kingadmin on 2018/3/6.
 */

public class LifehelperFragment extends Fragment {

    private CardView cooking ,powerTicket;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lifehelper_fragment,container,false);
        cooking = view.findViewById(R.id.cooking);
        powerTicket = view.findViewById(R.id.powticket);
        cooking.setOnClickListener(view1 -> getActivity().startActivity(new Intent(getContext(), FindRecipesActivity.class)));
        powerTicket.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), Main3Activity.class);
            intent.putExtra("url","http://v.juhe.cn/wepiao/go?key=1849534442884f55199fb8771e22a852&s=weixin");
            intent.putExtra("title","在线电影票");
            getActivity().startActivity(intent);
        });
        return view;
    }


}
