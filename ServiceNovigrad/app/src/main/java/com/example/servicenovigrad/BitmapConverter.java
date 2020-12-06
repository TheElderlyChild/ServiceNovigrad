package com.example.servicenovigrad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
This class is based on a reply from https://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
from user Lazy Ninja.
*/

public class BitmapConverter {
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        try {
            stream.close();
        }
        catch(IOException e){ }
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
