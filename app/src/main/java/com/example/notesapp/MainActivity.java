package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.example.notesapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ActivityMainBinding binding;
    static ArrayList<NotesModel> arrNotes;
    RecyclerNotesAdapter recyclerNotesAdapter;
    static boolean isNotesEmpty=true;
    MyDBHelper dbHelper = new MyDBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notifyChangeToRecyclerView();
        checkEmpty();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog addNoteDialog = new Dialog(MainActivity.this);
                addNoteDialog.setContentView(R.layout.add_note_layout);

                Window window = addNoteDialog.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }

                EditText addTitle = addNoteDialog.findViewById(R.id.addTitle);
                EditText addDescription = addNoteDialog.findViewById(R.id.addDescription);
                AppCompatButton addNote = addNoteDialog.findViewById(R.id.actionBtn);

                addNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        isNotesEmpty=false;
                        String title = addTitle.getText().toString();
                        String description = addDescription.getText().toString();
                        dbHelper.addNote(title,description);
                        notifyChangeToRecyclerView();
                        recyclerNotesAdapter.notifyItemInserted(arrNotes.size()-1);
                        binding.notesListRecyclerView.scrollToPosition(arrNotes.size()-1);
                        addNoteDialog.dismiss();
                        checkEmpty();
                    }
                });
                addNoteDialog.show();

            }
        });
    }

    public static void checkEmpty() {
        if(arrNotes.isEmpty()){
            isNotesEmpty=true;
        }else{
            isNotesEmpty = false;
        }
        if(isNotesEmpty){
            binding.emptyImage.setVisibility(View.VISIBLE);
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.arrow.setVisibility(View.VISIBLE);
        } else {
            binding.emptyImage.setVisibility(View.INVISIBLE);
            binding.emptyTxt.setVisibility(View.INVISIBLE);
            binding.arrow.setVisibility(View.INVISIBLE);
        }
    }

    public void notifyChangeToRecyclerView(){
        arrNotes = dbHelper.fetchNotes();
        binding.notesListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotesAdapter = new RecyclerNotesAdapter(this,arrNotes);
        binding.notesListRecyclerView.setAdapter(recyclerNotesAdapter);
    }


}