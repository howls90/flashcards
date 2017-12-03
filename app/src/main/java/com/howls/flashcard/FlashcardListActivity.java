package com.howls.flashcard;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

public class FlashcardListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE = "com.howls.languagenotes.AlbumId";
    MyDBHandle db;
    private ExpandableListView flashcardLayout;
    private FlashcardListAdapter adapter;
    private List<Flashcard> flashcardList;
    private List<Album> albumList;
    MediaPlayer m;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Default");

        setContentView(R.layout.activity_flashcard_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        db = new MyDBHandle(this);

        albumList = db.getAllAlbums();

        if (albumList.size() == 0) {
            db.addAlbum(new Album("Default"));
            finish();
            startActivity(getIntent());
        } else {
            for (i=0;i<albumList.size();i++) {
                final Album album =  albumList.get(i);
                menu.add(album.getName()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        setTitle(album.getName());
                        return false;
                    }
                });

            }
        }


        flashcardLayout = (ExpandableListView)findViewById(R.id.listViewFlashcard);
        flashcardList = db.getAllFashcards();
        adapter = new FlashcardListAdapter(this, flashcardList);
        flashcardLayout.setAdapter(adapter);

        flashcardLayout.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    flashcardLayout.collapseGroup(previousItem );
                previousItem = groupPosition;
            }
        });

        flashcardLayout.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
            try {
                MediaPlayer m = new MediaPlayer();
                m.setDataSource(flashcardList.get(i).getSound());
                m.prepare();
                m.start();
            } catch (IOException e) {}
            return false;
            }
        });

        flashcardLayout.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Intent intent = new Intent(getApplicationContext(), FlashcardShowActivity.class);
            String flashcardId = v.getTag().toString();
            Log.i("sasds",String.valueOf(groupPosition));
            intent.putExtra(EXTRA_MESSAGE, String.valueOf(groupPosition));
            startActivity(intent);
            return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flashcard_list, menu);
        return true;
    }

    public void play(int i){
        try {
            m = new MediaPlayer();
            m.setDataSource(flashcardList.get(i).getSound());
            m.prepare();
            m.start();
        } catch (IOException e) {}
    }

    public void newAlbum(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);

        alert.setTitle("New Album");
        alert.setView(edittext);

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = edittext.getText().toString();
                Album album = new Album(name);

                db = new MyDBHandle(getApplicationContext());
                db.addAlbum(album);
                finish();
                startActivity(getIntent());
            }
        });

        alert.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    public void playAudio(String path) {
        try {
            m = new MediaPlayer();
            m.setDataSource(path);
            m.prepare();
            m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    m.stop();
                    if (i < flashcardList.size()-1) {
                        i++;
                        if (flashcardList.get(i).getSound() != null) {
                            playAudio(flashcardList.get(i).getSound());
                        } else {

                        }
                    } else i = 0;
                }
            });
            m.start();
        } catch (IOException e) {}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            return true;
        }
        if (id == R.id.item_add) {
            Intent intent = new Intent(this, FlashcardNewActivity.class);
            startActivity(intent);
        }
        if (id == R.id.item_play) {

            playAudio(flashcardList.get(0).getSound());

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
