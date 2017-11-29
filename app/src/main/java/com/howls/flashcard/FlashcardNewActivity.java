package com.howls.flashcard;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class FlashcardNewActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.howls.languagenotes.AlbumId";

    private EditText word, read, translate;
    MyDBHandle db;

    MediaRecorder myAudioRecord;
    private String outputFile = null;
    private String albumId;
    ImageButton play, delete, record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_new);

        setTitle("Create new Flashcard");

        Intent intent = getIntent();
        //albumId = intent.getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);

        record = (ImageButton)findViewById(R.id.record);
        play = (ImageButton)findViewById(R.id.play);
        delete = (ImageButton)findViewById(R.id.delete);

        play.setEnabled(false);
        delete.setEnabled(false);


        record.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    play.setEnabled(true);
                    delete.setEnabled(true);
                    stop();
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.flashcard_new_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_flashcardnew_return:
                Intent intent = new Intent(this, FlashcardListActivity.class);
                //intent.putExtra(EXTRA_MESSAGE, albumId);
                startActivity(intent);
                return true;

            case R.id.item_flashcardnew_ok:
                addFlashcard();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void start() {

        word = (EditText)findViewById(R.id.word);
        String wordS = word.getText().toString();

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+wordS+".3gp";

        myAudioRecord = new MediaRecorder();
        myAudioRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecord.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecord.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecord.setOutputFile(outputFile);

        try {
            myAudioRecord.prepare();
            myAudioRecord.start();
        } catch (IllegalStateException e ) {
            e.printStackTrace();
        } catch (IOException f) {
            f.printStackTrace();
        }

        Toast.makeText(this,"Recording started", Toast.LENGTH_SHORT).show();
    }

    public void stop() {
        myAudioRecord.stop();
        myAudioRecord.release();
        myAudioRecord = null;

        Toast.makeText(this,"Audio Recorded Successfully", Toast.LENGTH_SHORT).show();
    }

    public void play(View v) throws IOException{
        MediaPlayer m = new MediaPlayer();
        m.setDataSource(outputFile);
        m.prepare();
        m.start();

        Toast.makeText(this,"Playing Audio", Toast.LENGTH_SHORT).show();
    }

    public void addFlashcard() {
        db = new MyDBHandle(this);
        word = (EditText)findViewById(R.id.word);
        read = (EditText)findViewById(R.id.read);
        translate = (EditText)findViewById(R.id.translate);

        String wordS = word.getText().toString();
        String readS = read.getText().toString();
        String translateS = translate.getText().toString();

        if (wordS.equals("") || translateS.equals("")) {
            Toast.makeText(this,"Word form and Translate form must be fullfil", Toast.LENGTH_SHORT).show();
        } else {
            Album album = db.getAlbum(albumId);
            String languageS = album.getDescription();

            Flashcard flashcard = new Flashcard(wordS,readS,translateS,languageS, outputFile,albumId);

            db.addFlashcard(flashcard);

            //Intent intent = new Intent(this, FlashcardListActivity.class);
            //intent.putExtra(EXTRA_MESSAGE, albumId);
            //startActivity(intent);
        }


    }
}
