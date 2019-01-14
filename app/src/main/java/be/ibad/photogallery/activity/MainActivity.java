package be.ibad.photogallery.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;

import be.ibad.photogallery.R;
import be.ibad.photogallery.adapter.GalleryAdapter;
import be.ibad.photogallery.model.ResponseOpenData;
import be.ibad.photogallery.webservices.OpenDataServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static be.ibad.photogallery.adapter.GalleryAdapter.LIST_VIEW;
import static be.ibad.photogallery.adapter.GalleryAdapter.PAGER_VIEW;

public class MainActivity extends AppCompatActivity {

    private static final String POSITION_STATE = "POSITION_STATE";
    private static final String TYPE_STATE = "TYPE_STATE";
    private RecyclerView mRecyclerView;
    private PagerSnapHelper mSnapHelper;
    private GridLayoutManager mGridLayoutManager;
    private GalleryAdapter mAdapter;
    private int mPosition = 0;
    private int mType = LIST_VIEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://opendata.bruxelles.be/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        OpenDataServices services = restAdapter.create(OpenDataServices.class);

        mRecyclerView = findViewById(R.id.recyclerView_main);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSnapHelper = new PagerSnapHelper();
        mAdapter = new GalleryAdapter(Glide.with(this));

        mAdapter.setListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                boolean isVertical = mGridLayoutManager.canScrollVertically();
                if (isVertical) {
                    setPagerView(position);
                }
            }
        });
        mGridLayoutManager = new GridLayoutManager(MainActivity.this, 1, GridLayoutManager.VERTICAL, false);
        mGridLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(POSITION_STATE);
            mType = savedInstanceState.getInt(TYPE_STATE);

        }
        services.getAll(100)
                .enqueue(new Callback<ResponseOpenData>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseOpenData> call, @NonNull Response<ResponseOpenData> response) {
                        Log.i("TEST", response.code() + " " + response.message());
                        if (response.isSuccessful()) {
                            ResponseOpenData data = response.body();
                            if (data != null) {
                                mAdapter.setData(data.getRecords());
                                if (mType == LIST_VIEW) {
                                    setListView(mPosition);
                                } else {
                                    setPagerView(mPosition);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseOpenData> call, @NonNull Throwable t) {
                        Log.e("TEST", t.getMessage());
                        t.printStackTrace();
                    }
                });
    }

    private void setListView(int position) {
        mAdapter.setType(LIST_VIEW);
        mGridLayoutManager.setSmoothScrollbarEnabled(true);
        mGridLayoutManager.setSpanCount(1);
        mGridLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        mSnapHelper.attachToRecyclerView(null);
        mGridLayoutManager.scrollToPosition(position);
    }

    public void setPagerView(int position) {
        mAdapter.setType(PAGER_VIEW);
        mGridLayoutManager.setSmoothScrollbarEnabled(false);
        mGridLayoutManager.setSpanCount(1);
        mGridLayoutManager.setOrientation(StaggeredGridLayoutManager.HORIZONTAL);
        mSnapHelper.attachToRecyclerView(mRecyclerView);
        mGridLayoutManager.scrollToPosition(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_STATE, mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
        outState.putInt(TYPE_STATE, mGridLayoutManager.canScrollVertically() ? LIST_VIEW : PAGER_VIEW);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_layout) {
            boolean isVertical = mGridLayoutManager.canScrollVertically();
            if (isVertical) {
                setPagerView(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
            } else {
                setListView(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
