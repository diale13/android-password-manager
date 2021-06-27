package com.example.ispass.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ispass.R;
import com.example.ispass.webapiModels.out.LeakOut;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class LeaksAdapter extends RecyclerView.Adapter<LeaksAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<LeakOut> leaksList;

    public LeaksAdapter(Context context, Activity activity, ArrayList<LeakOut> leaksList) {
        this.context = context;
        this.activity = activity;
        this.leaksList = leaksList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_leaks_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, email, description;
        RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            email = itemView.findViewById(R.id.email);
            layout = itemView.findViewById(R.id.leak_layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.title.setText(leaksList.get(position).LeakedSiteName);
        holder.email.setText("Email: " + leaksList.get(position).AsociatedEmail);
        holder.description.setText("Url: " + leaksList.get(position).Url);
    }

    @Override
    public int getItemCount() {
        return leaksList.size();
    }
}
