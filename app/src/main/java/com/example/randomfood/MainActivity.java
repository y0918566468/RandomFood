package com.example.randomfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //元件
    private EditText edInputFood;
    private Button btAdd;

    //資料庫
    private MyDBHelper db;

    //管理鍵盤
    private InputMethodManager imm;

    //Spinner使用
    private Spinner spFood;
    private int m_FoodPosition = 0;

    //隨機產生用
    private TextView tvRandomFood;
    private Button btRandomFood;
    private List<FoodData> randomLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //管理鍵盤
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        findViewId();

        //不使用了
        //edInputFood.setFilters(new InputFilter[]{new SpaceFilter()});
        db = new MyDBHelper(this);

        List<String> labels = db.getFoodLabels();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFood.setAdapter(dataAdapter);
        spFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_FoodPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void findViewId(){
        edInputFood = findViewById(R.id.ed_inputFood);
        btAdd = findViewById(R.id.bt_add);
        spFood = findViewById(R.id.sp_food);
        tvRandomFood = findViewById(R.id.tv_randomFood);
        btRandomFood = findViewById(R.id.bt_randomFood);
    }

    private void getSQLiteData() {
        List<String> foodLabels = db.getFoodLabels();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, foodLabels);
        spFood.setAdapter(dataAdapter);

    }

    public void addFood(View view) {
        String s = edInputFood.getText().toString();
        s.trim();

        if (s.startsWith(" ", 0) || s.isEmpty()) {
            Toast.makeText(this, "開頭請勿空白", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (s.indexOf(" ") == 0) {
//            Toast.makeText(this, "開頭請勿空白", Toast.LENGTH_SHORT).show();
//            return;
//        }

        ContentValues values = new ContentValues();
        values.put("foodWords", s);

        long id = db.getWritableDatabase().insert("food", null, values);

        if (id < 0) {
            new AlertDialog.Builder(this)
                    .setTitle("錯誤")
                    .setMessage("可能儲存到相同名稱")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            edInputFood.setText("");
            Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
        }

        getSQLiteData();

    }

    public void delFood(View view) {
        if (db.getDb_IsNull() == 0) {
            Toast.makeText(this, "資料庫已經是空的了", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> foodLabels = db.getFoodLabels();
        String s = foodLabels.get(m_FoodPosition);

        if (db.deleteData(s) == 0) {
            Toast.makeText(this, "刪除失敗", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "刪除成功", Toast.LENGTH_SHORT).show();
        }

        getSQLiteData();

    }

    class DrawingTask extends AsyncTask<Integer, Integer, Integer> {
        boolean stop = false;
        Random random = new Random();

        @Override
        protected Integer doInBackground(Integer... integers) {
            int duration = integers[0];
            int current = -1;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    stop = true;
                }
            }, duration * 1000);
            while (!stop) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                current = random.nextInt(randomLabels.size());
                publishProgress(current);
            }

            return current;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            tvRandomFood.setText("決定是 " + randomLabels.get(integer).getFoodWords() + "");
            btRandomFood.setEnabled(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int index = values[0];
            tvRandomFood.setText(randomLabels.get(index).getFoodWords() + "");
        }
    }

    public void randomFood(View view) {
        randomLabels = db.getFoddData();

        if (randomLabels.isEmpty()) {
            Toast.makeText(this, "沒有儲存資料無法產生", Toast.LENGTH_SHORT).show();
            return;
        }

        //new DrawingTask().execute(4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new DrawingTask().execute(4);

        btRandomFood.setEnabled(false);

    }

    public void goView(View view) {
        Intent intent = new Intent(this, RecActivity.class);
        startActivity(intent);

    }

    //處理點擊空白處收鍵盤
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
}

//不使用了
//處理鍵盤輸入空白鍵無作用
//class SpaceFilter implements InputFilter {
//
//    @Override
//    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
//        if (charSequence.equals(" "))
//            return " ";
//        return null;
//    }
//}