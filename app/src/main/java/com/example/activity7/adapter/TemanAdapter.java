package com.example.activity7.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.activity7.EditTeman;
import com.example.activity7.MainActivity;
import com.example.activity7.R;
import com.example.activity7.app.AppController;
import com.example.activity7.database.Teman;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {
    private ArrayList<Teman> listData;

    public TemanAdapter(ArrayList<Teman> listData) {
        this.listData = listData;
    }

    @Override
    public TemanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInf = LayoutInflater.from(parent.getContext());
        View view = layoutInf.inflate(R.layout.row_data_teman, parent,false);
        return new TemanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemanViewHolder holder, int position) {
        String id,nm, tlp;

        id = listData.get(position).getId();
        nm = listData.get(position).getNama();
        tlp = listData.get(position).getTelpon();

        holder.namaTxt.setTextColor(Color.BLUE);
        holder.namaTxt.setTextSize(20);
        holder.namaTxt.setText(nm);
        holder.telponTxt.setText(tlp);

        holder.cardku.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                PopupMenu pm = new PopupMenu(v.getContext(), v);

                pm.inflate(R.menu.popup1);

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Bundle bundle = new Bundle();
                                bundle.putString("kunci1", id);
                                bundle.putString("kunci2", nm);
                                bundle.putString("kunci3", tlp);

                                Intent intent = new Intent(v.getContext(), EditTeman.class);
                                intent.putExtras(bundle);
                                v.getContext().startActivity(intent);
                                break;

                            case R.id.hapus:
                                AlertDialog.Builder alertdb = new AlertDialog.Builder(v.getContext());
                                alertdb.setTitle("Yakin" + nm + "akan dihapus?");
                                alertdb.setMessage("Tekan Ya untuk menghapus");
                                alertdb.setCancelable(false);
                                alertdb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HapusData(id);
                                        Toast.makeText(v.getContext(), "Data" + id + "telah dihapus", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                                        v.getContext().startActivity(intent);
                                    }
                                });

                                alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog adlg = alertdb.create();
                                adlg.show();

                                break;
                        }
                        return true;
                    }
                });
                pm.show();
                return true;
            }
        });
    }

    private void HapusData(final String idx){
        String url_update = "http://10.0.2.2/umyTI/delete.php";
        final String TAG = MainActivity.class.getSimpleName();
        final String TAG_SUCCESS = "success";
        final int[] sukses = new int[1];

        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>(){

            public void onResponse(String Response) {
                Log.d(TAG, "Respon: "+ response.toString());

                try{
                    JSONObject jObj = new JSONObject(response);
                    sukses[0] = jObj.getInt(TAG_SUCCESS);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }}
    },new Response.ErrorListener(){
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Error: "+error.getMessage());
            }
        })
        {
            protected Map<String,String> getParams(){
                Map<String,String> params =  new HashMap<>();

                params.put("id", idx);

                return params;
                }
            };
        AppController.getInstance().addToRequestQueue(stringReq);
        }

        public int getItemCount(){
            return (listData != null)?listData.size() : =0;    }

            public class TemanViewHolder extends RecyclerView.ViewHolder{
                private  CardView cardku;
                private TextView namaTxt,telponTxt;
                public TemanViewHolder(View view) {
                    super(view);
                    cardku = (CardView) view.findViewById(R.id.kartuku);
                    namaTxt = (TextView) view.findViewById(R.id.textNama);
                    telponTxt = (TextView) view.findViewById(R.id.textTelpon);
                }
            }
}