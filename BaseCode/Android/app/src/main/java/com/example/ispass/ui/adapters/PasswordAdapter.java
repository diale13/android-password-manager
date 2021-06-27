package com.example.ispass.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ispass.R;
import com.example.ispass.models.entities.Password;
import com.example.ispass.ui.UpdatePasswordActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private List<Password> passwordList;

    public PasswordAdapter(Context context, Activity activity, List<Password> passwordList){
        this.context = context;
        this.activity = activity;
        this.passwordList = passwordList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_password_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView title, password;
        RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.rec_title);
            password = itemView.findViewById(R.id.rec_password);
            layout = itemView.findViewById(R.id.password_layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(passwordList.get(position).name);
        holder.password.setText(passwordList.get(position).password);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdatePasswordActivity.class);
                intent.putExtra("password_name", passwordList.get(position).name);
                intent.putExtra("password", passwordList.get(position).password);
                intent.putExtra("user_id", passwordList.get(position).user_identifier);
                intent.putExtra("password_id", passwordList.get(position).id);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public List<Password> getList() {
        return passwordList;
    }

    public void removeItem (int position) {
        passwordList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Password item, int position){
        passwordList.add(position, item);
        notifyItemInserted(position);
    }
}
