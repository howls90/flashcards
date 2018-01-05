package com.howls.flashcard;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class FlashcardNewActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "AlbumId";

    private EditText word, translate, examples, notes;
    private MyDBHandle db = new MyDBHandle(this);;
    private RingProgressBar progress;
    private MediaRecorder myAudioRecord;
    private String outputFile = null;
    private String albumId;
    private ImageButton play, delete, record;
    private boolean is_correct = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_new);

        albumId = getIntent().getStringExtra(FlashcardListActivity.EXTRA_MESSAGE);
        setTitle(db.getAlbum(albumId).getName());

        record = findViewById(R.id.record);
        play = findViewById(R.id.play);
        delete = findViewById(R.id.delete);
        word = findViewById(R.id.word);
        progress = findViewById(R.id.progress);

        play.setEnabled(false);
        delete.setEnabled(false);

        record.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                String wordS = word.getText().toString();
                if (wordS.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Introduce Term/Sentence first!", Toast.LENGTH_SHORT).show();
                } else {
                    start();
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                String wordS = word.getText().toString();
                if (wordS.isEmpty()) {
                } else {
                    play.setEnabled(true);
                    delete.setEnabled(true);
                    stop();
                }
            }
            return true;
            }
        });

        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            File sound = new File(outputFile);
            sound.delete();
            play.setEnabled(false);
            Toast.makeText(getApplicationContext(),"Audio Deleted", Toast.LENGTH_SHORT).show();
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
                intent.putExtra(EXTRA_MESSAGE, albumId);
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
        String wordS = word.getText().toString();

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+wordS+".3gp";

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
        MediaPlayer m;
        m = new MediaPlayer();
        m.setDataSource(outputFile);
        m.prepare();
        final int duration = m.getDuration();
        m.start();

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

    public void addFlashcard() {
        word = findViewById(R.id.word);
        translate = findViewById(R.id.translate);
        examples = findViewById(R.id.examples);
        notes = findViewById(R.id.notes);

        String wordS = word.getText().toString();
        String readS = wordS;
        String examplesS = examples.getText().toString();
        String notesS = notes.getText().toString();

        String translateS = translate.getText().toString();

        if (wordS.equals("") || translateS.equals("") || outputFile == null) {
            Toast.makeText(this,"Word ,Translate and Sound form must be fullfil", Toast.LENGTH_SHORT).show();
        } else {
            Flashcard flashcard = new Flashcard(wordS,readS,translateS, outputFile, albumId);
            flashcard.setNotes(notesS);
            flashcard.setExamples(examplesS);

            db.addFlashcard(flashcard);

            Intent intent = new Intent(this, FlashcardListActivity.class);
            intent.putExtra(EXTRA_MESSAGE, albumId);
            startActivity(intent);
        }
    }
}
