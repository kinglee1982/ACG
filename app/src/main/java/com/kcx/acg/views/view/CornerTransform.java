package com.kcx.acg.views.view;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;


/**
 */

public class CornerTransform  extends BitmapTransformation {
    private static float radius = 0f;

    public CornerTransform() {
        this(4);
    }

    public CornerTransform(int dp) {
        super();
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        //变换的时候裁切
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return roundCrop(pool, bitmap);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }


    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
//        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        RectF rectRound = new RectF(0f, 100f, source.getWidth(), source.getHeight());
        canvas.drawRect(rectRound, paint);
        canvas.drawRoundRect(rectRound, radius, radius, paint);
//        RectF rectRound = new RectF(0f, 100f, source.getWidth(), source.getHeight());
//        canvas.drawRect(rectRound, paint);
        return result;
    }
}