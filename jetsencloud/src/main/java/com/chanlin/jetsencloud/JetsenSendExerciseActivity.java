package com.chanlin.jetsencloud;

import android.app.Activity;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chanlin.jetsencloud.adapter.BooklistViewAdapter;
import com.chanlin.jetsencloud.adapter.RecyclerViewAdapter;
import com.chanlin.jetsencloud.adapter.QuestionContentListViewAdapter;
import com.chanlin.jetsencloud.database.DatabaseService;
import com.chanlin.jetsencloud.entity.Book;
import com.chanlin.jetsencloud.entity.CourseStandardTree;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.QuestionPeriodDetail;
import com.chanlin.jetsencloud.expandable.ExpandView;
import com.chanlin.jetsencloud.expandable.ExpandablePresenter;
import com.chanlin.jetsencloud.expandable.FileAdapter;
import com.chanlin.jetsencloud.util.FileUtils;
import com.chanlin.jetsencloud.util.StringUtils;
import com.chanlin.jetsencloud.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */
public class JetsenSendExerciseActivity extends FragmentActivity implements ExpandView, View.OnClickListener {

    private TextView send_exercise;
    private RelativeLayout mRLBack;

    private Context mContext;
    private String courseId;


    private FrameLayout fl_no_data, frameLayout_content;
    ArrayList<Book> mybooks = null;//教材列表
    Book thisBook = null;//当前选中的教材项
    CourseStandardTree courseStandardTree = null;//当前选中的课标树
    ArrayList<CourseStandardTree> courseStandardTreeArrayList = new ArrayList<>();//课标树、数据源

    private RecyclerView fileRv;
    private FileAdapter adapter;
    private ExpandablePresenter presenter;

    //popupWindow弹出框
    private LinearLayout ll_sendexercise;
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
    private int number = 0;


