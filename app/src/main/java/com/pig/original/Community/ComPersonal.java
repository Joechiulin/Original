package com.pig.original.Community;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pig.original.R;

public class ComPersonal extends Fragment {
    private View comPersonal_fragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View comPersonal_fragment =inflater.inflate( R.layout.compersonal,container,false );
        return comPersonal_fragment;

    }
}
