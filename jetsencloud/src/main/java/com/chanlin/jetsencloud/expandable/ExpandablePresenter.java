package com.chanlin.jetsencloud.expandable;

import com.chanlin.jetsencloud.database.DatabaseService;
import com.chanlin.jetsencloud.entity.CourseStandardTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanLin on 2018/1/11.
 * jetsenCloud
 * TODO:
 */

public class ExpandablePresenter {
    private  ExpandView mView;
    private List<CourseStandardTree> entityList = new ArrayList<>();

    public ExpandablePresenter(ExpandView view){
        mView = view;
    }
    public void getFiles(final  int position, int bookId ,int parentId){
        updateFilesMultiEntity(position, bookId, parentId);
        mView.fillData(position, entityList);
    }
    private void updateFilesMultiEntity(int position, int bookId, int parentId) {
        ArrayList<CourseStandardTree> courseStandardTreeList = DatabaseService.findCourseStandardTreeList(bookId,parentId);
        for (CourseStandardTree courseStandardTree : courseStandardTreeList){
            if (courseStandardTree.getHasChild() ==0 ){//如果没有子目录
                courseStandardTree.itemType = FileAdapter.type_file;
            }else {
                courseStandardTree.itemType = FileAdapter.type_directory;
            }
            if (position < 0){
                courseStandardTree.level = 0;//一级目录
                if(!entityList.contains(courseStandardTree)){
                    entityList.add(courseStandardTree);
                }

            }else if(position < entityList.size()){
                courseStandardTree.level = entityList.get(position).level + 1;

                if(!entityList.contains(courseStandardTree)) {
                    entityList.get(position).addSubItem(courseStandardTree);
                }
            }
        }

    }
}
