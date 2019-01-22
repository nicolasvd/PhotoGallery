package be.ibad.photogallery.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


/**
 * Created by nvandamme on 16/01/2019.
 * all rights reserved for PhotoGallery
 */
public class TrackedView extends FrameLayout {

    private static final long DELAY_ONE_SECOND = 1000;
    private Rect rootRect = new Rect();
    private Rect viewRect = new Rect();
    private long lastCheckTime = System.currentTimeMillis() - 200;
    private ExposureChangeListener listener;

    private Handler mTrackingHandler = new Handler();
    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            if (listener != null) {
                listener.onMatchTrackingRules(TrackedView.this);
            }
        }
    };

    public TrackedView(Context context) {
        super(context);
        init();
    }

    public TrackedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrackedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                enableTracking();
            }

        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        enableTracking();
    }

    private void enableTracking() {
        long throttleMs = System.currentTimeMillis() - lastCheckTime;
//        DevLog.i("PLOP throttleMs: " + throttleMs);
//        DevLog.i("PLOP getVisibility: " + (getVisibility() == VISIBLE));
//        DevLog.i("PLOP getAlpha: " + getAlpha());
//        DevLog.i("PLOP isShown: " + isShown());
//        DevLog.i("PLOP isExposed: " + isExposed(50));
//        DevLog.i("PLOP is Valid: " + (isExposed(50)
//                && getVisibility() == VISIBLE
//                && getAlpha() > 0
//                && isShown()
//                && throttleMs > 200));

        if (throttleMs > 200) {
            lastCheckTime = System.currentTimeMillis();
            if (isExposed(50)
                    && getVisibility() == VISIBLE
                    && getAlpha() > 0
                    && isShown()) {
                mTrackingHandler.postDelayed(delayRunnable, DELAY_ONE_SECOND);
            } else {
                mTrackingHandler.removeCallbacks(delayRunnable);
            }
        }
    }

    public void setListener(ExposureChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private boolean isExposed(@FloatRange(from = 0, to = 100) float minPercentageVisible) {
        if (getRootView() == null) {
            return false;
        }
        getRootView().getHitRect(rootRect);
        getLocalVisibleRect(viewRect);

        float itemVisibleSurface = viewRect.height() * viewRect.width();
//        DevLog.d("PLOP item height :" + itemVisibleSurface);
        float itemTotalSurface = getMeasuredHeight() * getMeasuredWidth();
//        DevLog.d("PLOP root height :" + itemTotalSurface);
        float requiredSurface = itemTotalSurface * (minPercentageVisible / 100);
//        DevLog.i("PLOP requiredSurface :" + requiredSurface);

//        Rect exposedRect = new Rect();
//        if (intersection2(rootRect, viewRect, exposedRect)) {
//            float exposedSurface = exposedRect.height() * exposedRect.width();
//            DevLog.i("PLOP exposedSurface :" + (exposedSurface / itemTotalSurface) * 100 + "%");
//        DevLog.i("PLOP isExposed :" + (itemVisibleSurface >= requiredSurface));
        return (itemVisibleSurface >= requiredSurface);
//        }
//        return false;
    }


    public interface ExposureChangeListener {
        void onMatchTrackingRules(View view);
    }

//    //returns true when intersection is found, false otherwise.
//    //when returning true, rectangle 'out' holds the intersection of r1 and r2.
//    private static boolean intersection2(Rect r1, Rect r2, Rect out) {
//        if (r1.intersect(r2)) {
//            int left = Math.max(r1.left, r2.left);
//            int right = Math.min(r1.right, r2.right);
//            int top = Math.max(r1.top, r2.top);
//            int bottom = Math.min(r1.bottom, r2.bottom);
//            out.set(left, top, right, bottom);
//            return true;
//        }
//        return false;
//    }
//
//
//    /**
//     * Whether the view is at least certain % visible
//     */
//    boolean isVisible(@Nullable final View rootView, @Nullable final View view, final float minPercentageViewed) {
//        // ListView & GridView both call detachFromParent() for views that can be recycled for
//        // new data. This is one of the rare instances where a view will have a null parent for
//        // an extended period of time and will not be the main window.
//        // view.getGlobalVisibleRect() doesn't check that case, so if the view has visibility
//        // of View.VISIBLE but it's group has no parent it is likely in the recycle bin of a
//        // ListView / GridView and not on screen.
//        if (view == null || view.getVisibility() != View.VISIBLE || rootView == null || rootView.getParent() == null) {
//            return false;
//        }
//
//        if (!view.getLocalVisibleRect(mClipRect)) {
//            // Not visible
//            return false;
//        }
//
//        rootView.getHitRect(rootRect);
//        view.getWindowVisibleDisplayFrame(viewRect);
//
//        Rect mRootRect = new Rect();
//        view.getWindowVisibleDisplayFrame(mRootRect);
////         % visible check - the cast is to avoid int overflow for large views.
//        final float visibleViewArea = (float) mClipRect.height() * mClipRect.width();
//        final float totalViewArea = (float) rootView.getMeasuredHeight() * rootView.getMeasuredWidth();
//        float requiredSurface = totalViewArea * (minPercentageViewed / 100);
//        DevLog.i("PLOP " + requiredSurface + " > " + totalViewArea);
//        DevLog.i("PLOP " + (totalViewArea * (minPercentageViewed / 100)));
//
//        if (totalViewArea <= 0) {
//            return false;
//        }
//
//        DevLog.i("PLOP " + (100 * visibleViewArea >= minPercentageViewed * totalViewArea));
//        DevLog.i("PLOP " + ((visibleViewArea / totalViewArea) * 100) + "%");
//        DevLog.i("PLOP " + (100 * visibleViewArea) + " >= " + (minPercentageViewed * totalViewArea));
//        return 100 * visibleViewArea >= minPercentageViewed * totalViewArea;
//    }

}
