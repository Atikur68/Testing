package com.flarze.hashstash.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flarze.hashstash.R;
import com.flarze.hashstash.data.ColorShades;
import com.flarze.hashstash.data.SharedPref;
import com.viewpagerindicator.CirclePageIndicator;

import static com.flarze.hashstash.data.Constants.IS_LOGIN;
import static com.flarze.hashstash.data.Constants.NOT_FIRST_TIME;

public class SlideShowActivity extends AppCompatActivity {
    private static final String SAVING_STATE_SLIDER_ANIMATION = "SliderAnimationSavingState";
    private boolean isSliderAnimation = false;
    ViewPager viewPager = null;
    Button btnGotit, btnSkip;

    private void launchHomeScreen() {
        SharedPref.write(NOT_FIRST_TIME, true);

        if (SharedPref.readBoolean(IS_LOGIN)) {
            startActivity(new Intent(SlideShowActivity.this, MapsActivity.class));
        } else {
            startActivity(new Intent(SlideShowActivity.this, SigninActivity.class));
        }
        finish();
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        if (SharedPref.readBoolean(NOT_FIRST_TIME)) {
            launchHomeScreen();
            finish();
        }

        setContentView(R.layout.activity_slide_show);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = (ViewPager) findViewById(R.id.pagerSlide);

        viewPager.setAdapter(new ViewPagerAdapter(R.array.icons, R.array.titles, R.array.hints));

        CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicatorSlide);
        mIndicator.setViewPager(viewPager);

        viewPager.setPageTransformer(true, new CustomPageTransformer());


        btnGotit = findViewById(R.id.btn_got_it_slide);
        btnSkip = findViewById(R.id.btn_skip_slide);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnGotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < 3) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

                View landingBGView = findViewById(R.id.layout_slideShow);
                int colorBg[] = getResources().getIntArray(R.array.landing_bg);


                ColorShades shades = new ColorShades();
                shades.setFromColor(colorBg[position % colorBg.length])
                        .setToColor(colorBg[(position + 1) % colorBg.length])
                        .setShade(positionOffset);

                landingBGView.setBackgroundColor(shades.generate());

            }

            @Override
            public void onPageSelected(int position) {

                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == 2) {
                    // last page. make button text to GOT IT
                    btnGotit.setVisibility(View.VISIBLE);
                    btnGotit.setText(getString(R.string.gotit));
                    btnSkip.setVisibility(View.GONE);
                } else {
                    // still pages are left
                    btnGotit.setVisibility(View.GONE);
                    //btnNext.setText(getString(R.string.next));
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    public class ViewPagerAdapter extends PagerAdapter {

        private int iconResId, titleArrayResId, hintArrayResId;

        public ViewPagerAdapter(int iconResId, int titleArrayResId, int hintArrayResId) {

            this.iconResId = iconResId;
            this.titleArrayResId = titleArrayResId;
            this.hintArrayResId = hintArrayResId;
        }

        @Override
        public int getCount() {
            return getResources().getIntArray(iconResId).length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Drawable icon = getResources().obtainTypedArray(iconResId).getDrawable(position);
            String title = getResources().getStringArray(titleArrayResId)[position];
            String hint = getResources().getStringArray(hintArrayResId)[position];


            View itemView = getLayoutInflater().inflate(R.layout.viewpager_item, container, false);


            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView titleView = (TextView) itemView.findViewById(R.id.landing_txt_title);
            TextView hintView = (TextView) itemView.findViewById(R.id.landing_txt_hint);


            iconView.setImageDrawable(icon);
            titleView.setText(title);
            hintView.setText(hint);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }

    public class CustomPageTransformer implements ViewPager.PageTransformer {


        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            View imageView = view.findViewById(R.id.landing_img_slide);
            View contentView = view.findViewById(R.id.landing_txt_hint);
            View txt_title = view.findViewById(R.id.landing_txt_title);

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left
            } else if (position <= 0) { // [-1,0]
                // This page is moving out to the left

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 + position);
                    setAlpha(txt_title, 1 + position);
                }

                if (imageView != null) {
                    // Fade the image in
                    setAlpha(imageView, 1 + position);
                }

            } else if (position <= 1) { // (0,1]
                // This page is moving in from the right

                // Counteract the default swipe
                setTranslationX(view, pageWidth * -position);
                if (contentView != null) {
                    // But swipe the contentView
                    setTranslationX(contentView, pageWidth * position);
                    setTranslationX(txt_title, pageWidth * position);

                    setAlpha(contentView, 1 - position);
                    setAlpha(txt_title, 1 - position);

                }
                if (imageView != null) {
                    // Fade the image out
                    setAlpha(imageView, 1 - position);
                }

            }
        }
    }

    /**
     * Sets the alpha for the view. The alpha will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view  - view to which alpha to be applied.
     * @param alpha - alpha value.
     */
    private void setAlpha(View view, float alpha) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setAlpha(alpha);
        }
    }

    /**
     * Sets the translationX for the view. The translation value will be applied only if the running android device OS is greater than honeycomb.
     *
     * @param view         - view to which alpha to be applied.
     * @param translationX - translationX value.
     */
    private void setTranslationX(View view, float translationX) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && !isSliderAnimation) {
            view.setTranslationX(translationX);
        }
    }

    public void onSaveInstanceState(Bundle outstate) {

        if (outstate != null) {
            outstate.putBoolean(SAVING_STATE_SLIDER_ANIMATION, isSliderAnimation);
        }

        super.onSaveInstanceState(outstate);
    }

    public void onRestoreInstanceState(Bundle inState) {

        if (inState != null) {
            isSliderAnimation = inState.getBoolean(SAVING_STATE_SLIDER_ANIMATION, false);
        }
        super.onRestoreInstanceState(inState);

    }
}

