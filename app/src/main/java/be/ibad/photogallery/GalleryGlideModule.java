package be.ibad.photogallery;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by nvandamme on 27/12/17.
 */

@GlideModule
public class GalleryGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(10)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
    }

}