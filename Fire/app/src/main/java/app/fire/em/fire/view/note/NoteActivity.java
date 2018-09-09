package app.fire.em.fire.view.note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        notes_list = (RecyclerView) findViewById(R.id.notes_list);
        noteAdapter = new NoteAdapter(getApplicationContext(), null, this);

        noteDomain = new NoteDomain(this, NoteDatasource.getInstance(this));
        noteDomain.queryNotes();
        queryNotes();

        saveNote();
    }


    private void saveNote(){
        Note note = new Note();
        note.setEndDate("2050-06-01");
        note.setDescription("*** ANDRIOD NOTE ****");
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
}
