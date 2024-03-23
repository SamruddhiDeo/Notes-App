package com.example.notesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerNotesAdapter extends RecyclerView.Adapter<RecyclerNotesAdapter.ViewHolder> {
    Context context;
    ArrayList<NotesModel> arrNotes;
    MyDBHelper dbHelper;
    RecyclerNotesAdapter(Context context,ArrayList<NotesModel> arrNotes){
        this.context= context;
        this.arrNotes = arrNotes;
        dbHelper = new MyDBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.noteTitle.setText(arrNotes.get(position).title);
        holder.noteDescription.setText(arrNotes.get(position).description);

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog editNoteDialog = new Dialog(context);
                editNoteDialog.setContentView(R.layout.add_note_layout);

                Window window = editNoteDialog.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }

                EditText addTitle = editNoteDialog.findViewById(R.id.addTitle);
                EditText addDescription = editNoteDialog.findViewById(R.id.addDescription);
                TextView headingTitle = editNoteDialog.findViewById(R.id.headingTitle);
                TextView headingDescription = editNoteDialog.findViewById(R.id.headingDescription);
                AppCompatButton editNote = editNoteDialog.findViewById(R.id.actionBtn);

                headingTitle.setText("Edit Title");
                headingDescription.setText("Edit Description");
                editNote.setText("Edit Note");
                addTitle.setText(arrNotes.get(position).title);
                addDescription.setText(arrNotes.get(position).description);

                editNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = addTitle.getText().toString();
                        String description = addDescription.getText().toString();
                        dbHelper.editNote(arrNotes.get(position).id,title,description);
                        notifyItemChanged(position);
                        arrNotes=dbHelper.fetchNotes();
                        editNoteDialog.dismiss();
                    }
                });
                editNoteDialog.show();
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                deleteDialog.setIcon(R.drawable.delete);
                deleteDialog.setTitle("Delete Note?")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteNote(arrNotes.get(position).id);
                                arrNotes.remove(position);
                                notifyItemRemoved(position);
                                MainActivity.checkEmpty();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                deleteDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle,noteDescription;
        ImageButton editBtn,deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteDescription = itemView.findViewById(R.id.noteDescription);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);


        }
    }

}
