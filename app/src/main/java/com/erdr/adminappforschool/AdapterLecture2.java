package com.erdr.adminappforschool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterLecture2 extends RecyclerView.Adapter<AdapterLecture2.BatchViewHolder> {

    Context mCtx;
    List<ListLectureList> batchtlist;
    //I am adding extra


    public AdapterLecture2(Context mCtx, List<ListLectureList> batchtlist) {
        this.mCtx = mCtx;
        this.batchtlist = batchtlist;
    }

    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.layout_lecture_video,parent,false);

        BatchViewHolder BatchViewHolder = new BatchViewHolder(view);
        return BatchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BatchViewHolder holder, int position) {

        ListLectureList Batchdata;
        Batchdata = batchtlist.get(position);
        holder.mainText.setText(Batchdata.getDataToShow());

    }

    @Override
    public int getItemCount() {

        return batchtlist.size();
    }

    class BatchViewHolder extends RecyclerView.ViewHolder {
      TextView mainText;

        public BatchViewHolder(@NonNull View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.mainText);


            mainText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Page3) mCtx).userItemClick(getAdapterPosition());
                }
            });

        }
    }

}
