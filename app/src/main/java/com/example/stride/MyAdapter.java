package com.example.stride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import static com.example.stride.R.layout.row;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<String> list;
    Context context;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String[] results = (list.get(position).toString()).split(",");
        holder.titleTextView.setText(results[0]);
        holder.descriptionTextView.setText(results[1]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView titleTextView;
        public TextView descriptionTextView;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            titleTextView = (TextView) itemView.findViewById(R.id.titleEntry);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionEntry);

            itemView.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context,
                    "You have clicked " + ((TextView)view.findViewById(R.id.titleEntry)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}

