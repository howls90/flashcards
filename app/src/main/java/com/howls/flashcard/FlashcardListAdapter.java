package com.howls.flashcard;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by howls on 17/11/23.
 */

public class FlashcardListAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private List<Flashcard> flashcardList;

    public FlashcardListAdapter(Context mContext, List<Flashcard> mWordList) {
        this.mContext = mContext;
        this.flashcardList = mWordList;
    }

    public int getGroupCount() {return flashcardList.size();}

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    public Object getGroup(int groupPosition) {
        return flashcardList.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return flashcardList.get(groupPosition);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext,R.layout.flashcard_list_group,null);
        TextView word = (TextView)v.findViewById(R.id.word);
        word.setText(flashcardList.get(i).getWord());
        v.setTag(flashcardList.get(i).getId());
        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext,R.layout.flashcard_list_child,null);
        TextView read = (TextView)v.findViewById(R.id.read);
        TextView translate = (TextView)v.findViewById(R.id.translate);
        read.setText(flashcardList.get(i).getRead());
        translate.setText(flashcardList.get(i).getTranslate());


        v.setTag(flashcardList.get(i).getId());

        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
