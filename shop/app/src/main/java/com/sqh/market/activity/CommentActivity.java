package com.sqh.market.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.sqh.market.R;
import com.sqh.market.models.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    /**
     * 返回按钮
     */
    private ImageView mBtnBack;

    /**
     * 评论 对应的 ListView
     */
    private ListView commentListView;

    /**
     * 用于接收后端传来的 评论数据
     */
    private List<CommentModel> commentModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        init();
    }

    /**
     * 初始化绑定控件
     */
    private void init() {
        mBtnBack = findViewById(R.id.comment_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commentListView = findViewById(R.id.comment_listView);
    }
}