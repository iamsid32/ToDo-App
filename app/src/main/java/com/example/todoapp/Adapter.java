package com.example.todoapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.db.AppDatabase;
import com.example.todoapp.db.User;

import java.util.HashSet;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {

    private List<User> tasks_list;
    private Context context;
    private AppDatabase db = AppDatabase.getDbInstance(context);

    public Adapter(List<User> tasks_list, Context context) {
        this.tasks_list = tasks_list;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.AdapterViewHolder holder, int position) {
        User user1 = tasks_list.get(position);
        String task = user1.firstName;
        holder.tv_task.setText(task);
        holder.tv_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog show_task_dialog = new Dialog(context);
                show_task_dialog.setContentView(R.layout.task_description);
                show_task_dialog.setCanceledOnTouchOutside(false);
                show_task_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams layout_params = new WindowManager.LayoutParams();
                layout_params.copyFrom(show_task_dialog.getWindow().getAttributes());
                layout_params.width = WindowManager.LayoutParams.MATCH_PARENT;
                layout_params.height = WindowManager.LayoutParams.MATCH_PARENT;
                show_task_dialog.getWindow().setAttributes(layout_params);
                TextView text_description = show_task_dialog.findViewById(R.id.text_description);
                text_description.setText(task);
                show_task_dialog.show();
            }
        });
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog edit_dialog = new Dialog(context);
                edit_dialog.setContentView(R.layout.edit_dialog);
                edit_dialog.setCanceledOnTouchOutside(false);
                edit_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams layout_params = new WindowManager.LayoutParams();
                layout_params.copyFrom(edit_dialog.getWindow().getAttributes());
                layout_params.width = WindowManager.LayoutParams.MATCH_PARENT;
                layout_params.height = WindowManager.LayoutParams.MATCH_PARENT;
                edit_dialog.getWindow().setAttributes(layout_params);
                EditText et_edit_here = edit_dialog.findViewById(R.id.et_edit_here);
                et_edit_here.setText(holder.tv_task.getText());
                TextView tv_edit = edit_dialog.findViewById(R.id.tv_edit);
                edit_dialog.show();
                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_edit_here.getText().toString().isEmpty()){
                            Toast.makeText(context, "Can't create empty events!", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.tv_task.setText(et_edit_here.getText());
                            User user = new User();
                            user.firstName = et_edit_here.getText().toString();
                            tasks_list.set(position, user);
                            db.userDao().updateUser(user);
                            notifyItemChanged(position);
//                            SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.todoapp", Context.MODE_PRIVATE);
//                            HashSet<String> set = new HashSet(tasks_list);
//                            sharedPreferences.edit().putStringSet("addNote", set).apply();
                            edit_dialog.dismiss();
                        }
                    }
                });
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = tasks_list.get(position);
                db.userDao().delete(user);
                tasks_list.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
//                SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.todoapp", Context.MODE_PRIVATE);
//                HashSet<String> set = new HashSet(tasks_list);
//                sharedPreferences.edit().putStringSet("addNote", set).apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks_list.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tv_task;
        ImageView iv_edit, iv_delete;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_task = itemView.findViewById(R.id.tv_task);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
