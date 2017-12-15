package com.howls.flashcard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.huxq17.swipecardsview.BaseCardAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by howls on 17/11/23.
 */

public class CardListAdapter extends BaseCardAdapter{

    private Context mContext;
    private List<Flashcard> flashcardList;

    public CardListAdapter(Context mContext, List<Flashcard> flashcardList) {
        this.mContext = mContext;
        this.flashcardList = flashcardList;
    }

    @Override
    public int getCount() {
        return flashcardList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.card_item;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if (flashcardList == null || flashcardList.size() == 0) {
            return;
        }
        Flashcard flashcard = flashcardList.get(position);

    }
}
