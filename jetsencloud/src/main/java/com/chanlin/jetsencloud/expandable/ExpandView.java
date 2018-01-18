package com.chanlin.jetsencloud.expandable;

import com.chanlin.jetsencloud.entity.CourseStandardTree;

import java.util.List;

/**
 * Created by ChanLin on 2018/1/11.
 * jetsenCloud
 * TODO:
 */

public interface ExpandView {
    void fillData(int position, List<CourseStandardTree> list);
}
