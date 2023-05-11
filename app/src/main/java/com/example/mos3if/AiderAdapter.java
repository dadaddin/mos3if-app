package com.example.mos3if;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AiderAdapter extends RecyclerView.Adapter<AiderAdapter.MyViewHolder>  {
    private Context context;
    private ArrayList<Contact> aiders;
    private AiderAdapter.OnItemClickListener listener;

    public AiderAdapter(Context context, ArrayList<Contact> aiders, AiderAdapter.OnItemClickListener listener) {
        this.context = context;
        this.aiders = aiders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AiderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_aider, parent, false);
        return new AiderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AiderAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Contact aider = aiders.get(position);

        holder.tv_name.setText(String.valueOf(aider.getName()));
        holder.tv_phone.setText(String.valueOf(aider.getPhone()));

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCallClick(aider);
            }
        });



    }

    @Override
    public int getItemCount() {
        return aiders.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_phone;
        private RelativeLayout callButton;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            callButton = itemView.findViewById(R.id.btn_call);

        }

    }

    public interface OnItemClickListener {
        void onCallClick(Contact contact);
    }

}