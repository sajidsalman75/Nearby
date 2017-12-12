package com.example.sajidsalman75.nearby.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 12/8/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Bitmap bitmaps[];

    public ViewPagerAdapter(Context context, Bitmap[] bitmaps1) {
        this.context = context;
        this.bitmaps = bitmaps1;
    }

    @Override
    public int getCount() {
        return bitmaps.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmaps[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
