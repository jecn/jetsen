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
import android.view.KeyEvent;
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
import com.chanlin.jetsencloud.util.CacheActivity;
import com.chanlin.jetsencloud.util.Constant;
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
public class JetsenSendExerciseActivity extends Activity implements ExpandView, View.OnClickListener {

    private TextView send_exercise;
    private RelativeLayout mRLBack;

    // 题目类型（选择、判断、填空、主观）
    private RelativeLayout rl_types1, rl_types2, rl_types3, rl_types4;
    private TextView tv_types1, tv_types2, tv_types3, tv_types4 , no_downlaod_questions;
    private ImageView view_types1, view_types2, view_types3, view_types4;
    private ArrayList<QuestionPeriodDetail> questionContentList2 = new ArrayList<>();

    private Context mContext;
    private String courseId;


    private FrameLayout fl_no_data, frameLayout_content;
    ArrayList<Book> mybooks = new ArrayList<>();//教材列表
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
        CacheActivity.addActivity(this);

        initView();
        initPop();
        initData();
        setGridView();
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

        no_downlaod_questions = (TextView) findViewById(R.id.no_downlaod_questions);
        // 题目类型（选择、判断、填空、主观）
        rl_types1 = (RelativeLayout) findViewById(R.id.rl_types1);
        rl_types2 = (RelativeLayout) findViewById(R.id.rl_types2);
        rl_types3 = (RelativeLayout) findViewById(R.id.rl_types3);
        rl_types4 = (RelativeLayout) findViewById(R.id.rl_types4);
        rl_types1.setOnClickListener(this);
        rl_types2.setOnClickListener(this);
        rl_types3.setOnClickListener(this);
        rl_types4.setOnClickListener(this);

        tv_types1 = (TextView) findViewById(R.id.tv_types1);
        tv_types2 = (TextView) findViewById(R.id.tv_types2);
        tv_types3 = (TextView) findViewById(R.id.tv_types3);
        tv_types4 = (TextView) findViewById(R.id.tv_types4);
        view_types1 = (ImageView) findViewById(R.id.view_types1);
        view_types2 = (ImageView) findViewById(R.id.view_types2);
        view_types3 = (ImageView) findViewById(R.id.view_types3);
        view_types4 = (ImageView) findViewById(R.id.view_types4);
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
                //resourceTreeList = DatabaseService.findResourceTreeList(entity.getId());
                questionPeriodList = DatabaseService.findQuestionPeriodList(entity.getId());

