package app.fire.em.fire.domain.note;

import android.view.View;

import java.util.List;

import app.fire.em.fire.R;
import app.fire.em.fire.datasource.note.INoteRemoteDatasource;
import app.fire.em.fire.model.note.Note;

public class NoteDomain {

    private View view;
    private INoteRemoteDatasource iNoteRemoteDatasource;

    public NoteDomain(View view, INoteRemoteDatasource iNoteRemoteDatasource){
        this.view = view;
        this.iNoteRemoteDatasource = iNoteRemoteDatasource;
    }


    public void queryNotes(){
        iNoteRemoteDatasource.getNotes(new INoteRemoteDatasource.GetNotesCallback() {
            @Override
            public void onNotesLoader(List<Note> notes) {
                view.showNotes(notes);
            }

            @Override
            public void onError() {
                view.showMessage(R.string.generic_error);
            }
        });
    }


    public void saveNote(Note note){
        iNoteRemoteDatasource.saveNote(new INoteRemoteDatasource.PostNoteCallback() {
            @Override
            public void saveNote() {
                view.showMessage(R.string.successful_message);
            }

            @Override
            public void onError() {
                view.showMessage(R.string.generic_error);
            }
        }, note);
    }


    public void deleteNote(String id){

    }

    public interface  View {
        void showNotes(List<Note> notes);
        void showMessage(int message);
    }

}
