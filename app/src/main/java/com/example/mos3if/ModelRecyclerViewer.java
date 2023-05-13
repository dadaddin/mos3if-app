package com.example.mos3if;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ModelRecyclerViewer extends RecyclerView.Adapter<ModelRecyclerViewer.ViewHolder> {


    Context context;
    ArrayList<Model> arrayList = new ArrayList<>();


    public ModelRecyclerViewer(Context context , ArrayList<Model> arrayList){
        this.context= context;
        this.arrayList= arrayList;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_steps,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.image.setImageResource(arrayList.get(position).getImage());
        holder.heading.setText(arrayList.get(position).getTitle());
        holder.step1.setText(arrayList.get(position).getStep1());
        holder.step2.setText(arrayList.get(position).getStep2());
        holder.step3.setText(arrayList.get(position).getStep3());
        holder.step4.setText(arrayList.get(position).getStep4());
        holder.step5.setText(arrayList.get(position).getStep5());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,StepsActivity.class);
                intent.putExtra("image",arrayList.get(position).getImage());
                intent.putExtra("heading",arrayList.get(position).getTitle());
                intent.putExtra("step1",arrayList.get(position).getStep1());
                intent.putExtra("step2",arrayList.get(position).getStep2());
                intent.putExtra("step3",arrayList.get(position).getStep3());
                intent.putExtra("step4",arrayList.get(position).getStep4());
                intent.putExtra("step5",arrayList.get(position).getStep5());



                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
//    public void filterList(ArrayList<Model> filterList){
//        arrayList = filterList;
//        notifyDataSetChanged();
//
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView heading ;
        TextView step1 , step2 , step3 , step4 , step5 ;
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            heading = itemView.findViewById(R.id.heading);
            step1 = itemView.findViewById(R.id.step1);
            step2 = itemView.findViewById(R.id.step2);
            step3 = itemView.findViewById(R.id.step3);
            step4 = itemView.findViewById(R.id.step4);
            step5 = itemView.findViewById(R.id.step5);

            cardview = itemView.findViewById(R.id.cardview);

        }
    }
}
