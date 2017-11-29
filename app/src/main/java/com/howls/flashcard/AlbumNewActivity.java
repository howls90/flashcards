package com.howls.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AlbumNewActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextView name, description;
    MyDBHandle db;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_new);

        setTitle("Create Album");

        name = (EditText)findViewById(R.id.name);
        description = (EditText)findViewById(R.id.description);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.album_new_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_ok:
                String nameS = name.getText().toString();
                String descriptionS = description.getText().toString();

                Album album = new Album(nameS,descriptionS);

                db = new MyDBHandle(this);
                db.addAlbum(album);

            default:
                intent = new Intent(this, AlbumListActivity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
        }
    }
}