    //定义发送消息的接口
    //课时列表
    private ArrayList<QuestionPeriod> questionPeriodList = new ArrayList<>();
    private ArrayList<QuestionPeriodDetail> questionContentList = new ArrayList<>();
    private ArrayList<QuestionPeriodDetail> addList = new ArrayList<>();//加载到选中列表中的对象
    private String addType;//选中题目的类型，一次只能选一种类型的题目
    private LinearLayout ll_question_view;//右侧习题显示的view
    private RecyclerView gv_question_period_list;//课时 列表
//    private QuestionPeriodGridViewAdapter gridViewAdapter;
    private RecyclerViewAdapter mAdapter;
    private ListView lv_question_detial_list;//问题列表
    private QuestionContentListViewAdapter listViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_jetsen_sendexercise);
        this.mContext = this;

        initView();
        initData();
        setGridView();
        initPop();
        initListener();
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
        send_exercise = (TextView) findViewById(R.id.sendexercise);
        send_exercise.setOnClickListener(this);

        img_booklist = (ImageView) findViewById(R.id.img_booklist);
        ll_sendexercise = (LinearLayout) findViewById(R.id.ll_sendexercise);


    }

    private void initListener() {
        fileRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                //清空选中的题目
                addList.clear();
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
                //resourceTreeList = DatabaseService.findResourceTreeList(entity.getId());
                questionPeriodList = DatabaseService.findQuestionPeriodList(entity.getId());

                number = 0;
                addType = "";
                courseStandardTree = entity;
                //刷新课时数据
                mAdapter.updateAdapter(questionPeriodList);
                if (questionPeriodList != null && questionPeriodList.size() > 0) {
                    ll_question_view.setVisibility(View.VISIBLE);
                    questionContentList = DatabaseService.findQuestionPeriodDetailListWhereUrlNotNull(questionPeriodList.get(0).getId());
                    listViewAdapter.updateList(questionContentList);
                }else{
                    ll_question_view.setVisibility(View.INVISIBLE);
                }
            }
        });

        mAdapter.setItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                QuestionPeriod questionPeriod = questionPeriodList.get(position);
                //数据库查询 已经下载了 的 问题详情
                questionContentList = DatabaseService.findQuestionPeriodDetailListWhereUrlNotNull(questionPeriod.getId());
                //清空选中 的题目
                addList.clear();
                number = 0;
                addType = "";
                mAdapter.updateAdapter(position, questionPeriodList);
                listViewAdapter.updateList(questionContentList);
            }
        });

        lv_question_detial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击某个listview的item时 选中或者取消选中
                QuestionPeriodDetail detail = questionContentList.get(position);
                //读取文件并解析
                if (detail.getUrl() != null){
                    String jsonStr = FileUtils.getJsonFile(detail.getUrl());
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        int thisQuestionType = json.getInt("type");
                        if (StringUtils.isEmpty(addType)){
                            addType = String.valueOf(thisQuestionType);
                            number++;
                            detail.setIschecked(true);
                            addList.add(detail);
                            listViewAdapter.notifyDataSetChanged();
                        }else if (addType.equals(String.valueOf(thisQuestionType))){
                            if (detail.ischecked()){
                                detail.setIschecked(false);
                                addList.remove(detail);
                                number--;
                                if (number == 0){
                                    addType = "";
                                }
                            }else {
                                detail.setIschecked(true);
                                addList.add(detail);
                                number++;
                            }
                            listViewAdapter.notifyDataSetChanged();
                            return;
                        }else {
                            Toast.makeText(mContext, "请选择相同类型的题目！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.shortToast(mContext,R.string.no_question_file);
                        return;
                    }


                }else {
                    ToastUtils.shortToast(mContext,R.string.no_question_file);
                }

            }
        });
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
            // courseStandardTreeArrayList = DatabaseService.findCourseStandardTreeList(thisBook.getId());

            //列表目录
            presenter.getFiles(-1, thisBook.getId(), 0);
        }

    }

    private void setGridView() {
        //右侧内容
        ll_question_view = (LinearLayout) findViewById(R.id.ll_question_view);
        gv_question_period_list = (RecyclerView) findViewById(R.id.gv_question_period_list);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gv_question_period_list.setLayoutManager(linearLayoutManager);
        lv_question_detial_list = (ListView) findViewById(R.id.lv_question_detial_list);
        mAdapter = new RecyclerViewAdapter(mContext, questionPeriodList);
//        gridViewAdapter = new QuestionPeriodGridViewAdapter(mContext, questionPeriodList);
        gv_question_period_list.setAdapter(mAdapter);

        //setListView();
        listViewAdapter = new QuestionContentListViewAdapter(mContext, questionContentList);
        listViewAdapter.setListView(lv_question_detial_list);
        lv_question_detial_list.setAdapter(listViewAdapter);
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
                course_id = mybooks.get(position).getCourse_id();
                book_id = mybooks.get(position).getId();
                book_name = mybooks.get(position).getName();
                text_book_name.setText(book_name);
                Log.i("onActivityResult", " course_id=" + course_id + " book_id=" + book_id + " book_name" + book_name);
                if (thisBook != mybooks.get(position)){//如果前面的book不是选中的则刷新列表
                    presenter.getFiles(-1,thisBook.getId(),0);
                }
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
//                ll_sendexercise.setBackgroundResource(R.color.white);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        popX = getScreenWidth(this) / 8;
    }

    @Override
    public void fillData(int position, List<CourseStandardTree> list) {
        adapter.setNewData(list);
        if (position > -1) {
            adapter.expand(position);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_back) {
            finish();
        }else if(id == R.id.sendexercise) {
            //判断是否选择了题目,没选择则提示选择
            if (addList == null || addList.size() < 1) {
                //没选中题目
                ToastUtils.shortToast(mContext, R.string.no_question_list);
//                finish();
            } else {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);//返回页面1
                Bundle bundle = intent.getExtras();
                bundle.putInt("course_standard_id", courseStandardTree.getId());
                bundle.putSerializable("questionList", addList);//添加要返回给页面1的数据
                intent.putExtras(bundle);
                finish();
            }

        }else if(id == R.id.tv_book_name){ // popupWindow弹框
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
//                    ll_sendexercise.setBackgroundResource(R.color.translucence_gray);
                    img_booklist.setImageResource(R.mipmap.img_booklist_bottom);
                    popupWindow.showAsDropDown(relative_booklist, popX, 5);
//                    popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
                    // 设置背景颜色变暗
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.7f;
                    getWindow().setAttributes(lp);
                }
        }
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

}
