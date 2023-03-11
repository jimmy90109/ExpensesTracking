package com.example.expensestracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    static final String TB_NAME="expenses";
    SQLiteDatabase db;
    StdDBHelper dbHelper;
    Cursor cur;
    SimpleCursorAdapter adapter;
    int year=2022;
    int month=12;
    int positive,negative;

    static final String[] FROM=new String[] {"date","detail","money"};

    ListView lv;
    TextView textDate,txtPos,txtNeg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        txtPos = (TextView) findViewById(R.id.txtPos);
        txtNeg = (TextView) findViewById(R.id.txtNeg);
        textDate = (TextView) findViewById(R.id.textDate);

        dbHelper = new StdDBHelper(this);
        db = dbHelper.getWritableDatabase();

        adapter=new SimpleCursorAdapter(this,
                R.layout.item, cur,
                FROM,
                new int[] {R.id.date,R.id.detail,R.id.money}, 0);

        lv=findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        requery();
    }

    private void requery() {
        cur=db.rawQuery("SELECT * FROM expenses WHERE yyyymm = '"+textDate.getText().toString()+"' ORDER BY date", null);
        adapter.changeCursor(cur);
        if(cur.moveToFirst()){
            lv.setVisibility(View.VISIBLE);
            positive = 0;
            negative = 0;
            do{
                if(cur.getInt(4)>0) {
                    positive += cur.getInt(4);
                }
                else{
                    negative += cur.getInt(4);
                }
            }while (cur.moveToNext());

            txtPos.setText("$ +"+positive);
            if(negative==0)
                txtNeg.setText("$ -0");
            else
                txtNeg.setText("$ "+negative);
        }
        else{
            txtPos.setText("$ +0");
            txtNeg.setText("$ -0");
            lv.setVisibility(View.GONE);
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        cur.moveToPosition(position);

        new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("Delete")
                .setMessage("Do you want to delete this item?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.delete(TB_NAME, "_id="+cur.getInt(0),null);
                        requery();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(getApplicationContext(), "Resume",
                        Toast.LENGTH_SHORT)
                .show();
        requery();
    }
    @Override
    protected void onStop(){
        super.onStop();
        Toast.makeText(getApplicationContext(), "Stop",
                        Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "Pause",
                        Toast.LENGTH_SHORT)
                .show();
    }


    public void plusButton(View view){
        Intent intent = new Intent(this, NewData.class);
        startActivity(intent);
    }
    public void nextMonth(View view){
        if(month == 12){
            year += 1;
            month = 1;
        }else
            month ++;

        if(month<10)
            textDate.setText(year+"/0"+month);
        else
            textDate.setText(year+"/"+month);

        requery();
    }
    public void previousMonth(View view){
        if(month == 1){
            year -= 1;
            month = 12;
        }
        else
            month--;

        if(month<10)
            textDate.setText(year+"/0"+month);
        else
            textDate.setText(year+"/"+month);

        requery();
    }


}