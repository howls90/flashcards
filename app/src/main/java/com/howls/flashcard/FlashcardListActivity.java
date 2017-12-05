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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FlashcardListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE = "AlbumId";
    private MyDBHandle db;
    private ExpandableListView flashcardLayout;
    private FlashcardListAdapter adapter;
    private List<Flashcard> flashcardList;
    private List<Album> albumList;
    private MediaPlayer m;
    int i = 0;
    int albumId = 1;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flashcard_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();

        db = new MyDBHandle(this);
        albumList = db.getAllAlbums();

        if (albumList.size() == 0) {
            db.addAlbum(new Album("Default"));
            finish();
            startActivity(getIntent());
        } else {
            for (i=0;i<albumList.size();i++) {
                final Album album =  albumList.get(i);
                flashcardList = db.getAllFashcards(String.valueOf(album.getId()));
                menu.add(album.getName()+" ("+flashcardList.size()+")").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        for (int i = 0; i < menu.size(); i++) {
                            menu.getItem(i).setChecked(false);
                        }
                        setTitle(album.getName());
                        menuItem.setChecked(true);
                        albumId=album.getId();
                        flashcardLayout = findViewById(R.id.listViewFlashcard);
                        flashcardList = db.getAllFashcards(String.valueOf(albumId));
                        adapter = new FlashcardListAdapter(getApplicationContext(), flashcardList);
                        flashcardLayout.setAdapter(adapter);
                        return false;
                    }
                });
            }
        }

        Intent intent = getIntent();
        if (intent.hasExtra("AlbumId")) {
            albumId = Integer.valueOf(intent.getStringExtra(FlashcardNewActivity.EXTRA_MESSAGE));
            setTitle(db.getAlbum(String.valueOf(albumId)).getName());
            for (int i =0;i<albumList.size();i++) {
                if (albumList.get(i).getId() == albumId) {
                    menu.getItem(i).setChecked(true);
                }
            }
        } else {
            setTitle("Default");
            menu.getItem(0).setChecked(true);
        }

        flashcardLayout = findViewById(R.id.listViewFlashcard);
        flashcardList = db.getAllFashcards(String.valueOf(albumId));
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
            intent.putExtra(EXTRA_MESSAGE, String.valueOf(groupPosition)+"/"+albumId);
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
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterData(flashcardList, newText);
                return false;
            }

        });

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
            public void onClick(DialogInterface dialog, int whichButton) {}
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
                    flashcardLayout.expandGroup(i);
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

        if (id == R.id.item_game) {
            flashcardLayout = (ExpandableListView)findViewById(R.id.listViewFlashcard);
            flashcardList = db.getAllFashcards(String.valueOf(albumId));
            Collections.shuffle(flashcardList);
            adapter = new FlashcardListAdapter(getApplicationContext(), flashcardList);
            flashcardLayout.setAdapter(adapter);
            return true;
        }
        if (id == R.id.item_add) {
            Intent intent = new Intent(this, FlashcardNewActivity.class);
            intent.putExtra(EXTRA_MESSAGE, String.valueOf(albumId));
            startActivity(intent);
        }
        if (id == R.id.item_play) {
            playAudio(flashcardList.get(0).getSound());
        }
        if (id == R.id.item_album_delete) {
            db.deleteAlbum(String.valueOf(albumId));
            Intent intent = new Intent(getApplicationContext(), FlashcardListActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "1");
            startActivity(intent);
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
