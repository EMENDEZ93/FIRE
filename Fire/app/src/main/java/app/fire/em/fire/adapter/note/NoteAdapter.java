package app.fire.em.fire.adapter.note;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.fire.em.fire.R;
import app.fire.em.fire.datasource.note.impl.NoteDatasource;
import app.fire.em.fire.domain.note.NoteDomain;
import app.fire.em.fire.model.note.Note;
import app.fire.em.fire.view.note.NoteActivity;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private static List<Note> noteList;
    private Context context;
    private NoteActivity noteActivity;

    public NoteAdapter(Context context, List<Note> notes, NoteActivity noteActivity){
        this.context = context;
        this.noteList = notes;
        this.noteActivity = noteActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_adapter, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.id.setText(note.getId());
        holder.startDate.setText(note.getStartDate());
        holder.endDate.setText(note.getEndDate());
        holder.endTime.setText(note.getEndTime());
        holder.description.setText(note.getDescription());
        holder.user.setText(note.getUser());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView id;
        public TextView startDate;
        public TextView endDate;
        public TextView endTime;
        public TextView description;
        public TextView user;

        public ViewHolder(final View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.note_id);
            startDate = (TextView) itemView.findViewById(R.id.start_date);
            endDate = (TextView) itemView.findViewById(R.id.end_date);
            endTime = (TextView) itemView.findViewById(R.id.end_time);
            description = (TextView) itemView.findViewById(R.id.description);
            user = (TextView) itemView.findViewById(R.id.user);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                Context context;

                @Override
                public boolean onLongClick(final View view) {

                    context = view.getContext();
                    //id = view.getTag().toString();

                    final CharSequence[] items = { "Edit", "Delete" };

                    new AlertDialog.Builder(context).setTitle("Student Record")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {

                                    if(item == 0){
                                        editRecord(item, context);
                                    }

                                    if(item ==1){
                                        Toast.makeText(view.getContext(), "eliminando " + id.getText().toString(), Toast.LENGTH_LONG).show();
                                    }

                                    dialog.dismiss();
                                }
                            }).show();

                    return false;
                }
            });

        }
    }

    static void editRecord(final int studentId, Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.note_form, null, false);

        final EditText editTextStudentFirstname =
                (EditText) formElementsView.findViewById(R.id.descriptionEditText);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Record")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();


    }

}
