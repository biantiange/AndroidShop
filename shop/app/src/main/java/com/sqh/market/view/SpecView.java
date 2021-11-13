package com.sqh.market.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sqh.market.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpecView extends RelativeLayout {

    private JSONObject json;

    public SpecView(Context context) {
        super(context);
        init(context);
    }

    public SpecView(Context context,JSONObject json) {
        super(context);
        this.json=json;
        init(context);
    }

    public SpecView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpecView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SpecView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {

        View inflate = View.inflate(context, R.layout.spec_group, this);
        RadioGroup radioGroup = inflate.findViewById(R.id.radioGroup);


        TextView name = inflate.findViewById(R.id.name);
        try {
            JSONObject jsonObject = json;
            String name1 = jsonObject.getString("name");
            name.setText(name1);

            JSONArray sel = jsonObject.getJSONArray("sel");
            for(int i=0;i<sel.length();i++){
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(sel.getString(i));
                radioGroup.addView(radioButton);
                if(i==0){
                    radioButton.setChecked(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
