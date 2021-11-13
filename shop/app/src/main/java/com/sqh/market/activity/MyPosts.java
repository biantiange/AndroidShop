package com.sqh.market.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.sqh.market.R;
import com.sqh.market.adapter.PostsAdapter;
import com.sqh.market.constant.Constants;
import com.sqh.market.models.PostModel;
import com.sqh.market.utils.LoginCheckUtil;
import com.sqh.market.utils.NetUtil;
import com.sqh.market.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyPosts extends Activity {

    private Activity mContext = this;
    /**
     * 商品List数据源
     */
    private List<PostModel> postList;

    private View mProgressBar;

    private PostsAdapter postsAdapter;

    private RecyclerView postListView;

    private ImageView btnSendPosts;

    /**
     * 用于从网络初始化UI的handler
     */
    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    //网络请求失败
                    Toast.makeText(mContext, "网络请求失败！获取商品类别失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    //网络请求成功，但是返回状态为失败
                    Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //网络请求商品类别返回成功时，初始化控件
                    //绑定数据以及设置Adapter
                    postsAdapter = new PostsAdapter(postList, mContext);
                    postListView.setAdapter(postsAdapter);
                    postListView.setLayoutManager(new LinearLayoutManager(mContext));
                    break;

            }

            //显示列表，隐藏进度条
            loadingAnim(false);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_posts);

        postListView = findViewById(R.id.list_posts);
        mProgressBar = findViewById(R.id.progress);
        btnSendPosts = findViewById(R.id.btn_send_posts);
        btnSendPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginCheckUtil.isLogin(mContext)) {
                    Intent intent = new Intent(mContext, PostsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "您还未登录，请先登录！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getDataFromServer();
        //加载中动效
        loadingAnim(true);
    }

    /**
     * 从网络获取初始数据
     */
    private void getDataFromServer() {
        String myid = String.valueOf(SharedPreferencesUtil
                .get(mContext, "userInfo", "userId", 0));

        String postsUrl = Constants.BASE_URL + Constants.GET_POSTS_URL + "?limit=9999999&page=1&userId="+myid+"&yesMine=1";
        NetUtil.doGet(postsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("网络请求失败！", "网络请求失败！获取商品类别列表失败！");
                Message message = Message.obtain();
                message.what = -1;
                uiHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string().trim();
                com.alibaba.fastjson.JSONObject obj = JSON.parseObject(result);

                Message message = Message.obtain();
                if (obj.getBoolean("flag")) {
                    message.what = 1;
                    //获得商品类型数据
                    List<PostModel> posts = com.alibaba.fastjson.JSONArray
                            .parseArray(JSON.toJSONString(obj.get("data"))
                                    , PostModel.class);
                    postList = posts;
                } else {
                    message.what = 0;
                }

                message.obj = obj.getString("message");
                uiHandler.sendMessage(message);
            }
        });

    }

    /**
     * 当进行网络请求时，播放进度条动画
     *
     * @param isLoading 是否正在网络请求
     */
    private void loadingAnim(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        postListView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
