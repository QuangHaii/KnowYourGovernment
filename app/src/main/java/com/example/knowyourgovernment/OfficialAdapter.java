package com.example.knowyourgovernment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Official> officials;

    public OfficialAdapter(Context context, ArrayList<Official> officials) {
        this.context = context;
        this.officials = officials;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOffice;
        private TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOffice = itemView.findViewById(R.id.txtOffice);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View textView = inflater.inflate(R.layout.row_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Official official = officials.get(position);
        holder.txtName.setText(official.getName()+" ("+official.getParty()+")");
        holder.txtOffice.setText(official.getOffice());
    }

    @Override
    public int getItemCount() {
        return officials.size();
    }

}
