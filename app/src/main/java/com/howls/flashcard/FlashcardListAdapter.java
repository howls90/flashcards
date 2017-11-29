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

public class FlashcardListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Flashcard> flashcardList;

    public FlashcardListAdapter(Context mContext, List<Flashcard> mWordList) {
        this.mContext = mContext;
        this.flashcardList = mWordList;
    }

    @Override
    public int getCount() {
        return flashcardList.size();
    }

    @Override
    public Object getItem(int i) {
        return flashcardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext,R.layout.item_flashcard_list,null);
        TextView word = (TextView)v.findViewById(R.id.albumName);
        word.setText(flashcardList.get(i).getWord());

        v.setTag(flashcardList.get(i).getId());

        return v;
    }
}
