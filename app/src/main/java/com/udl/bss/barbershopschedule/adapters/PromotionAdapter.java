package com.udl.bss.barbershopschedule.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.udl.bss.barbershopschedule.R;
import com.udl.bss.barbershopschedule.domain.Barber;
import com.udl.bss.barbershopschedule.domain.BarberService;
import com.udl.bss.barbershopschedule.domain.Promotion;
import com.udl.bss.barbershopschedule.listeners.OnItemClickListener;
import com.udl.bss.barbershopschedule.serverCommunication.APIController;

import java.util.Iterator;
import java.util.List;


public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {

    private List<Promotion> mDataset;
    private OnItemClickListener listener;
    private String token;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView description;
        TextView service;
        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view);
            name = itemView.findViewById(R.id.name_cv);
            description = itemView.findViewById(R.id.description_cv);
            service = itemView.findViewById(R.id.service_cv);
        }
    }

    public PromotionAdapter(List<Promotion> myDataset, OnItemClickListener listener, String token) {
        mDataset = myDataset;
        this.listener = listener;
        this.token = token;
    }

    @Override
    public PromotionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promotion_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        APIController.getInstance().getServiceById(token ,String.valueOf(mDataset.get(position).getService_id()))
                .addOnCompleteListener(new OnCompleteListener<BarberService>() {
                    @Override
                    public void onComplete(@NonNull Task<BarberService> task) {
                        BarberService barberService = task.getResult();
                        holder.service.setText(barberService.getName());
                    }
                });

        APIController.getInstance().getBarberById(token ,String.valueOf(mDataset.get(position).getBarber_shop_id()))
                .addOnCompleteListener(new OnCompleteListener<Barber>() {
                    @Override
                    public void onComplete(@NonNull Task<Barber> task) {
                        Barber barber = task.getResult();
                        holder.name.setText(barber.getName());
                    }
                });

        holder.description.setText(mDataset.get(position).getDescription());

        ViewCompat.setTransitionName(holder.service, String.valueOf(position)+"_serv");
        ViewCompat.setTransitionName(holder.name, String.valueOf(position)+"_name");
        ViewCompat.setTransitionName(holder.description, String.valueOf(position)+"_desc");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Promotion getItem (int position) {
        return mDataset.get(position);
    }

    public void removeAll(){
        Iterator<Promotion> iter = mDataset.iterator();
        while(iter.hasNext()){
            Promotion promotion = iter.next();
            int position = mDataset.indexOf(promotion);
            iter.remove();
            notifyItemRemoved(position);
        }
    }

    public int add(Promotion promotion){
        mDataset.add(promotion);
        notifyItemInserted(mDataset.size()-1);
        return mDataset.size()-1;
    }
}

