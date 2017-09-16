package be.ibad.photogallery.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;

import be.ibad.photogallery.GalleryAdapter;
import be.ibad.photogallery.R;
import be.ibad.photogallery.model.ResponseOpenData;
import be.ibad.photogallery.webservices.OpenDataServices;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String POSITION_STATE = "POSITION_STATE";
    private RecyclerView mRecyclerView;
    private PagerSnapHelper mSnapHelper;
    private GridLayoutManager mGridLayoutManager;
    private GalleryAdapter mAdapter;
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("http://opendata.bruxelles.be/")
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
                    mAdapter.setType(GalleryAdapter.PAGER_VIEW);
                    mGridLayoutManager.setSpanCount(1);
                    mGridLayoutManager.setOrientation(StaggeredGridLayoutManager.HORIZONTAL);
                    mSnapHelper.attachToRecyclerView(mRecyclerView);
                    mGridLayoutManager.scrollToPositionWithOffset(position, 0);
                } else {
                    mAdapter.setType(GalleryAdapter.LIST_VIEW);
                    mGridLayoutManager.setSpanCount(2);
                    mGridLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
                    mSnapHelper.attachToRecyclerView(null);
                    mGridLayoutManager.scrollToPosition(position);
                }
            }
        });

        mGridLayoutManager = new GridLayoutManager(MainActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mSnapHelper.attachToRecyclerView(mRecyclerView);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(POSITION_STATE);
        }
        services.getAll(100)
                .enqueue(new Callback<ResponseOpenData>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseOpenData> call, @NonNull Response<ResponseOpenData> response) {
                        if (response.isSuccessful()) {
                            ResponseOpenData data = response.body();
                            if (data != null) {
                                mAdapter.setData(data.getRecords());
                                mGridLayoutManager.scrollToPosition(mPosition);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseOpenData> call, @NonNull Throwable t) {

                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_STATE, mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
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
                mAdapter.setType(GalleryAdapter.PAGER_VIEW);
                mGridLayoutManager.setSpanCount(1);
                mGridLayoutManager.setOrientation(StaggeredGridLayoutManager.HORIZONTAL);
                mSnapHelper.attachToRecyclerView(mRecyclerView);
            } else {
                mAdapter.setType(GalleryAdapter.LIST_VIEW);
                mGridLayoutManager.setSpanCount(2);
                mGridLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
                mSnapHelper.attachToRecyclerView(null);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
