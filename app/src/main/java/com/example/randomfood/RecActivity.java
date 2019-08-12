package com.example.randomfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);

        MyDBHelper db = new MyDBHelper(this);
        Cursor cursor = db.getReadableDatabase().query("food",null,null,
                null,null,null,null);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecAdapter adapter = new RecAdapter(this, cursor);
        recyclerView.setAdapter(adapter);

    }

    class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder>{
        private Context context;
        private Cursor cursor;

        public RecAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @NonNull
        @Override
        public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new RecViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
            cursor.moveToPosition(position);
            holder.foodWord.setText(cursor.getString(cursor.getColumnIndex("foodWords")));
        }

        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        class RecViewHolder extends RecyclerView.ViewHolder {
            TextView foodWord;

            public RecViewHolder(@NonNull View itemView) {
                super(itemView);

                foodWord = itemView.findViewById(android.R.id.text1);

            }
        }
    }
}
