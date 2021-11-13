package com.sqh.market.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sqh.market.R;
import com.sqh.market.models.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private List<CommentModel> commentModels = new ArrayList<>();
    private Context context;

    public CommentAdapter(Context context) {
        this.context = context;
    }

    public void setCommentModels(List<CommentModel> commentModels) {
        this.commentModels = commentModels;
    }

    @Override
    public int getCount() {
        return commentModels == null ? 0 : commentModels.size();
    }

    @Override
    public Object getItem(int i) {
        return commentModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, viewGroup, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommentModel commentModel = commentModels.get(i);
        holder.userName.setText(commentModel.getUseName());
        holder.content.setText(commentModel.getContent());
        holder.createTime.setText(commentModel.getCreateTime());

        return convertView;
    }

    class ViewHolder {
        TextView userName, content, createTime;

        public ViewHolder(View itemView) {
            userName = itemView.findViewById(R.id.item_comment_user_name);
            content = itemView.findViewById(R.id.item_comment_content);
            createTime = itemView.findViewById(R.id.item_comment_time);
        }
    }
}
