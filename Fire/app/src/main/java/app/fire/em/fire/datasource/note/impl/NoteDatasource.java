package app.fire.em.fire.datasource.note.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.List;

import app.fire.em.fire.datasource.note.INoteRemoteDatasource;
import app.fire.em.fire.model.note.Note;

public class NoteDatasource implements INoteRemoteDatasource {

    private final static String GET_NOTES_URL = "http://fire-backend.herokuapp.com/notes";
    private final static String SAVE_NOTE_URL = "http://fire-backend.herokuapp.com/note/save";
    private final static String DELETE_NOTE_URL = "http://fire-backend.herokuapp.com/note/";

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

    @Override
    public void saveNote(final PostNoteCallback callback, final Note note) {

        try{

            StringRequest postResquest = new StringRequest(
                    Request.Method.POST,
                    SAVE_NOTE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            callback.onError();
                        }
                    }

            ) {
                @Override
                public String getBodyContentType(){
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(note);
                    byte[] body = new byte[0];
                    try{
                        body = json.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        body = new byte[0];
                    }
                    return body;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response){
                    String responseString = "";
                    if(response != null){
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString,
                            HttpHeaderParser.parseCacheHeaders(response));
                }

            };

            requestQueue.add(postResquest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteNote(final DeleteNoteCallback callback, String id) {


        StringRequest deleteResquest = new StringRequest(
                Request.Method.DELETE,
                DELETE_NOTE_URL + id + "/delete",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {}
                    }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        callback.onError();
                    }
        });
        requestQueue.add(deleteResquest);


    }

}
