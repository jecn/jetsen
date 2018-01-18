package com.chanlin.jetsencloud.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public class CourseStandardTree extends AbstractExpandableItem<CourseStandardTree> implements MultiItemEntity{

    public int level = 0;
    public int itemType = 0;

    private int book_id;
    private int id;
    private String description;
    private int parentId;//文件目录的父目录， 如果是根目录，则父目录为0
    private int hasChild;//是否有子目录，0 没有，1有
    public boolean isExpand = false;//是否是展开状态
    public boolean hasGet = false;//是否拿过数据库，如果取拿过则不再拿

    public int getHasChild() {
        return hasChild;
    }

    public void setHasChild(int hasChild) {
        this.hasChild = hasChild;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    //    private String child;

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
/*

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }
*/

    @Override
    public String toString() {
        return "CourseStandardTree{" +
                "book_id=" + book_id +
                ", id=" + id +
                ", description='" + description + '\'' +

                '}';
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public int hashCode() {
        final int prime = 2;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((description == null)?0:description.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
       /* if (getClass() != obj.getClass()) {
            return false;
        }*/
        CourseStandardTree tree = (CourseStandardTree) obj;
        if (id != tree.id) {
            return false;
        }
        if (description == null){
            if (tree.description != description)
                return false;
        }else if(!description.equals(tree.description)){
            return false;
        }
        return true;
    }
}
