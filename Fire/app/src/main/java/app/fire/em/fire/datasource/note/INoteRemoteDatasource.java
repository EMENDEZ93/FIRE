package app.fire.em.fire.datasource.note;

import app.fire.em.fire.model.note.Note;
import java.util.List;

public interface INoteRemoteDatasource {

    void getNotes(GetNotesCallback callback);

    void saveNote(PostNoteCallback callback, Note note);

    void deleteNote(DeleteNoteCallback deleteNoteCallback, String id);

    interface GetNotesCallback{
        void onNotesLoader(List<Note> notes);
        void onError();
    }

    interface PostNoteCallback{
        void saveNote();
        void onError();
    }

    interface DeleteNoteCallback{
        void onDeleteNote(String id);
        void onError();
    }

}
