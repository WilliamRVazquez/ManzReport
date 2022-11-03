package com.example.manzreport.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manzreport.R;
import com.example.manzreport.model.Report;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ReportAdapter extends FirestoreRecyclerAdapter<Report, ReportAdapter.ViewHolder> {//
    Activity activity;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReportAdapter(@NonNull FirestoreRecyclerOptions<Report> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @NonNull Report Report) {
        viewHolder.tiporeporte.setText(Report.getTiporeporte());//
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_report_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tiporeporte, ubicacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tiporeporte = itemView.findViewById(R.id.tiporeporte);


        }
    }
}
