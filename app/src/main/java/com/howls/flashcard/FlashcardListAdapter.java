package com.howls.flashcard;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
        //return flashcardList.get(i).getPosition();
        Log.i("ffffff",flashcardList.get(i).getWord());
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
        TextView word = v.findViewById(R.id.word);
        word.setText(flashcardList.get(i).getWord());
        v.setTag(flashcardList.get(i).getId());
        return v;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext,R.layout.flashcard_list_child,null);
        TextView read = v.findViewById(R.id.read);
        TextView translate = v.findViewById(R.id.translate);
        read.setText(flashcardList.get(i).getRead());
        translate.setText(flashcardList.get(i).getTranslate());
        flashcardList.get(i).setPosition(i);

        v.setTag(flashcardList.get(i).getId());

        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void filterData(List<Flashcard> flashcardsOrigin , String query) {
        query = query.toLowerCase();
        List<Flashcard> newList = new ArrayList<Flashcard>();
        newList.clear();

        int position=0, childID=0;
        for (Flashcard flashcard : flashcardsOrigin) {
            if (flashcard.getWord().toLowerCase().contains(query) || flashcard.getTranslate().toLowerCase().contains(query)) {
                flashcard.setPosition(position);
                flashcard.setChildID(childID);
                newList.add(flashcard);
                childID++;
            }
            position++;

        }

        this.flashcardList = newList;
        notifyDataSetChanged();
    }
}
