package com.example.jefflin.notipreference.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jefflin.notipreference.ActivityProfile;
import com.example.jefflin.notipreference.R;
import com.example.jefflin.notipreference.ViewModel.LogViewModel;
import com.example.jefflin.notipreference.adapter.LogAdapter;
import com.example.jefflin.notipreference.model.LogModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentLog extends Fragment {

    private FragmentActivity mContext;
    private LogAdapter logAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_log, container, false);

        ImageView iv_back = (ImageView) rootView.findViewById(R.id.back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityProfile) mContext).onBackPressed();
            }
        });

        logAdapter = new LogAdapter(getActivity(), new ArrayList<LogModel>());
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_log_event);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(logAdapter);

        final LogViewModel viewModel = new ViewModelProvider(requireActivity()).get(LogViewModel.class);
        viewModel.getLogList().observe(getViewLifecycleOwner(), new Observer<List<LogModel>>() {
            @Override
            public void onChanged(@Nullable List<LogModel> logModels) {
                logAdapter.addItems(logModels);
            }
        });

        Button delete = (Button) rootView.findViewById(R.id.log_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteItem();
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
//        ESMProperties survery_properties = (ESMProperties) getArguments().getSerializable("survery_properties");
//
//        assert survery_properties != null;
//        textView_end.setText(Html.fromHtml(survery_properties.getEndMessage()));
    }
}
