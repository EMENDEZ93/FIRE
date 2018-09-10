package app.fire.em.fire.view.note;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import app.fire.em.fire.R;
import app.fire.em.fire.adapter.note.NoteAdapter;
import app.fire.em.fire.datasource.note.impl.NoteDatasource;
import app.fire.em.fire.domain.note.NoteDomain;
import app.fire.em.fire.model.note.Note;

public class NoteActivity extends AppCompatActivity implements NoteDomain.View {

    private NoteAdapter noteAdapter;
    private NoteDomain noteDomain;
    private RecyclerView notes_list;
    private Button create_note_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        notes_list = (RecyclerView) findViewById(R.id.notes_list);
        noteAdapter = new NoteAdapter(getApplicationContext(), null, this);

        noteDomain = new NoteDomain(this, NoteDatasource.getInstance(this));
        noteDomain.queryNotes();
        deleteNote("16");
        queryNotes();

        create_note_button = (Button) findViewById(R.id.createNoteButton);

        create_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getRootView().getContext();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.note_form, null, false);


                final EditText description = (EditText) formElementsView.findViewById(R.id.descriptionEditText);

                new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle("Create Notes")
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        saveNote(description.getText().toString());

                                        Toast.makeText(getApplicationContext()
                                                , description.getText().toString(), Toast.LENGTH_LONG).show();
                                        dialog.cancel();

                                        finish();
                                        startActivity(getIntent());

                                    }

                                }).show();


            }
        });



    }


    private void saveNote(String descripcion){
        Note note = new Note();
        note.setEndDate("2050-06-01");
        note.setDescription(descripcion);
        noteDomain.saveNote(note);
    }

    private void queryNotes(){
        noteDomain = new NoteDomain(this, NoteDatasource.getInstance(this));
        noteDomain.queryNotes();
    }

    @Override
    public void showNotes(List<Note> notes) {
        noteAdapter = new NoteAdapter(getApplicationContext(), notes, this);
        notes_list.setAdapter(noteAdapter);
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(getApplicationContext(),
                "***** ERROR *****" + getString(message), Toast.LENGTH_LONG).show();
    }

    public void deleteNote(String id){
        noteDomain = new NoteDomain(this, NoteDatasource.getInstance(this));
        noteDomain.deleteNote(id);
    }


    public void restartActivity(){
        finish();
        startActivity(getIntent());
    }

}
