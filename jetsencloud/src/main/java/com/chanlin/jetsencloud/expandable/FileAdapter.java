package com.chanlin.jetsencloud.expandable;

import android.support.annotation.IntRange;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.entity.CourseStandardTree;
import com.chanlin.jetsencloud.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanLin on 2018/1/11.
 * jetsenCloud
 * TODO:
 */

public class FileAdapter extends BaseMultiItemQuickAdapter<CourseStandardTree, BaseViewHolder> {
    public static final int type_file = 0;
    public static final int type_directory = 1;
    public FileAdapter(){
        this(new ArrayList<CourseStandardTree>());
    }
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FileAdapter(List<CourseStandardTree> data) {
        super(data);
        addItemType(type_file , R.layout.item_file);
        addItemType(type_directory, R.layout.item_directory);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseStandardTree item) {
        switch (helper.getItemViewType()){
            case type_file:
                helper.setImageResource(R.id.type_icon,R.mipmap.file).setText(R.id.date,item.getDescription());
                break;
            case type_directory:
                if (item.isExpand){
                    helper.setImageResource(R.id.arrow, R.mipmap.arrow_down);
                }else{
                    helper.setImageResource(R.id.arrow,R.mipmap.arrow_right);
                }
                helper.setImageResource(R.id.type_icon,R.mipmap.directory);
                break;
        }
        View typeIcon = helper.getView(R.id.type_icon);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) typeIcon.getLayoutParams();

        params.setMargins(ConvertUtils.dp2px(8 * item.getLevel()), 0, 0, 0);
        typeIcon.setLayoutParams(params);
        helper.setText(R.id.name, item.getDescription());
    }

    @Override
    public int expand(@IntRange(from = 0L) int position) {
        mData.get(position).isExpand = true;
        mData.get(position).hasGet = true;
        return super.expand(position);
    }

    @Override
    public int collapse(@IntRange(from = 0L) int position) {
        mData.get(position).isExpand = false;
        return super.collapse(position);
    }
}
