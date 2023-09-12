package com.jakop52.apartmentrent.android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.List;

public class MediaPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Object> mediaList;

    public MediaPagerAdapter(Context context, List<Object> mediaList) {
        this.context = context;
        this.mediaList = mediaList;
    }

    @Override
    public int getCount() {
        return mediaList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object mediaItem = mediaList.get(position);
        if (mediaItem instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) mediaItem;
            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(drawable);
            container.addView(imageView);
            return imageView;
        } else if (mediaItem instanceof Drawable) {
            Drawable drawable = (Drawable) mediaItem;
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(drawable);
            container.addView(imageView);
            return imageView;
        } else {
            return new View(context);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}

