package com.chanlin.jetsencloud;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanlin.jetsencloud.adapter.BooklistViewAdapter;
import com.chanlin.jetsencloud.adapter.PrepareResourceAdapter;
import com.chanlin.jetsencloud.database.DatabaseService;
import com.chanlin.jetsencloud.entity.Book;
import com.chanlin.jetsencloud.entity.CourseStandardTree;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.ResourceTree;
import com.chanlin.jetsencloud.expandable.ExpandView;
import com.chanlin.jetsencloud.expandable.ExpandablePresenter;
import com.chanlin.jetsencloud.expandable.FileAdapter;
import com.chanlin.jetsencloud.util.CacheActivity;
import com.chanlin.jetsencloud.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/15.
 */
public class JetsenPrepareResourceActivity extends FragmentActivity implements ExpandView, View.OnClickListener {

    private RelativeLayout mRLBack;

    private Context mContext;
    private String courseId;

    private ListView resourse_listview;

    private FrameLayout fl_no_data, frameLayout_content;
    ArrayList<Book> mybooks =  new ArrayList<>();//教材列表
    Book thisBook = null;//当前选中的教材项

    private RecyclerView fileRv;
    private FileAdapter adapter;
    private ExpandablePresenter presenter;

    //popupWindow弹出框
    private LinearLayout ll_preparesource;
    private RelativeLayout relative_booklist;
    private TextView text_book_name;
    private ImageView img_booklist;
    private ListView mlistview;
    private View view;
    private BooklistViewAdapter booklistViewAdapter;
    private PopupWindow popupWindow;
    private int popX; // 横坐标

    private int course_id;
    private int book_id;
    private String book_name;

    PrepareResourceAdapter prepareResourceAdapter;

