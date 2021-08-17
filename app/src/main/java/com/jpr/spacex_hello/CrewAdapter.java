package com.jpr.spacex_hello;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {

    private Context context;
    private List<com.jpr.spacex_hello.Crew> mCrewList;

    public CrewAdapter(Context context, List<com.jpr.spacex_hello.Crew> mCrewList) {
        this.context = context;
        this.mCrewList = mCrewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(mCrewList.get(position).getImage())
                .into(holder.mImage);

        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.jpr.spacex_hello.DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("detail", mCrewList.get(position));
                context.startActivity(intent);
            }
        });

        holder.mCrewName.setText(mCrewList.get(position).getName());
    }



    @Override
    public int getItemCount() {
        return mCrewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mCrewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.crewImage);
            mCrewName= itemView.findViewById(R.id.crewName);
        }
    }

}
