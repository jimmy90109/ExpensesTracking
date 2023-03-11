package com.example.expensestracking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewData extends AppCompatActivity {
    static final String TB_NAME="expenses";
    SQLiteDatabase db;
    StdDBHelper dbHelper;

    DatePickerDialog.OnDateSetListener datePicker;
    Calendar calendar;

    EditText editDate, editDetail, editMoney;
    TextView textWarning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        dbHelper = new StdDBHelper(this);
        db = dbHelper.getWritableDatabase();

        editDate = (EditText) findViewById(R.id.editTextDate);
        editDetail = (EditText) findViewById(R.id.editTextDetail);
        editMoney = (EditText) findViewById(R.id.editTextExpense);


        calendar = Calendar.getInstance();
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH , i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
                editDate.setText(sdf.format(calendar.getTime()));
            }
        };
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(NewData.this,
                        datePicker,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        textWarning = (TextView) findViewById(R.id.textViewWarning);
    }

    @Override
    protected void onStop(){
        super.onStop();
        Toast.makeText(getApplicationContext(), "STOP",
                        Toast.LENGTH_SHORT)
                .show();
        //db.close();
    }

    public void addData(View view){
        textWarning.setVisibility(View.INVISIBLE);
        if(!editDate.getText().toString().equals("") &&!editDetail.getText().toString().equals("") && !editMoney.getText().toString().equals("")){

            ContentValues cv = new ContentValues();
            cv.put("yyyymm", editDate.getText().toString().substring(0,7));
            cv.put("date", editDate.getText().toString().substring(8));
            cv.put("detail", editDetail.getText().toString());
            cv.put("money", Integer.parseInt(editMoney.getText().toString()));
            db.insert(TB_NAME, null, cv);

            finish();
        }
        else{
            textWarning.setVisibility(View.VISIBLE);
        }
    }
    public void returnToMain(View view){
        finish();
    }
}