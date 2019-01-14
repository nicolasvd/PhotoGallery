package be.ibad.photogallery.tracking;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Map;
import java.util.WeakHashMap;

import be.ibad.photogallery.R;

/**
 * Created by nvandamme on 09/11/2018.
 * all rights reserved for PhotoGallery
 */
public class ActiveViewTracker {

    private static final long DELAY_ONE_SECOND = 1000;
    private static final double MINIMUM_VISIBLE_HEIGHT_THRESHOLD = 50;
    // Flag is required because 'addOnGlobalLayoutListener'
    // is called multiple times.
    // The flag limits the action inside 'onGlobalLayout' to only once.
    private boolean firstTrackFlag = false;
    private VisibilityTrackerListener listener;
    private Handler mVisibilityHandler = new Handler();
    private WeakHashMap<View, Runnable> mTrackedViews = new WeakHashMap<>();

    public ActiveViewTracker(@NonNull final RecyclerView recyclerView) {
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        final LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());

        if (adapter != null && layoutManager != null) {
            // recycler view for the first time.
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                    .OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!firstTrackFlag && adapter.getItemCount() > 0) {
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = ((LinearLayoutManager)
                                recyclerView.getLayoutManager())
                                .findLastVisibleItemPosition();

                        analyzeAndAddViewData(layoutManager, firstVisibleItemPosition, lastVisibleItemPosition);
                        firstTrackFlag = true;
                    }
                }
            });

            // Track the views when user scrolls through the recyclerview.
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && adapter.getItemCount() > 0) {
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                        analyzeAndAddViewData(layoutManager, firstVisibleItemPosition, lastVisibleItemPosition);
                    }
                }
            });

            recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
                @Override
                public boolean onFling(int velocityX, int velocityY) {


                    return false;
                }
            });
        }
    }

    private void analyzeAndAddViewData(@NonNull RecyclerView.LayoutManager layoutManager,
                                       int firstVisibleItemPosition,
                                       int lastVisibleItemPosition) {
        // Analyze all the views

        for (int viewPosition = firstVisibleItemPosition; viewPosition <= lastVisibleItemPosition; viewPosition++) {
            Log.i("View being considered", String.valueOf(viewPosition));
            // Get the view from its position.
            final View itemView = layoutManager.findViewByPosition(viewPosition);
            if (itemView != null) {
                if (listener != null) {
                    listener.onScrollPercentage(itemView, getVisibleHeightPercentage(itemView));
                }
                // Check if the visibility of the view is more than or equal
                // to the threshold provided. If it falls under the desired limit,
                // add it to the tracking data.
                if (getVisibleHeightPercentage(itemView) >= MINIMUM_VISIBLE_HEIGHT_THRESHOLD) {
                    //todo delete color change it's for demo and test purposes
                    if (!mTrackedViews.containsKey(itemView)) {
                        itemView.findViewById(R.id.item_name).setBackgroundColor(Color.YELLOW);
                        Runnable delayRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if (mTrackedViews.containsKey(itemView)) {
                                    if (listener != null) {
                                        listener.onMatchTrackingConstraints(itemView, getVisibleHeightPercentage(itemView));
                                    }
                                }
                            }
                        };
                        mTrackedViews.put(itemView, delayRunnable);
                    }
                } else {
                    mTrackedViews.remove(itemView);
                }
                scheduleVisibilityCheck();
            }
        }
    }

    private void scheduleVisibilityCheck() {
        if (mTrackedViews != null && !mTrackedViews.isEmpty()) {
            for (Map.Entry<View, Runnable> viewRunnableEntry : mTrackedViews.entrySet()) {
                mVisibilityHandler.postDelayed(viewRunnableEntry.getValue(), DELAY_ONE_SECOND);
            }
        }
    }

    // Method to calculate how much of the view is visible
    // (i.e. within the screen) wrt the view height.
    // @param view
    // @return Percentage of the height visible.
    private double getVisibleHeightPercentage(View view) {
        Rect itemRect = new Rect();
        view.getLocalVisibleRect(itemRect);

        // Find the height of the item.
        double visibleHeight = itemRect.height();
        double height = view.getMeasuredHeight();
        double viewVisibleHeightPercentage = ((visibleHeight / height) * 100);

        Log.i("Percentage visible", String.valueOf(viewVisibleHeightPercentage));
        return viewVisibleHeightPercentage;
    }

    public void setListener(VisibilityTrackerListener listener) {
        this.listener = listener;
    }

    public void stopTracking(View itemView) {
        Runnable runnable = mTrackedViews.remove(itemView);
        mVisibilityHandler.removeCallbacks(runnable);
    }

    public interface VisibilityTrackerListener {

        void onMatchTrackingConstraints(View view, double percentageVisible);

        void onScrollPercentage(View itemView, double visibleHeightPercentage);
    }

}
