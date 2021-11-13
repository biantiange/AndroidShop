package com.sqh.market.adapter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sqh.market.R;
import com.sqh.market.constant.Constants;
import com.sqh.market.models.ReplyInfo;
import com.sqh.market.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.FormBody;
import okhttp3.RequestBody;


public class ReplyAdapter extends RecyclerView.Adapter {

    private List<ReplyInfo> mReplyInfoList = new ArrayList<>();
    private PostsAdapter superThat;
    private String myid;
    private int superPosition;

    public ReplyAdapter(List<ReplyInfo> mReplyInfoList, PostsAdapter that, String myid, int position) {
        this.mReplyInfoList=mReplyInfoList;
        this.superThat=that;
        this.myid=myid;
        this.superPosition=position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_posts_reply,null);
        MyViewHolder mvh=new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ReplyInfo info = mReplyInfoList.get(position);

        //判断是否为自己发的帖子或者是不是自己的回复 不是则隐藏删除按钮
        if(info.isYesMine()){
            ((MyViewHolder) holder).reply_del_button.setVisibility(View.VISIBLE);
        }else {
            ((MyViewHolder) holder).reply_del_button.setVisibility(View.INVISIBLE);
        }

        //为回复设置内容
        ((MyViewHolder)holder).reply_text.setText(info.getUserName()+":"+info.getContent());

        //删除回复
        ((MyViewHolder)holder).reply_del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Handler handler = new Handler();

                //网络删除
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            String result = HttpUtils.okGet(Constants.BASE_URL+Constants.DELETE_REPLY_URL
                                    +"?commentId="+info.getCommentId());
                            boolean flag = new JSONObject(result).getBoolean("flag");
                            if(flag){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(superThat.activity, "删除成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                //本地删除
                mReplyInfoList.remove(position);//list移除
                ReplyAdapter.this.notifyDataSetChanged();//界面响应

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mReplyInfoList==null)
            return 0;
        else
            return mReplyInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView reply_text;
        private TextView reply_del_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reply_text=itemView.findViewById(R.id.reply_text);
            reply_del_button=itemView.findViewById(R.id.reply_del_button);
        }
    }

}