                addList.clear(); //清空选中的题目
                number = 0;
                courseStandardTree = entity;
                //刷新课时数据
                mAdapter.updateAdapter(questionPeriodList);
                if (questionPeriodList != null && questionPeriodList.size() > 0) {
                    no_downlaod_questions.setVisibility(View.GONE);
                    ll_question_view.setVisibility(View.VISIBLE);
                    questionContentList = DatabaseService.findQuestionPeriodDetailListWhereUrlNotNull(questionPeriodList.get(0).getId());
                    int type = getType(questionContentList); // 获取默认初始题型
                    setTypeBackColor(type); // 设置题型背景色
                    getQuestionListTypeDate(type, questionContentList); // 过滤题目列表
                    listViewAdapter.updateList(questionContentList2);
                } else {
                    ll_question_view.setVisibility(View.GONE);
                    no_downlaod_questions.setVisibility(View.VISIBLE);
                }
            }
        });

        mAdapter.setItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                QuestionPeriod questionPeriod = questionPeriodList.get(position);
                //数据库查询 已经下载了 的 问题详情
                questionContentList = DatabaseService.findQuestionPeriodDetailListWhereUrlNotNull(questionPeriod.getId());
                addList.clear(); //清空选中的题目
                number = 0;
                mAdapter.updateAdapter(position, questionPeriodList);
                int type = getType(questionContentList);
                setTypeBackColor(type); // 设置题型背景色
                getQuestionListTypeDate(type, questionContentList); // 过滤题目列表
                listViewAdapter.updateList(questionContentList2);
            }
        });

        lv_question_detial_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击某个listview的item时 选中或者取消选中
                QuestionPeriodDetail detail = questionContentList2.get(position);
                //读取文件并解析
                if (detail.getUrl() != null) {
//                    String jsonStr = FileUtils.getJsonFile(detail.getUrl());
//                    try {
//                        JSONObject json = new JSONObject(jsonStr);
//                        int thisQuestionType = json.getInt("type");
//                        if (StringUtils.isEmpty(addType)){
//                            addType = String.valueOf(thisQuestionType);
//                            number++;
//                            detail.setIschecked(true);
//                            addList.add(detail);
//                            listViewAdapter.notifyDataSetChanged();
//                        }else if (addType.equals(String.valueOf(thisQuestionType))){
//                            if (detail.ischecked()){
//                                detail.setIschecked(false);
//                                addList.remove(detail);
//                                number--;
//                                if (number == 0){
//                                    addType = "";
//                                }
//                            }else {
//                                detail.setIschecked(true);
//                                addList.add(detail);
//                                number++;
//                            }
//                            listViewAdapter.notifyDataSetChanged();
//                            return;
//                        }else {
//                            Toast.makeText(mContext, "请选择相同类型的题目！", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        ToastUtils.shortToast(mContext,R.string.no_question_file);
//                        return;
//                    }

                    if (detail.ischecked()) {
                        detail.setIschecked(false);
                        addList.remove(detail);
                        number--;
                    } else if (number < 10){
                        detail.setIschecked(true);
                        addList.add(detail);
                        number++;
                    } else {
                        ToastUtils.shortToast(mContext, "题目数量超过限制！");
                    }
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast(mContext, R.string.no_question_file);
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
            updatePopUI(0);
            booklistViewAdapter.updateBookList(mybooks);
            presenter.getFiles(-1, thisBook.getId(), 0);
        }
    }

    private void setGridView() {
        //右侧内容
        ll_question_view = (LinearLayout) findViewById(R.id.ll_question_view);
        ll_question_view.setVisibility(View.GONE);
        no_downlaod_questions.setVisibility(View.VISIBLE);
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
//                ll_sendexercise.setBackgroundResource(R.color.white);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        popX = getScreenWidth(this) / 8;
    }

    private void updatePopUI(int position) {
        course_id = mybooks.get(position).getCourse_id();
        book_id = mybooks.get(position).getId();
        book_name = mybooks.get(position).getName();
        text_book_name.setText(book_name);
        Log.i("onActivityResult", " course_id=" + course_id + " book_id=" + book_id + " book_name" + book_name);
        if (thisBook != mybooks.get(position)) {//如果前面的book不是选中的则刷新列表
            thisBook = mybooks.get(position);
            presenter.getFiles(-1, thisBook.getId(), 0);
        }
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
        if (id == R.id.title_back) { // 返回
            finish();
        } else if (id == R.id.sendexercise) { // 发送
            //判断是否选择了题目,没选择则提示选择
            if (addList == null || addList.size() < 1) {
                //没选中题目
                ToastUtils.shortToast(mContext, R.string.no_question_list);
//                finish();
            } else {
//                Intent intent = getIntent();
//                setResult(Activity.RESULT_OK, intent);//返回页面1
//                Bundle bundle = intent.getExtras();
//                bundle.putInt("course_standard_id", courseStandardTree.getId());
//                bundle.putSerializable("questionList", addList);//添加要返回给页面1的数据
//                intent.putExtras(bundle);
//                finish();
                // 发送习题广播
                Intent intent = new Intent(Constant.SEND_EXERCISES);
                Bundle bundle = new Bundle();
                bundle.putInt("course_standard_id", courseStandardTree.getId());
                bundle.putSerializable("questionList", addList);//添加要返回给页面1的数据
                intent.putExtras(bundle);

                sendBroadcast(intent);
                moveTaskToBack(true);

                //finish();
            }

        } else if (id == R.id.tv_book_name) { // popupWindow弹框
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
        } else if (id == R.id.rl_types1) { // 选择题
            addList.clear(); //清空选中的题目
            number = 0;
            for (int i = 0; i < questionContentList2.size(); i++) {
                questionContentList2.get(i).setIschecked(false);
            }
            setTypeBackColor(1); // 设置题型背景色
            getQuestionListTypeDate(1, questionContentList); // 过滤题目列表
            listViewAdapter.updateList(questionContentList2);
        } else if (id == R.id.rl_types2) { // 判断题
            addList.clear(); //清空选中的题目
            number = 0;
            for (int i = 0; i < questionContentList2.size(); i++) {
                questionContentList2.get(i).setIschecked(false);
            }
            setTypeBackColor(2);
            getQuestionListTypeDate(2, questionContentList); // 过滤题目列表
            listViewAdapter.updateList(questionContentList2);
        } else if (id == R.id.rl_types3) { // 填空题
            addList.clear(); //清空选中的题目
            number = 0;
            for (int i = 0; i < questionContentList2.size(); i++) {
                questionContentList2.get(i).setIschecked(false);
            }
            setTypeBackColor(3);
            getQuestionListTypeDate(3, questionContentList); // 过滤题目列表
            listViewAdapter.updateList(questionContentList2);
        } else if (id == R.id.rl_types4) { // 主观题
            addList.clear(); //清空选中的题目
            number = 0;
            for (int i = 0; i < questionContentList2.size(); i++) {
                questionContentList2.get(i).setIschecked(false);
            }
            setTypeBackColor(4);
            getQuestionListTypeDate(4, questionContentList); // 过滤题目列表
            listViewAdapter.updateList(questionContentList2);
        }
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    // 题目初始选中类型
    private int getType(ArrayList<QuestionPeriodDetail> questionContentList) {
        int mintype = 5;
        for (int i = 0; i < questionContentList.size(); i++) {
            QuestionPeriodDetail detail = questionContentList.get(i);
            if (detail.getUrl() != null) {
                String jsonStr = FileUtils.getJsonFile(detail.getUrl());
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    int type = json.getInt("type");
                    if (mintype > type) {
                        mintype = type;
                    }
                } catch (JSONException e) {

                }
            }
        }
        return mintype;
    }

    // 过滤题目列表
    private void getQuestionListTypeDate(int type, ArrayList<QuestionPeriodDetail> questionContentList) {
        questionContentList2.clear();
        for (int i = 0; i < questionContentList.size(); i++) {
            QuestionPeriodDetail detail = questionContentList.get(i);
            if (detail.getUrl() != null) {
                String jsonStr = FileUtils.getJsonFile(detail.getUrl());
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    int types = json.getInt("type");
                    if (type == types || (type == 4 && types == 5) || (type == 5 && types == 4)) {
                        questionContentList2.add(questionContentList.get(i));
                    }
                } catch (JSONException e) {

                }
            }
        }
    }

    // 设置题型背景色
    private void setTypeBackColor(int i) {
        if (i == 1) {
            tv_types1.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
            tv_types2.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types3.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types4.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            view_types1.setVisibility(View.VISIBLE);
            view_types2.setVisibility(View.INVISIBLE);
            view_types3.setVisibility(View.INVISIBLE);
            view_types4.setVisibility(View.INVISIBLE);
        }
        if (i == 2) {
            tv_types1.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types2.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
            tv_types3.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types4.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            view_types1.setVisibility(View.INVISIBLE);
            view_types2.setVisibility(View.VISIBLE);
            view_types3.setVisibility(View.INVISIBLE);
            view_types4.setVisibility(View.INVISIBLE);
        }
        if (i == 3) {
            tv_types1.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types2.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types3.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
            tv_types4.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            view_types1.setVisibility(View.INVISIBLE);
            view_types2.setVisibility(View.INVISIBLE);
            view_types3.setVisibility(View.VISIBLE);
            view_types4.setVisibility(View.INVISIBLE);
        }
        if (i == 4) {
            tv_types1.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types2.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types3.setTextColor(mContext.getResources().getColor(R.color.motion_detail_position_text));
            tv_types4.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
            view_types1.setVisibility(View.INVISIBLE);
            view_types2.setVisibility(View.INVISIBLE);
            view_types3.setVisibility(View.INVISIBLE);
            view_types4.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivity.removeActivity(this);
    }
}
