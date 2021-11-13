package com.sqh.market.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sqh.market.R;
import com.sqh.market.constant.Constants;
import com.sqh.market.utils.HttpUtils;
import com.sqh.market.utils.SharedPreferencesUtil;
import com.sqh.market.utils.Uri2PathUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PostsActivity extends Activity {

    private ImageView imageAdd[] = new ImageView[3];
    private String jobBase[]=new String[3];
    private EditText sendEditext;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_posts);


        mHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String res = (String)msg.obj;
                try {
                    boolean flag = new JSONObject(res).getBoolean("flag");
                    if(flag){
                        Toast.makeText(PostsActivity.this, "发帖成功", Toast.LENGTH_SHORT).show();
                        PostsActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        };

        init();
    }

    /**
     * 初始化绑定控件
     */
    private void init() {

        findViewById(R.id.cart_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostsActivity.this.finish();
            }
        });
        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });


        imageAdd[0]=findViewById(R.id.image_add1);
        imageAdd[1]=findViewById(R.id.image_add2);
        imageAdd[2]=findViewById(R.id.image_add3);

        imageAdd[0].setOnClickListener(new MyOnClickListener());
        imageAdd[1].setOnClickListener(new MyOnClickListener());
        imageAdd[2].setOnClickListener(new MyOnClickListener());

        sendEditext=findViewById(R.id.send_editext);

    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.image_add1:
                    startIntentForImg(0);
                    break;
                case R.id.image_add2:
                    startIntentForImg(1);
                    break;
                case R.id.image_add3:
                    startIntentForImg(2);
                    break;
            }
        }

        private void startIntentForImg(int pos) {
            Intent intent = new Intent();
            intent.setType("image/*");
            /* 使用Intent.ACTION_GET_CONTENT这个Action */
            intent.setAction(Intent.ACTION_GET_CONTENT);
            /* 取得相片后返回本画面 */
            startActivityForResult(intent, pos);
        }
    }

    public void send() {

        int uId = (Integer) SharedPreferencesUtil
                .get(this, "userInfo", "userId", 0);

        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");// HH:mm:ss
        String timetext = simpleDateFormat.format(date);

        String content = sendEditext.getText().toString();



        if(jobBase[0]==null)
            jobBase[0]="";
        if(jobBase[1]==null)
            jobBase[1]="";
        if(jobBase[2]==null)
            jobBase[2]="";

        RequestBody body = new FormBody.Builder()
                .add("imgOne",jobBase[0])
                .add("imgTwo",jobBase[1])
                .add("imgThree",jobBase[2])
                .add("content",content)
                .add("createTime",timetext)
                .add("userId", String.valueOf(uId))
                .build();


        getDataFromPost(body);

    }

    private void getDataFromPost(final RequestBody body) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String result = HttpUtils.okPost(Constants.BASE_URL+Constants.POST_URL,body);
                    Message msg = new Message();
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    protected void onActivityResult(final int requestCode, int resultCode, final Intent data){

        // 从相册返回的数据
        if (data != null) {
            Uri originalUri = data.getData();
            imageAdd[requestCode].setImageURI(originalUri);
            String filePath = Uri2PathUtil.getRealPathFromUri(PostsActivity.this,originalUri);
            filePath+="";

            final String finalFilePath = filePath;
            //Toast.makeText(this, finalFilePath, Toast.LENGTH_SHORT).show();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String res = HttpUtils.uploadFile(Constants.BASE_URL+Constants.UPLOAD_URL, finalFilePath,"temp.png");
                    try {
                        res = new JSONObject(res).getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    res+="";
                    jobBase[requestCode] = res;
                }
            }.start();



        }
    }
}
