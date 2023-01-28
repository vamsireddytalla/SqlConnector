package com.example.mysqlconnector;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private static final String url = "192.168.0.54:3306";
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

        binding.createTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String createTable = "CREATE TABLE Svik (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50),age INT,email VARCHAR(50))";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(createTable);
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
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                String ConnURL;
                ConnURL = "jdbc:jtds:sqlserver://" + url + ";" + "databaseName=" + "sample_db" + ";user=" + user + ";password=" + pass + ";";
                con = DriverManager.getConnection(ConnURL);
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
           String selectTable = "SELECT * FROM sample_table";
           Statement stmt = con.createStatement();
           ResultSet result = stmt.executeQuery(selectTable);
           while(result.next()){
               int id = result.getInt("id");
               String name = result.getString("name");
               //Do something with the data
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }


}