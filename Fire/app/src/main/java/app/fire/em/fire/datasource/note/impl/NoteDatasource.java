package app.fire.em.fire.datasource.note.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.List;

import app.fire.em.fire.datasource.note.INoteRemoteDatasource;
import app.fire.em.fire.model.note.Note;

public class NoteDatasource implements INoteRemoteDatasource {

    private final static String GET_NOTES_URL = "http://fire-backend.herokuapp.com/notes";
    private Context context;
    private RequestQueue requestQueue;
    private static INoteRemoteDatasource INSTANCE = null;

    private NoteDatasource(Context context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static INoteRemoteDatasource getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new NoteDatasource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getNotes(final GetNotesCallback callback) {

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                GET_NOTES_URL,
                null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try{

                            Gson gson = new Gson();
                            List<Note> notes = (List<Note>) gson.fromJson(response.toString(),
                            new TypeToken<List<Note>>(){}.getType());
                            callback.onNotesLoader(notes);

                        } catch (Exception e){

                            callback.onError();

                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        callback.onError();

                    }

        });

        requestQueue.add(request);

    }
}
