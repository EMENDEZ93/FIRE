package app.fire.em.fire.datasource.note;

import app.fire.em.fire.model.note.Note;
import java.util.List;

public interface INoteRemoteDatasource {

    void getNotes(GetNotesCallback callback);

    public interface GetNotesCallback{
        void onNotesLoader(List<Note> notes);
        void onError();
    }

}
