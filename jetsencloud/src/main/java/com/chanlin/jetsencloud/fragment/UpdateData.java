package com.chanlin.jetsencloud.fragment;

import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.ResourceTree;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/11.
 * jetsenCloud
 * TODO:
 */

public interface UpdateData {
    void updataResourceTree(ArrayList<ResourceTree> resourceTrees);
    void updataQuestionPeriod(ArrayList<QuestionPeriod> questionPeriods);
}
