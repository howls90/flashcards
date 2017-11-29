package com.howls.flashcard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by howls on 17/11/23.
 */

public class AlbumListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Album> albumList;

    public AlbumListAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int i) {
        return albumList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext,R.layout.item_album_list,null);

        TextView albumName = (TextView)v.findViewById(R.id.albumName);
        TextView albumLang = (TextView)v.findViewById(R.id.albumDescription);

        albumName.setText(albumList.get(i).getName());
        albumLang.setText(albumList.get(i).getDescription());

        v.setTag(albumList.get(i).getId());

        return v;
    }
}
