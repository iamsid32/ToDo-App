package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.db.AppDatabase;
import com.example.todoapp.db.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
//    public static List<String> list;
    public static List<User> list1;
    Adapter adapter;
    private CardView card_view_add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recycler_view);
        card_view_add_button = findViewById(R.id.card_view_add_button);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.todoapp", Context.MODE_PRIVATE);
//        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("addNote", null);

        AppDatabase db = AppDatabase.getDbInstance(getApplicationContext());
        List<User> userList = db.userDao().getAllUsers();

        if (userList != null){
            list1 = new ArrayList<>(userList);
        } else {
            list1 = new ArrayList<>();
        }

        adapter = new Adapter(list1,this);
        recyclerView.setAdapter(adapter);
        layoutAnimation(recyclerView);
        adapter.notifyDataSetChanged();

        card_view_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog add_task_dialog = new Dialog(MainActivity.this);
                add_task_dialog.setContentView(R.layout.add_task_dialog);
                add_task_dialog.setCanceledOnTouchOutside(false);
                add_task_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams layout_params = new WindowManager.LayoutParams();
                layout_params.copyFrom(add_task_dialog.getWindow().getAttributes());
                layout_params.width = WindowManager.LayoutParams.MATCH_PARENT;
                layout_params.height = WindowManager.LayoutParams.MATCH_PARENT;
                add_task_dialog.getWindow().setAttributes(layout_params);
                EditText et_add_here = add_task_dialog.findViewById(R.id.et_add_here);
                TextView tv_add_to_list = add_task_dialog.findViewById(R.id.tv_add_to_list);
                add_task_dialog.show();
                tv_add_to_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!et_add_here.getText().toString().isEmpty()){
//                            list.add(String.valueOf(et_add_here.getText()));
//                            adapter.notifyItemInserted(list.size()-1);
//                            adapter.notifyDataSetChanged();

//                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.todoapp", Context.MODE_PRIVATE);
//                            HashSet<String> set = new HashSet(list);
//                            sharedPreferences.edit().putStringSet("addNote", set).apply();

                            AppDatabase db = AppDatabase.getDbInstance(getApplicationContext());
                            User user = new User();
                            user.firstName = et_add_here.getText().toString();
                            db.userDao().insertUser(user);
                            list1.add(user);
                            adapter.notifyItemInserted(list1.size()-1);
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(MainActivity.this, "Can't create empty events!", Toast.LENGTH_SHORT).show();
                        }
                        add_task_dialog.dismiss();
                    }
                });
            }
        });
    }

    private void layoutAnimation(RecyclerView recyclerView){
        Context context = recyclerView.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}