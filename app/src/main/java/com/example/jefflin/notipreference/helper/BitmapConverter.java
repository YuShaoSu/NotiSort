package com.example.jefflin.notipreference.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class BitmapConverter {

    public byte[] toByte(Bitmap bmp) {

        int bytes = bmp.getByteCount();

        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);

        return buf.array();

    }

    public Bitmap toBmp(byte[] bmpByte, int height, int width) {

//        return BitmapFactory.decodeByteArray(bmpByte, 0, bmpByte.length);


        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        if(bmpByte != null)  stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(bmpByte));

        return stitchBmp;

    }

    @NonNull
    public Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, out);      // compress to save memory
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }


}
