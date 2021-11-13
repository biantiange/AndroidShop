package com.sqh.market.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sqh.market.R;
import com.sqh.market.callbacks.BaseCallback;
import com.sqh.market.view.SpecView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 购物车及购买按钮点击后弹出的小dialog
 * (二次封装github开源dialog组件)
 * url：
 * https://github.com/orhanobut/dialogplus
 */
public class MyCartDialog {
    private Context mContext;
    private boolean first = true;

    private String jsonArr = "";

    public MyCartDialog(Context mContext,String jsonArr) {
        this.mContext = mContext;
        this.jsonArr = jsonArr;
    }

    /**
     * 创建dialog
     *
     * @param callback 回调
     */
    public DialogPlus createDialog(final BaseCallback callback) {
        return DialogPlus.newDialog(mContext)
                .setCancelable(false)
                .setContentHolder(new ViewHolder(R.layout.dialog_cart_layout){
                    @Override
                    public View getInflatedView() {
                        View view = super.getInflatedView();
                        if(first){
                            LinearLayout specWarp = view.findViewById(R.id.specWarp);
                            try {
                                JSONArray jsonArray = new JSONArray(jsonArr);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject o = jsonArray.getJSONObject(i);
                                    specWarp.addView(new SpecView(mContext,o));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            first=false;
                        }
                        return view;
                    }
                })
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        View contentView = dialog.getHolderView();
                        EditText editText = contentView.findViewById(R.id.et_count);
                        int count = Integer.parseInt(StringUtils.isBlank(editText.getText().toString()) ? "1"
                                : editText.getText().toString());

                        switch (view.getId()) {
                            case R.id.btn_close:
                                dialog.dismiss();
                                break;
                            case R.id.iv_count_minus:
                                if (count > 1) {
                                    count -= 1;
                                }
                                editText.setText(count + "");
                                break;
                            case R.id.iv_count_add:
                                editText.setText((++count) + "");
                                break;
                            case R.id.btn_ok:
                                String number = StringUtils.isBlank(editText.getText().toString()) ? "1"
                                        : editText.getText().toString();
                                editText.setText(number);
                                callback.sendMessage(number);
                                break;
                            default:
                                break;

                        }
                    }
                }).create();
    }

}
