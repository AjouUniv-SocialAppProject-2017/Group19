package com.share.keyword.sharekeywordapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.share.keyword.sharekeywordapplication.R;
import com.share.keyword.sharekeywordapplication.datas.Memo;

import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {
    Context context;
    ArrayList<Memo> list = new ArrayList<>();

    public MemoAdapter(Context context, ArrayList<Memo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_memo, viewGroup, false);
        }

        TextView title = view.findViewById(R.id.tv_title);
        TextView content = view.findViewById(R.id.tv_content);

        title.setText(list.get(i).getTitle());
        content.setText(list.get(i).getContent());

        return view;
    }
}
