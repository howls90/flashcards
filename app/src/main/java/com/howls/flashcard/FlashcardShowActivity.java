package com.howls.flashcard;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class FlashcardShowActivity extends AppCompatActivity {

    static final String EXTRA_MESSAGE = "AlbumId";
    static final String EXTRA_MESSAGE2 = "flashcardId";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MyDBHandle db = new MyDBHandle(this);
    private String albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String msn = getIntent().getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);
        String childID = msn.split("/")[0];
        albumId = msn.split("/")[1];

        getSupportActionBar().setTitle(db.getAlbum(albumId).getName());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(Integer.valueOf(childID));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_show, menu);
        return true;
    }

    public String getMyData() {
        Intent intent = getIntent();
        String msn = intent.getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);
        albumId = msn.split("/")[1];

        return albumId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_quiz) {
            return true;
        }

        if (id == R.id.item_flashcardnew_return) {
            Intent intent = new Intent(this,FlashcardListActivity.class);
            intent.putExtra(EXTRA_MESSAGE, albumId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        MyDBHandle db;
        String outputFile;
        String albumId;

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_flashcard_show, container, false);

            FlashcardShowActivity activity = (FlashcardShowActivity) getActivity();
            albumId = activity.getMyData();

            TextView word = rootView.findViewById(R.id.word);
            TextView translate = rootView.findViewById(R.id.translate);
            TextView examples = rootView.findViewById(R.id.examples);
            TextView notes = rootView.findViewById(R.id.notes);
            ImageButton play = rootView.findViewById(R.id.play);
            ImageButton delete = rootView.findViewById(R.id.delete);
            ImageButton edit = rootView.findViewById(R.id.edit);
            final RingProgressBar progress = rootView.findViewById(R.id.progress);

            db = new MyDBHandle(getContext());
            final List<Flashcard> flashcards = db.getAllFashcards(albumId);

            int pos = Integer.parseInt(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            final Flashcard flashcard = flashcards.get(pos);

            word.setText(flashcard.getWord());
            translate.setText(flashcard.getTranslate());
            examples.setText(flashcard.getExamples());
            notes.setText(flashcard.getNotes());
            outputFile = flashcard.getSound();

            play.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                final int duration = flashcard.sound();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    for(int i=0;i<=100;i++) {
                        try{
                            Thread.sleep(duration/100);
                            progress.setProgress(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    }
                }).start();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("Are you sure you want to delete?");
                alert.setCancelable(false);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    File sound = new File(flashcard.getSound());
                    sound.delete();
                    db.deleteFlashcard(String.valueOf(flashcard.getId()));
                    Intent intent = new Intent(getContext(),FlashcardListActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, albumId);
                    startActivity(intent);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.create().show();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),FlashcardEditActivity.class);
                    intent.putExtra(EXTRA_MESSAGE2, String.valueOf(flashcard.getId()));
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        String msn = getIntent().getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);
        String albumId = msn.split("/")[1];

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            db = new MyDBHandle(getApplicationContext());
            List<Flashcard> flashcardList = db.getAllFashcards(albumId);
            return flashcardList.size();
        }
    }
}
