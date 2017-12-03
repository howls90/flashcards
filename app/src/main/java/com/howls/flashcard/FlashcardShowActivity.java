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

public class FlashcardShowActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    MyDBHandle db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_show);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashcard_show, menu);
        return true;
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
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        MyDBHandle db;
        String outputFile;

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

            db = new MyDBHandle(getContext());

            final List<Flashcard> flashcards = db.getAllFashcards();
            TextView word = (TextView) rootView.findViewById(R.id.word);
            TextView read = (TextView) rootView.findViewById(R.id.read);
            TextView translate = (TextView) rootView.findViewById(R.id.translate);
            ImageButton play = (ImageButton) rootView.findViewById(R.id.play);
            Button delete = (Button)rootView.findViewById(R.id.delete);

            int pos = Integer.parseInt(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            final Flashcard flashcard = flashcards.get(pos);
            word.setText(flashcard.getWord());
            read.setText(flashcard.getRead());
            translate.setText(flashcard.getTranslate());
            outputFile = flashcard.getSound();

            play.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (outputFile != null) {
                        try {
                            MediaPlayer m = new MediaPlayer();
                            m.setDataSource(flashcard.getSound());
                            m.prepare();
                            m.start();

                            Toast.makeText(getContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {}
                    } else {
                        Toast.makeText(getContext(), "Not recording", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
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

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            db = new MyDBHandle(getApplicationContext());
            List<Flashcard> flashcardList = db.getAllFashcards();

            return flashcardList.size();
        }
    }
}
