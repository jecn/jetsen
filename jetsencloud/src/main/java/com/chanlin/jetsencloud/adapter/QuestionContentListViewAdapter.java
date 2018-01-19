package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.entity.QuestionContent;
import com.chanlin.jetsencloud.entity.QuestionPeriodDetail;
import com.chanlin.jetsencloud.util.FileUtils;
import com.chanlin.jetsencloud.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ChanLin on 2018/1/15.
 */
public class QuestionContentListViewAdapter extends BaseAdapter {
    private static final String TAG = "QuestionContentListViewAdapter";
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<QuestionPeriodDetail> list = new ArrayList<>();

    public QuestionContentListViewAdapter(Context context, ArrayList<QuestionPeriodDetail> details){
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.list = details;
    }
    public void updateList(ArrayList<QuestionPeriodDetail> details) {
        this.list = details;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //这个view 就是习题详情展示的UI
        View view = convertView;
        ViewHodler hodler = null;
        if (convertView == null){
            hodler = new ViewHodler();
            view = layoutInflater.inflate(R.layout.item_question_content,parent,false);
            hodler.tv_file_title = (TextView) view.findViewById(R.id.tv_file_title);
            hodler.iv_file_title = (ImageView) view.findViewById(R.id.iv_file_title);
            hodler.tv_pid_title = (TextView) view.findViewById(R.id.tv_pid_title);
            hodler.iv_pid_title = (ImageView) view.findViewById(R.id.iv_pid_title);
            hodler.tv_parse = (TextView) view.findViewById(R.id.tv_parse);
            hodler.iv_parse = (ImageView) view.findViewById(R.id.iv_parse);
            hodler.tv_parse_title = (TextView) view.findViewById(R.id.tv_parse_title);
            hodler.iv_parse_title = (ImageView) view.findViewById(R.id.iv_parse_title);
            //hodler.file_title
            hodler.down = (ImageView) view.findViewById(R.id.type_icon);
            view.setTag(hodler);
        }else{
            hodler = (ViewHodler) convertView.getTag();
        }
        QuestionPeriodDetail detail = list.get(position);
        //解析文件json
        String jstr = FileUtils.getJsonFile(detail.getUrl());
        if (StringUtils.isEmpty(jstr)){
            hodler.tv_file_title.setText("题目已删除！");
            hodler.tv_file_title.setVisibility(View.VISIBLE);
            hodler.iv_file_title.setVisibility(View.INVISIBLE);
        }else {
            try {
                Log.e("course_standard_id", jstr + "");
                JSONObject jsonObject = new JSONObject(jstr);
                QuestionContent content = pullJson(jsonObject);
                if (StringUtils.isEmpty(content.getTitle_key())){ // title_key  title
                    if (StringUtils.isEmpty(content.getTitle())){
                        hodler.tv_file_title.setText("题目已删除！");
                        hodler.tv_file_title.setVisibility(View.INVISIBLE);
                    } else {
                        String text2 = "<span style=\"font-weight:600; color:rgb(74,74,74); fontsize:20px;\">数学伴我们成长</span><p></p>";
                        String text = content.getTitle();
                        hodler.tv_file_title.setText(Html.fromHtml(text));
                    }
                    hodler.iv_file_title.setVisibility(View.INVISIBLE);
                }else if (content.getTitle_key().startsWith("<img ") ){//图片类型
                    hodler.tv_file_title.setVisibility(View.INVISIBLE);
                    hodler.iv_file_title.setVisibility(View.VISIBLE);
                    //base64转bitmap
                    String regEx = "<img src=\"data:image/png;base64,(.*?)\"/>";
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(content.getTitle_key());
                    if (matcher.find()){
                        String c = matcher.group(1);
                        hodler.iv_file_title.setImageBitmap(FileUtils.stringtoBitmap(c));
                    }
                }else { // 文本或html类型
                    hodler.iv_file_title.setVisibility(View.INVISIBLE);
                    hodler.tv_file_title.setText(Html.fromHtml(content.getTitle_key()));
                }

                if (StringUtils.isEmpty(content.getPid_title_key())){ // pid_title  pid_title_key
                    if (StringUtils.isEmpty(content.getPid_title())){
                        hodler.tv_pid_title.setText("题目已删除！");
                        hodler.tv_pid_title.setVisibility(View.INVISIBLE);
                    } else {
                        hodler.tv_pid_title.setText(content.getPid_title());
                    }
                    hodler.iv_pid_title.setVisibility(View.INVISIBLE);
                } else if (content.getPid_title_key().startsWith("<img")){
                    hodler.tv_pid_title.setVisibility(View.INVISIBLE);
                    hodler.iv_pid_title.setVisibility(View.VISIBLE);
                    //base64转bitmap
                    String regEx = "<img src=\"data:image/png;base64,(.*?)\"/>";
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(content.getPid_title_key());
                    if (matcher.find()){
                        String c = matcher.group(1);
                        hodler.iv_pid_title.setImageBitmap(FileUtils.stringtoBitmap(c));
                    }
                } else {
                    hodler.iv_pid_title.setVisibility(View.INVISIBLE);
                    hodler.tv_pid_title.setText(Html.fromHtml(content.getPid_title_key()));
                }

                if (StringUtils.isEmpty(content.getParse())){ // parse
                    hodler.tv_parse.setText("题目已删除！");
                    hodler.tv_parse.setVisibility(View.INVISIBLE);
                    hodler.iv_parse.setVisibility(View.INVISIBLE);
                } else if (content.getParse().startsWith("<img")){
                    hodler.tv_parse.setVisibility(View.INVISIBLE);
                    hodler.iv_parse.setVisibility(View.VISIBLE);
                    //base64转bitmap
                    String regEx = "<img src=\"data:image/png;base64,(.*?)\"/>";
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(content.getParse());
                    if (matcher.find()){
                        String c = matcher.group(1);
                        hodler.iv_parse.setImageBitmap(FileUtils.stringtoBitmap(c));
                    }
                } else {
                    hodler.iv_parse.setVisibility(View.INVISIBLE);
                    hodler.tv_pid_title.setText(Html.fromHtml(content.getParse()));
                }

                if (StringUtils.isEmpty(content.getParse_key())){ // parse_key
                    hodler.tv_parse_title.setText("题目已删除！");
                    hodler.tv_parse_title.setVisibility(View.INVISIBLE);
                    hodler.iv_parse_title.setVisibility(View.INVISIBLE);
                } else if (content.getParse_key().startsWith("<img")){
                    hodler.tv_parse_title.setVisibility(View.INVISIBLE);
                    hodler.iv_parse_title.setVisibility(View.VISIBLE);
                    //base64转bitmap
                    String regEx = "<img src=\"data:image/png;base64,(.*?)\"/>";
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(content.getParse_key());
                    if (matcher.find()){
                        String c = matcher.group(1);
                        hodler.iv_parse_title.setImageBitmap(FileUtils.stringtoBitmap(c));
                    }
                } else {
                    hodler.iv_parse_title.setVisibility(View.INVISIBLE);
                    hodler.tv_pid_title.setText(Html.fromHtml(content.getParse_key()));
                }
                String questionStr = content.getTitle_key();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (detail.ischecked()){
            hodler.down.setImageResource(R.mipmap.period_check_on);
        }else{
            hodler.down.setImageResource(R.mipmap.period_check_off);
        }
        return view;
    }
    private class ViewHodler{

        TextView tv_file_title, tv_pid_title, tv_parse, tv_parse_title;
        ImageView iv_file_title, iv_pid_title, iv_parse, iv_parse_title;
        ImageView down;
    }

    private QuestionContent pullJson(JSONObject jsonObject) throws JSONException{
        if (jsonObject != null){
            QuestionContent content = new QuestionContent();
            content.setUuid(jsonObject.getString("uuid"));
            content.setType(jsonObject.getInt("type"));
            content.setOptions(jsonObject.getInt("options"));
            content.setAnswer(jsonObject.getString("answer"));
            content.setTitle(jsonObject.getString("title"));
            content.setTitle_key(jsonObject.getString("title_key"));
            content.setPid_title(jsonObject.getString("pid_title"));
            content.setPid_title_key(jsonObject.getString("pid_title_key"));
            content.setParse(jsonObject.getString("parse"));
            content.setParse_key(jsonObject.getString("parse_key"));
            return content;
        }else {
            return null;
        }


    }

}
