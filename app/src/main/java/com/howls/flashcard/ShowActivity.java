package com.howls.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private SwipeCardsView swipeCardsView;
    private List<Flashcard> flashcards = new ArrayList<>();
    private MyDBHandle db = new MyDBHandle(this);
    private String albumID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        albumID = intent.getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);

        swipeCardsView = findViewById(R.id.swipe);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);

        flashcards = db.getAllFashcards(albumID);
        CardListAdapter adapter = new CardListAdapter(this,flashcards);
        swipeCardsView.setAdapter(adapter);
    }
}
