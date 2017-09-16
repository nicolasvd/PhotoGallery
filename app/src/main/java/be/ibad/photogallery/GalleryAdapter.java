package be.ibad.photogallery;

import android.graphics.Bitmap;
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

import be.ibad.photogallery.model.Record;

/**
 * @author Pc Nicolas
 *         All right reserved for PhotoGallery.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    public final static int LIST_VIEW = 0;
    public final static int PAGER_VIEW = 1;

    private ArrayList<Record> items;
    private OnItemClickListener listener;
    private RequestBuilder<Bitmap> mGlide;
    private RequestManager mRequestManager;

    private int type = PAGER_VIEW;

    public GalleryAdapter(RequestManager requestManager) {
        RequestOptions options = new RequestOptions().placeholder(R.drawable.placeholder).priority(Priority.HIGH);
        mRequestManager = requestManager;
        mGlide = mRequestManager.asBitmap().apply(options);
    }

    public void setData(ArrayList<Record> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == LIST_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic_wall, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fullscreen, parent, false);
        }
        return new GalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, int position) {
        String url = String.format(Locale.US, "https://opendata.bruxelles.be/explore/dataset/bruxelles_parcours_bd/files/%s/download", items.get(position).getFields().getPhoto().getId());
        String thumbUrl = String.format(Locale.US, "https://opendata.bruxelles.be/explore/dataset/bruxelles_parcours_bd/files/%s/300", items.get(position).getFields().getPhoto().getId());

        if (holder.getItemViewType() == LIST_VIEW) {
            holder.nameTextView.setText(items.get(position).getFields().getPersonnageS());
            mGlide.load(thumbUrl).into(holder.photoImageView);
        } else {
            RequestBuilder<Bitmap> thumbRequestBuilder = mGlide.load(thumbUrl).clone();
            mGlide.load(url)
                    .thumbnail(thumbRequestBuilder)
                    .into(holder.photoImageView);
        }
    }

    @Override
    public void onViewRecycled(GalleryViewHolder holder) {
        super.onViewRecycled(holder);
        holder.photoImageView.setImageBitmap(null);
        mRequestManager.clear(holder.photoImageView);
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
