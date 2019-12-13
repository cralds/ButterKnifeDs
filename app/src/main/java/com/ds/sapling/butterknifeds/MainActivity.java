package com.ds.sapling.butterknifeds;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.sapling.annotation.BindOnClick;
import com.ds.sapling.annotation.BindView;
import com.ds.sapling.buttermanager.ButterKnifeD;
import com.ds.sapling.buttermanager.UnBinder;

import java.lang.reflect.Constructor;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvHtllo)
    public TextView tv;
    @BindView(R.id.tvTwo)
    public TextView tvTwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UnBinder unBinder = ButterKnifeD.bind(this);
        dealTvOne();
        dealTvTwo();

    }

    @BindOnClick(R.id.tvHtllo)
    public void clickTvHello(){
        Toast.makeText(this,"click phone",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void dealTvOne() {

        SpannableString spannableString = new SpannableString(" 爆款直降  苹果手机");
        ImageSpan imageSpan = new ImageSpan(this, R.mipmap.ic_launcher, DynamicDrawableSpan.ALIGN_BASELINE);
//        spannableString.setSpan(imageSpan,0,1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        RadiusSpan span = new RadiusSpan(this,12, Color.parseColor("#F32020"),Color.parseColor("#FFEFEF"),9);
        spannableString.setSpan(span,0,5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }

    private void dealTvTwo(){
        SpannableString spannableString = new SpannableString(" 爆款直降  苹果手机");
        RadiusBackgroundV2Span span = new RadiusBackgroundV2Span(this, Color.parseColor("#F32020"),Color.parseColor("#FFEFEF"),5,12,2, Paint.Style.FILL);
        spannableString.setSpan(span,0,5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(spannableString);
    }
}
