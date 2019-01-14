package be.ibad.photogallery.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Locale;

import be.ibad.photogallery.R;
import be.ibad.photogallery.model.Record;
import be.ibad.photogallery.tracking.ActiveViewTracker;

/**
 * @author Pc Nicolas
 * All right reserved for PhotoGallery.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    public final static int LIST_VIEW = 0;
    public final static int PAGER_VIEW = 1;
    private ArrayList<Record> items;
    private OnItemClickListener listener;
    private RequestBuilder<Bitmap> mGlide;
    private RequestManager mRequestManager;
    private int type = LIST_VIEW;
    private ActiveViewTracker mActiveViewTracker;

    public GalleryAdapter(RequestManager requestManager) {
        mRequestManager = requestManager;
        mGlide = mRequestManager.asBitmap();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mActiveViewTracker = new ActiveViewTracker(recyclerView);
        mActiveViewTracker.setListener(new ActiveViewTracker.VisibilityTrackerListener() {
            @Override
            public void onMatchTrackingConstraints(View view, double percentageVisible) {
                view.findViewById(R.id.item_name).setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onScrollPercentage(View view, double visibleHeightPercentage) {
                GalleryViewHolder holder = (GalleryViewHolder) recyclerView.findContainingViewHolder(view);
                if (holder != null) {
                    Record record = items.get(holder.getAdapterPosition());
                    holder.nameTextView.setText(String.format(Locale.getDefault(), "%s %.2f %%", record.getFields().getPersonnageS(), visibleHeightPercentage));
                }
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void setData(ArrayList<Record> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == LIST_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic_wall, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fullscreen, parent, false);
        }
        return new GalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {
        String pictureUrl = String.format(Locale.US, "https://opendata.bruxelles.be/explore/dataset/bruxelles_parcours_bd/files/%s/download", items.get(position).getFields().getPhoto().getId());
        String thumbUrl = String.format(Locale.US, "https://opendata.bruxelles.be/explore/dataset/bruxelles_parcours_bd/files/%s/300", items.get(position).getFields().getPhoto().getId());
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .priority(Priority.HIGH);

        if (holder.getItemViewType() == LIST_VIEW) {
            holder.nameTextView.setText(items.get(position).getFields().getPersonnageS());
            holder.nameTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.transparent_black));
            options = options.centerCrop();
        } else {
            options = options.centerInside();
        }

        final RequestBuilder<Bitmap> thumbRequest = mGlide.clone()
                .load(thumbUrl)
                .apply(options.clone());

        final RequestBuilder<Bitmap> pictureRequest = mGlide.clone()
                .load(pictureUrl)
                .thumbnail(thumbRequest)
                .apply(options.clone());

        pictureRequest.into(holder.photoImageView);
    }

    @Override
    public void onViewRecycled(@NonNull GalleryViewHolder holder) {
        super.onViewRecycled(holder);
        mRequestManager.clear(holder.photoImageView);
        mActiveViewTracker.stopTracking(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setType(int type) {
        this.type = type;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView photoImageView;
        TextView nameTextView;

        GalleryViewHolder(View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.item_photo);
            nameTextView = itemView.findViewById(R.id.item_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        Log.i("TEST", "getAdapterPosition " + getAdapterPosition() + " getLayoutPosition " + getLayoutPosition());
                        listener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }

    }

}
