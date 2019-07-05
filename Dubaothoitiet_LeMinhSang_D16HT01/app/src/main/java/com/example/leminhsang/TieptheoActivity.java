package com.example.leminhsang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TieptheoActivity extends AppCompatActivity {

    String tenthanhpho ="";
    ImageView imgback;
    TextView txtName;
    ListView lv;
    CustomAdapter customAdapter;
    ArrayList<Thoitiet> mangthoitiet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tieptheo);
        Anhxa();
        Intent intent =getIntent();
        String city =intent.getStringExtra("name");
        Log.d("ketqua","Du lieu truyen qua: "+ city);
        if(city.equals("")){
            tenthanhpho ="Saigon";
            Get7DaysData(tenthanhpho);
        }else {
            tenthanhpho=city;
            Get7DaysData(tenthanhpho);
        }
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private  void Anhxa(){
        imgback =(ImageView) findViewById(R.id.imageviewBack);
        txtName =(TextView) findViewById(R.id.tenthanhpho);
        lv =(ListView) findViewById(R.id.Listview);
        mangthoitiet =new ArrayList<Thoitiet>();
        customAdapter =new CustomAdapter(TieptheoActivity.this,mangthoitiet);
        lv.setAdapter(customAdapter);
    }

    private  void  Get7DaysData(String data){
        String url ="http://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&cnt=7&appid=bf7cb62d6b1718e65a17076096f5547e";
        RequestQueue requestQueue = Volley.newRequestQueue(TieptheoActivity.this);
        StringRequest stringRequest =new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject =new JSONObject(response);
                            JSONArray jsonArrayList =jsonObject.getJSONArray("list");
                            for(int i=0;i<jsonArrayList.length();i++){
                                JSONObject jsonObjectList =jsonArrayList.getJSONObject(i);
                                String ngay =jsonObjectList.getString("dt");

                                long l =Long.valueOf(ngay);
                                Date date =new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                                String Day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp =jsonObjectList.getJSONObject("main");
                                String max =jsonObjectTemp.getString("temp_max");
                                String min =jsonObjectTemp.getString("temp_min");

                                Double a =Double.valueOf(max);
                                Double b =Double.valueOf(min);
                                String NhietdoMax =String.valueOf(a.intValue());
                                String NhietdoMin =String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather =jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");




                                JSONObject jsonObjectCity =jsonObject.getJSONObject("city");
                                String name =jsonObjectCity.getString("name");
                                txtName.setText(name);
                                mangthoitiet.add(new Thoitiet(Day,status,icon,NhietdoMax,NhietdoMin));

                            }
                               customAdapter.notifyDataSetChanged();
                           }catch (JSONException e){
                               e.printStackTrace();
                           }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
           requestQueue.add(stringRequest);
    }
}
