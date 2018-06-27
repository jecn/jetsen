package com.chanlin.jetsencloud.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.adapter.QuestionAdapter;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.ResourceTree;
import com.chanlin.jetsencloud.util.Utils;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/11.
 * jetsenCloud
 * TODO:
 */

public class QuestionFragment extends Fragment implements UpdateData {
    private static final String TAG = "QuestionFragment";
    Context mContext;
    private ArrayList<QuestionPeriod> questionPeriodList = new ArrayList<>();
    ListView resourceListView;
    QuestionAdapter questionAdapter;
    public QuestionFragment(){
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
        View resourceView = inflater.inflate(R.layout.fragment_question,container,false);
        initView(resourceView);
        initListener();
        return resourceView;
    }
    private void initView(View view){
        resourceListView = (ListView) view.findViewById(R.id.lv_question);
        questionAdapter = new QuestionAdapter(mContext,questionPeriodList);
        questionAdapter.setListView(resourceListView);
        resourceListView.setAdapter(questionAdapter);
    }
    public void setActivityHandler(Handler mMainHandler){
        questionAdapter.setActivityHandler(mMainHandler);
    }
    private void initListener(){

    }
    @Override
    public void updataResourceTree(ArrayList<ResourceTree> resourceTrees) {

    }

    /**
     * 只实现更新问题列表的方法
     * @param questionPeriods
     */
    @Override
    public void updataQuestionPeriod(ArrayList<QuestionPeriod> questionPeriods) {
        questionAdapter.updateList(questionPeriods);
    }
}
