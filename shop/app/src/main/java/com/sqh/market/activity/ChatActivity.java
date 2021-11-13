package com.sqh.market.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sqh.market.R;
import com.sqh.market.adapter.ChatAdapter;
import com.sqh.market.models.PersonChat;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {
    private ChatAdapter chatAdapter;
    /**
     * 声明ListView
     */
    private ListView lv_chat_dialog;
    /**
     * 集合
     */
    private List<PersonChat> personChats = new ArrayList<PersonChat>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            switch (what) {
                case 1:
                    /**
                     * ListView条目控制在最后一行
                     */
                    lv_chat_dialog.setSelection(personChats.size());
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        findViewById(R.id.cart_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.this.finish();
            }
        });
        /**
         * 虚拟4条发送方的消息
         */

        PersonChat personChat = new PersonChat();
        personChat.setChatMessage("您好，需要什么帮助？");
        personChat.setName("机器人");
        personChat.setMeSend(false);
        personChats.add(personChat);

        lv_chat_dialog = (ListView) findViewById(R.id.lv_chat_dialog);
        Button btn_chat_message_send = (Button) findViewById(R.id.btn_chat_message_send);
        final EditText et_chat_message = (EditText) findViewById(R.id.et_chat_message);
        /**
         *setAdapter
         */
        chatAdapter = new ChatAdapter(this, personChats);
        lv_chat_dialog.setAdapter(chatAdapter);
        /**
         * 发送按钮的点击事件
         */
        btn_chat_message_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(et_chat_message.getText().toString())) {
                    Toast.makeText(ChatActivity.this, "发送内容不能为空", 0).show();
                    return;
                }
                PersonChat personChat = new PersonChat();
                //代表自己发送
                personChat.setMeSend(true);
                //得到发送内容
                personChat.setChatMessage(et_chat_message.getText().toString());
                //加入集合
                personChats.add(personChat);
                //清空输入框
                et_chat_message.setText("");


                PersonChat personChat2 = new PersonChat();
                personChat2.setChatMessage("好的，尽快帮您处理。");
                personChat2.setName("机器人");
                personChat2.setMeSend(false);
                personChats.add(personChat2);


                //刷新ListView
                chatAdapter.notifyDataSetChanged();
                handler.sendEmptyMessage(1);
            }
        });
    }

}
