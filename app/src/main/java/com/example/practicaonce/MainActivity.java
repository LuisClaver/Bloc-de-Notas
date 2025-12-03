package com.example.practicaonce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteListener {

    public static final String PREFERENCES_FILE_NAME = "MyNotesPreferences";
    public static final String NOTES_SET_KEY = "notesSet";

    private List<String> notesList;
    private NoteAdapter adapter;
    private RecyclerView recyclerView;


    private final ActivityResultLauncher<Intent> noteEditorLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String updatedNote = result.getData().getStringExtra("note_key");
                    int position = result.getData().getIntExtra("note_position_key", -1);

                    if (updatedNote != null && !updatedNote.isEmpty()) {
                        if (position == -1) {
                            notesList.add(updatedNote);
                            adapter.notifyItemInserted(notesList.size() - 1);
                        } else {
                            notesList.set(position, updatedNote);
                            adapter.notifyItemChanged(position);
                        }
                        saveNotes();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.topAppBarMain);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.notesRecyclerView);
        FloatingActionButton fab = findViewById(R.id.fabAddNote);

        loadNotes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(notesList, this);
        recyclerView.setAdapter(adapter);


        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
            noteEditorLauncher.launch(intent);
        });
    }



    @Override
    public void onDeleteClick(int position) {
        notesList.remove(position);
        adapter.notifyItemRemoved(position);
        saveNotes();
    }


    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);

        intent.putExtra("note_key", notesList.get(position));
        intent.putExtra("note_position_key", position);
        noteEditorLauncher.launch(intent);
    }


    private void saveNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> notesSet = new HashSet<>(notesList);
        editor.putStringSet(NOTES_SET_KEY, notesSet);
        editor.apply();
    }

    private void loadNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        Set<String> notesSet = sharedPreferences.getStringSet(NOTES_SET_KEY, new HashSet<>());
        notesList = new ArrayList<>(notesSet);
    }
}