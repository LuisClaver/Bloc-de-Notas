package com.example.practicaonce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class NoteEditorActivity extends AppCompatActivity {

    private EditText noteEditText;
    private int currentNotePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.topAppBarEditor);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        noteEditText = findViewById(R.id.noteEditText);


        Intent intent = getIntent();
        if (intent.hasExtra("note_key") && intent.hasExtra("note_position_key")) {
            String noteText = intent.getStringExtra("note_key");
            currentNotePosition = intent.getIntExtra("note_position_key", -1);
            noteEditText.setText(noteText);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                String noteText = noteEditText.getText().toString();

                if (!noteText.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("note_key", noteText);

                    if (currentNotePosition != -1) {
                        resultIntent.putExtra("note_position_key", currentNotePosition);
                    }
                    setResult(RESULT_OK, resultIntent);
                } else {
                    setResult(RESULT_CANCELED);
                }

                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}