    //定义发送消息的接口
    private ArrayList<ResourceTree> resourceTreeList = new ArrayList<>();
    private ArrayList<QuestionPeriod> questionPeriodList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_jetsen_prepareresource);
        mContext = this;
        CacheActivity.addActivity(this);

        initView();
        initPop();
        initData();
        setlistview();
    }

    private void initView() {
        mRLBack = (RelativeLayout) findViewById(R.id.title_back);
        mRLBack.setOnClickListener(this);

        fl_no_data = (FrameLayout) findViewById(R.id.fl_no_data);
        frameLayout_content = (FrameLayout) findViewById(R.id.frameLayout_content);

        fileRv = (RecyclerView) findViewById(R.id.fileRv);
        fileRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FileAdapter();
        presenter = new ExpandablePresenter(this);
        fileRv.setAdapter(adapter);

        relative_booklist = (RelativeLayout) findViewById(R.id.relative_booklist);
        text_book_name = (TextView) findViewById(R.id.tv_book_name);
        text_book_name.setOnClickListener(this);

        img_booklist = (ImageView) findViewById(R.id.img_booklist);
        ll_preparesource = (LinearLayout) findViewById(R.id.ll_preparesource);

        initListener();
    }

    private void initData() {
        Intent it = getIntent();
        courseId = it.getStringExtra("courceId");
        courseId = courseId == null ? "0" : courseId;
        //数据库中获取数据
        mybooks = DatabaseService.findBookList(Integer.parseInt(courseId));
        if (mybooks != null && mybooks.size() > 0) {
            fl_no_data.setVisibility(View.GONE);
            frameLayout_content.setVisibility(View.VISIBLE);
            thisBook = mybooks.get(0);
            //courseStandardTreeArrayList = DatabaseService.findCourseStandardTreeList(thisBook.getId());

            //列表目录
            updatePopUI(0);
            booklistViewAdapter.updateBookList(mybooks);
            presenter.getFiles(-1, thisBook.getId(), 0);
        }
    }

    private void setlistview() {
        //右侧内容
        resourse_listview = (ListView) findViewById(R.id.resource_listview);
        prepareResourceAdapter = new PrepareResourceAdapter(mContext, resourceTreeList);
        resourse_listview.setAdapter(prepareResourceAdapter);
        resourse_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                Bundle mBundle = new Bundle();
//                mBundle.putSerializable("resourceTree", resourceTreeList.get(position));
//                intent.putExtras(mBundle);
//                finish();
                // 发送习题广播
                Intent intent = new Intent(Constant.SEND_PREPARE_RESOURCE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("resourceTree", resourceTreeList.get(position));
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        });
    }

    private void initPop() {
        view = LayoutInflater.from(this).inflate(R.layout.booklistview, null);
        mlistview = (ListView) view.findViewById(R.id.book_listview);
        booklistViewAdapter = new BooklistViewAdapter(mContext, mybooks);
        mlistview.setAdapter(booklistViewAdapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                updatePopUI(position);
            }
        });
        popupWindow = new PopupWindow(view, getScreenWidth(this) / 4, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 解决PopupWindow 设置setOutsideTouchable无效
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                img_booklist.setImageResource(R.mipmap.img_booklist_right);
//                ll_preparesource.setBackgroundResource(R.color.white);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        popX = getScreenWidth(this) / 8;
    }
    private void updatePopUI(int position){
        course_id = mybooks.get(position).getCourse_id();
        book_id = mybooks.get(position).getId();
        book_name = mybooks.get(position).getName();
        text_book_name.setText(book_name);
        Log.i("onActivityResult", " course_id=" + course_id + " book_id=" + book_id + " book_name" + book_name);
        if (thisBook != mybooks.get(position)){//如果前面的book不是选中的则刷新列表
            thisBook = mybooks.get(position);
            presenter.getFiles(-1,thisBook.getId(),0);
        }
    }
    private void initListener() {
        fileRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseStandardTree entity = (CourseStandardTree) adapter.getItem(position);
                if (entity.isExpand) {
                    adapter.collapse(position);
                } else {
                    if (!entity.hasGet) {//是否请求过，请求过的就放缓存里了，不需要重复去拿
                        presenter.getFiles(position, thisBook.getId(), entity.getId());
                    } else {
                        adapter.expand(position);
                    }
                }
                //这里就去访问数据库资源和题库里的内容，如果没有则访问服务器的
                //拿到数据后传递到Fragment里面去
                resourceTreeList = DatabaseService.findResourceTreeList(entity.getId());
                questionPeriodList = DatabaseService.findQuestionPeriodList(entity.getId());
                //发送消息给fragment更新数据
//                resourceFragment.updataResourceTree(resourceTreeList);
//                questionFragment.updataQuestionPeriod(questionPeriodList);
                //网络获取
//                courseStandardTree = entity;
//                resourceController.getResourceList(entity.getId());
//                questionController.getQuestionPeriodList(entity.getId());

                //刷新 listview
                prepareResourceAdapter.updateList(resourceTreeList);
            }
        });
    }

    @Override
    public void fillData(int position, List<CourseStandardTree> list) {
        adapter.setNewData(list);
        if (position > -1) {
            adapter.expand(position);
        }
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_back){
            finish();
        }else if(id == R.id.tv_book_name){ // popupWindow弹框
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
//                    ll_preparesource.setBackgroundResource(R.color.translucence_gray);
                    img_booklist.setImageResource(R.mipmap.img_booklist_bottom);
                    popupWindow.showAsDropDown(relative_booklist, popX, 5);
//                    popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                }

        }
    }

     class ExerciseListViewAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<QuestionPeriod> questionPeriodList1 = new ArrayList<QuestionPeriod>();
        private ArrayList<String> list = new ArrayList<String>();
        private LayoutInflater layoutInflater;

        public ExerciseListViewAdapter(Context context, ArrayList<QuestionPeriod> questionPeriodList, ArrayList<String> list) {
            this.mContext = context;
            this.questionPeriodList1 = questionPeriodList;
            this.list = list;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return questionPeriodList.size();
        }

        @Override
        public Object getItem(int position) {
            return questionPeriodList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.exercise_list_item, null);
                holder.period_name = (TextView) convertView.findViewById(R.id.period_name);
                holder.imgcheck = (ImageView) convertView.findViewById(R.id.imgbtn_check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            if (list.get(position).equals("1")){
//                holder.imgcheck.setImageResource(R.mipmap.period_check_on);
//            } else {
//                holder.imgcheck.setImageResource(R.mipmap.period_check_off);
//            }
            holder.period_name.setText(questionPeriodList.get(position).getTitle());

            return convertView;
        }

        private class ViewHolder {
            private TextView period_name;
            private ImageView imgcheck;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivity.removeActivity(this);
    }
}
