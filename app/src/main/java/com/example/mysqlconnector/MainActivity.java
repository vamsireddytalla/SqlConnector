package com.example.mysqlconnector;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mysqlconnector.databinding.ActivityMainBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private static final String url = "jdbc:mysql://192.168.119.114:3306/sample_db?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String user = "root";
    private static final String pass = "Ismav@143";
    private Connection con;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectMySql connectMySql = new ConnectMySql();
                connectMySql.execute("");
            }
        });

        binding.getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getData();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }


    private class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, user, pass);
                if (!con.isClosed()){
                    System.out.println("Databaseection success");
                    res = "connection success";
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            binding.tv1.setText(result);
        }
    }

    private void getData(){
       try {
           String selectTable = "SELECT * FROM products;";
           Statement stmt = con.createStatement();
           ResultSet result = stmt.executeQuery(selectTable);
           while(result.next()){
               int id = result.getInt("id");
               String name = result.getString("name");
               String email = result.getString("email");
               Log.d(TAG, "getData: "+id+" "+name+" "+email);
               //Do something with the data
           }
           result.close();
           stmt.close();
           con.close();
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           binding.tv1.setText("Closed connection");
       }
    }


}