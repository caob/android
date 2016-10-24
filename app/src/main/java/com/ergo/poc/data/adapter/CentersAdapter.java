package com.ergo.poc.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.data.model.Centers;
import com.ergo.poc.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mariano on 10/21/16.
 */
public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.ViewHolder> {

    private List<Centers> centersList;

    private OnItemClickListener onItemClickListener;

    public CentersAdapter() {
        this.centersList = new ArrayList<>();
    }

    public void setCentersList(List<Centers> centersList) {
        this.centersList = centersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_centers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Centers centers = centersList.get(position);

        int markerColor = 0;

        switch (centers.getType()) {
            case Constant.CENTERS_OWNER:
                markerColor = R.drawable.map_owner;
                break;
            case Constant.CENTERS_RETAIL:
                markerColor = R.drawable.map_retail;
                break;
            case Constant.CENTERS_DEALER:
                markerColor = R.drawable.map_dealer;
                break;
        }

        holder.mapMarker.setImageResource(markerColor);
        holder.type.setText(centers.getType());
        holder.state.setText(R.string.lorem_ipsum);
        holder.distance.setText(centers.getDistanceParsed());
    }

    @Override
    public int getItemCount() {
        return centersList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.centers_item_title)
        TextView type;

        @BindView(R.id.centers_item_description)
        TextView state;

        @BindView(R.id.centers_item_distance)
        TextView distance;

        @BindView(R.id.centers_item_map_icon)
        ImageView mapMarker;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(itemView, centersList.get(getAdapterPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Centers value);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
