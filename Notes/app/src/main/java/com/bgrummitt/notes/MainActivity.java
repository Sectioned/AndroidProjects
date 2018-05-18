package com.bgrummitt.notes;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private List<Note> notes = new ArrayList<>();
    private ListAdapter mListAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView =  findViewById(R.id.list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });

        mListAdapter = new ListAdapter(this, notes);
        listView.setAdapter(mListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newNote(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_note, null);

        final EditText mSubject = mView.findViewById(R.id.subjectEditText);
        final EditText mNotes = mView.findViewById(R.id.mainNotesEditText);
        Button mSaveButton = mView.findViewById(R.id.saveButton);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSubject.getText().toString().isEmpty() && !mNotes.getText().toString().isEmpty()){
                    makeNewNote(mSubject.getText().toString(), mNotes.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void makeNewNote(String subject, String note){
        refreshListAdapter();

        notes.add(new Note(subject, note));

        mListAdapter = new ListAdapter(this, notes);
        listView.setAdapter(mListAdapter);
    }

    public void refreshListAdapter(){
        notes = mListAdapter.getmNotes();
        mListAdapter = new ListAdapter(this, notes);
        listView.setAdapter(mListAdapter);
    }

}