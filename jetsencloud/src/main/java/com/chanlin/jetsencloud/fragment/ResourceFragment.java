package com.chanlin.jetsencloud.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.adapter.ResourceAdapter;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.ResourceTree;
import com.chanlin.jetsencloud.util.Utils;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/11.
 * jetsenCloud
 * TODO:
 */

public class ResourceFragment extends Fragment implements UpdateData {
    private static final String TAG = "ResourceFragment";
    Context mContext;
    private ArrayList<ResourceTree> resourceTreeList = new ArrayList<>();
    ListView resourceListView;
    ResourceAdapter resourceAdapter;
    public ResourceFragment(){
        mContext = Utils.getContext();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View resourceView = inflater.inflate(R.layout.fragment_resource,container,false);
        initView(resourceView);
        initListener();
        return resourceView;
    }

    private void initView(View view){
        resourceListView = (ListView) view.findViewById(R.id.lv_resource);
        resourceAdapter = new ResourceAdapter(mContext,resourceTreeList);
        resourceListView.setAdapter(resourceAdapter);
    }
    private void initListener(){

    }

    /**
     * 只实现resource更新方法
     * @param resourceTrees
     */
    @Override
    public void updataResourceTree(ArrayList<ResourceTree> resourceTrees) {
        resourceAdapter.updateList(resourceTrees);
    }

    @Override
    public void updataQuestionPeriod(ArrayList<QuestionPeriod> questionPeriods) {

    }
}
