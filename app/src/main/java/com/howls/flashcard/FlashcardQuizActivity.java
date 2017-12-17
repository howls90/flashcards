package com.howls.flashcard;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class FlashcardQuizActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "AlbumId";
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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        albumId = getIntent().getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);

        getSupportActionBar().setTitle(db.getAlbum(albumId).getName());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_quiz, menu);
        return true;
    }

    public String getMyData() {
        Intent intent = getIntent();
        String msn = intent.getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);
        return msn;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_edit) {
            return true;
        }
        if (id == R.id.action_delete) {
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
            final View rootView = inflater.inflate(R.layout.fragment_flashcard_quiz, container, false);

            FlashcardQuizActivity activity = (FlashcardQuizActivity) getActivity();
            albumId = activity.getMyData();

            db = new MyDBHandle(getContext());
            final List<Flashcard> flashcards = db.getAllFashcards(albumId);
            Collections.shuffle(flashcards);
            int pos = Integer.parseInt(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            final Flashcard flashcard = flashcards.get(pos);

            final List<Flashcard> flashcards_options = flashcards;
            flashcards_options.remove(pos);

            final Button option1 = rootView.findViewById(R.id.option1);
            final Button option2 = rootView.findViewById(R.id.option2);
            final Button option3 = rootView.findViewById(R.id.option3);

            final RingProgressBar progress = rootView.findViewById(R.id.progress);

            Random rand = new Random();
            int num = rand.nextInt((3 - 1) + 1) + 1;

            if (num == 1) {
                option1.setText(flashcard.getTranslate());

                int val = rand.nextInt((flashcards_options.size()));
                option2.setText(flashcards_options.get(val).getTranslate());
                int val1 = rand.nextInt((flashcards_options.size()));
                option3.setText(flashcards_options.get(val1).getTranslate());

                option1.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option1.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.play));
                    }
                });
                option2.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option2.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.delete));
                    }
                });
                option3.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option3.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.delete));
                    }
                });
            }
            if (num == 2) {
                option2.setText(flashcard.getTranslate());
                int val = rand.nextInt((flashcards_options.size()));
                option1.setText(flashcards_options.get(val).getTranslate());
                int val1 = rand.nextInt((flashcards_options.size()));
                option3.setText(flashcards_options.get(val1).getTranslate());

                option2.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option2.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.play));
                    }
                });
                option1.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option1.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.delete));
                    }
                });
                option3.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option3.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.delete));
                    }
                });
            }
            if (num == 3) {
                option3.setText(flashcard.getTranslate());
                int val = rand.nextInt((flashcards_options.size()));
                option2.setText(flashcards_options.get(val).getTranslate());
                int val1 = rand.nextInt((flashcards_options.size()));
                option1.setText(flashcards_options.get(val1).getTranslate());

                option3.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option3.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.play));
                    }
                });
                option1.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option1.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.delete));
                    }
                });
                option2.setOnClickListener(new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v)
                    {
                        option2.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.delete));
                    }
                });
            }

            ImageButton play = rootView.findViewById(R.id.play);

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


            TextView term = rootView.findViewById(R.id.term);
            term.setText(flashcard.getWord());


            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        Intent intent = getIntent();
        String albumId = intent.getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);

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
