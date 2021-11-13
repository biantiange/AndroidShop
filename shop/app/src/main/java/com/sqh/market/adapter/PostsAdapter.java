package com.sqh.market.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bumptech.glide.Glide;
import com.sqh.market.R;
import com.sqh.market.constant.Constants;
import com.sqh.market.models.PostModel;
import com.sqh.market.models.ReplyInfo;
import com.sqh.market.utils.HttpUtils;
import com.sqh.market.utils.MyImageView;
import com.sqh.market.utils.NetUtil;
import com.sqh.market.utils.SharedPreferencesUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PostsAdapter extends RecyclerView.Adapter {


    protected List<PostModel> postModels;
    public Activity activity;
    private PostsAdapter that;
    private String myid,myname;//自己id,name

    public PostsAdapter(List<PostModel> postModels, Activity activity) {
        this.postModels=postModels;
        this.activity=activity;
        this.that=this;

        myid = String.valueOf(SharedPreferencesUtil
                .get(activity, "userInfo", "userId", 0));
        myname = String.valueOf(SharedPreferencesUtil
                .get(activity, "userInfo", "username",""));
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_posts, parent, false);

        MyViewHolder mvh=new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        PostModel info=postModels.get(position);

        //为每个条目设置数据
        ((MyViewHolder) holder).content.setText(info.getContent());

        ((MyViewHolder) holder).timetext.setText(info.getCreateTime());

        ((MyViewHolder) holder).pCount.setText("点赞数：" + info.getpCount());

        ((MyViewHolder) holder).senduser.setText(info.getUname());

        if(info.isPraiseFlag()){
            ((MyViewHolder) holder).up_button.setImageDrawable(activity.getDrawable(R.drawable.zan_fill));
        }

        //设置banner样式
        ((MyViewHolder) holder).banner.setBannerStyle(0);

        GlideImageLoader glideImageLoader = new GlideImageLoader();

        //设置图片加载器
        ((MyViewHolder) holder).banner.setImageLoader(glideImageLoader);
        final ArrayList<String> list_path = new ArrayList<>();
        if(info.getImgOne()!=null&&!"".equals(info.getImgOne()))
            list_path.add(info.getImgOne());
        if(info.getImgTwo()!=null&&!"".equals(info.getImgTwo()))
            list_path.add(info.getImgTwo());
        if(info.getImgThree()!=null&&!"".equals(info.getImgThree()))
            list_path.add(info.getImgThree());

        if(info.getImgOne()==null&&info.getImgTwo()==null&&info.getImgThree()==null) {
            list_path.add(null);
        }
        //设置图片集合
        ((MyViewHolder) holder).banner.setImages(list_path);

        //设置轮播时间
        ((MyViewHolder) holder).banner.setDelayTime(2000);
        ((MyViewHolder) holder).banner.isAutoPlay(true);


        //((MyViewHolder) holder).banner.setOverScrollMode(2);
        //banner设置方法全部调用完毕时最后调用
        ((MyViewHolder) holder).banner.start();
        //设置点击时间放大图片
        ((MyViewHolder) holder).banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                final MyImageView img = new MyImageView(activity);
                img.setMinimumHeight(500);
                img.setImageURL(list_path.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("").setView(img);
                builder.show();
            }
        });



        //为每个条目的点赞和回复和删除按钮设置响应事件
        ((MyViewHolder) holder).up_button.setOnClickListener(new MyOnClickListener(info,activity,position,holder));
        ((MyViewHolder) holder).reply_button.setOnClickListener(new MyOnClickListener(info,activity,position,holder));
        ((MyViewHolder) holder).del_button.setOnClickListener(new MyOnClickListener(info,activity,position,holder));

        //判断是否为自己发的帖子 不是则隐藏删除按钮
        if(!String.valueOf(info.getUserId()).equals(myid)){
            ((MyViewHolder) holder).del_button.setVisibility(View.INVISIBLE);
        }else{
            ((MyViewHolder) holder).del_button.setVisibility(View.VISIBLE);
        }

        //帖子评论
        String commentModels = info.getCommentModels();
        List<ReplyInfo> replyInfos = new ArrayList<>();
        if(commentModels!=null){

            JSONArray arr=JSONArray.parseArray(commentModels);

            for (int i = 0; i < arr.size(); i++) {
                JSONObject jsonObject = arr.getJSONObject(i);

                ReplyInfo replyInfo = new ReplyInfo(jsonObject.getString("commentId"),
                        jsonObject.getString("userName"),
                        jsonObject.getString("content"),
                        jsonObject.getString("createTime"),
                        jsonObject.getBoolean("yesMine"));
                replyInfos.add(replyInfo);
            }
        }

        //为评论区设置数据
        info.setReplyInfos(replyInfos);
        ReplyAdapter mReplyAdapter=new ReplyAdapter(info.getReplyInfos(),that,myid,position);
        info.setReplyAdapter(mReplyAdapter);
        ((MyViewHolder) holder).communitrv.setLayoutManager(new LinearLayoutManager(activity));
        ((MyViewHolder) holder).communitrv.setAdapter(info.getReplyAdapter());


    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView content,del_button;
        private Banner banner;
        private TextView pCount,senduser,timetext;
        private ImageView up_button,reply_button;
        private RecyclerView communitrv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            content=itemView.findViewById(R.id.content);
            banner= itemView.findViewById(R.id.banner);
            pCount=itemView.findViewById(R.id.pCount);
            senduser=itemView.findViewById(R.id.item_name);
            timetext=itemView.findViewById(R.id.item_time);
            up_button=itemView.findViewById(R.id.up_button);
            reply_button=itemView.findViewById(R.id.reply_button);
            del_button=itemView.findViewById(R.id.item_del_button);

            communitrv=itemView.findViewById(R.id.communitrv);
        }
    }

    class MyOnClickListener implements View.OnClickListener{
        private PostModel info;
        private int pos;
        private RecyclerView.ViewHolder holder;
        public MyOnClickListener(PostModel info, Activity activity, int pos, RecyclerView.ViewHolder holder) {
            super();
            this.info=info;
            this.pos=pos;
            this.holder=holder;
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.reply_button:
                    //这里添加回复的代码
                    final EditText input = new EditText(activity);

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("回复/评论").setView(input)
                            .setNegativeButton("取消", null);
                    builder.setPositiveButton("发表", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String retext = input.getText().toString();

                            if(!"".equals(retext)){

                                //生成时间
                                Date date = new Date(System.currentTimeMillis());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");// HH:mm:ss
                                String timetext = simpleDateFormat.format(date);

                                //网络添加代码
                                final RequestBody body = new FormBody.Builder()
                                        .add("postId",String.valueOf(info.getPostId()))
                                        .add("content",input.getText().toString())
                                        .add("createTime",timetext)
                                        .add("deleteFlag","0")
                                        .add("userId",myid)
                                        .build();

                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            String result = HttpUtils.okPost(Constants.BASE_URL+Constants.SEND_REPLY_URL,body);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();


                                //在本地添加评论
                                List<ReplyInfo> replyInfos = info.getReplyInfos();
                                ReplyInfo replyInfo = new ReplyInfo(null,
                                        myname,
                                        input.getText().toString(),
                                        timetext,
                                        true);
                                replyInfos.add(replyInfo);
                                info.getReplyAdapter().notifyDataSetChanged();
                                //that.notifyDataSetChanged();

                            }

                        }
                    });
                    builder.show();

                    break;
                case R.id.up_button:
                    //这里添加点赞和取消的代码

                    if(info.isPraiseFlag()){
                        //取消赞

                        ((ImageView)view).setImageDrawable(activity.getDrawable(R.drawable.zan));
                        final String s = String.valueOf(Integer.parseInt(info.getpCount()) - 1);
                        info.setpCount(s);
                        ((MyViewHolder) holder).pCount.setText("点赞数：" + info.getpCount());
                        info.setPraiseFlag(false);
                        String postsUrl = Constants.BASE_URL + Constants.DELETE_PRAISE_URL;
                        //构造请求体
                        RequestBody body = new FormBody.Builder()
                                .add("userId", myid)
                                .add("postId", info.getPostId()+"")
                                .build();
                        NetUtil.doPost(postsUrl,body, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("网络请求失败！", "网络请求失败！获取商品类别列表失败！");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("点赞成功", "点赞成功");
                            }
                        });
                    }else {
                        //点赞

                        ((ImageView)view).setImageDrawable(activity.getDrawable(R.drawable.zan_fill));
                        final String s = String.valueOf(Integer.parseInt(info.getpCount()) + 1);
                        info.setpCount(s);
                        ((MyViewHolder) holder).pCount.setText("点赞数：" + info.getpCount());
                        info.setPraiseFlag(true);
                        String postsUrl = Constants.BASE_URL + Constants.ADD_PRAISE_URL;
                        //构造请求体
                        RequestBody body = new FormBody.Builder()
                                .add("userId", myid)
                                .add("postId", info.getPostId()+"")
                                .build();
                        NetUtil.doPost(postsUrl,body, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("网络请求失败！", "网络请求失败！获取商品类别列表失败！");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("点赞成功", "点赞成功");
                            }
                        });
                    }

                    break;
                case R.id.item_del_button:
                    //这里添加删除的代码
                    final Handler handler = new Handler();
                    new AlertDialog.Builder(activity)
                            .setTitle("确定删除吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //网络删除
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            super.run();
                                            try {
                                                String result = HttpUtils.okGet(Constants.BASE_URL+Constants.DELETE_POSTS_URL
                                                        +"?postId="+info.getPostId());
                                                boolean flag = new org.json.JSONObject(result).getBoolean("flag");
                                                if(flag){
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //本地删除
                                                            postModels.remove(pos);
                                                            that.notifyDataSetChanged();
                                                            Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            } catch (IOException | JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();


                                }
                            })
                            .setNegativeButton("取消",null)
                            .create()
                            .show();



                    break;
            }

        }

    }

    public class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }

}
