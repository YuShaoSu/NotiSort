package com.example.jefflin.notipreference.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class IconHandler {

    private Context mContext;

//    public IconHandler(Context context) {
//        this.mContext = context;
//    }

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

    public String saveToInternalStorage(Drawable drawable, File dir, String iconName){
        Bitmap bitmapImage = getBitmapFromDrawable(drawable);

        File mypath=new File(dir, iconName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dir.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path ,String iconName)
    {

        Bitmap icon = null;

        try {
            File f=new File(path, iconName);
            icon = BitmapFactory.decodeStream(new FileInputStream(f));
            Log.d("load icon from", String.valueOf(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return icon;

    }



}
