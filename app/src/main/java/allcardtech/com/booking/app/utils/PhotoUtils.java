package allcardtech.com.booking.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by jrbigcas on 3/6/2018.
 */

public class PhotoUtils {


    public static int neededRotation(String fileName) {
        try {
            ExifInterface exif = new ExifInterface(fileName);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            }
            return 0; //lets rotate it to portrait
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Bitmap get2by2(String fileName) {
        Bitmap cameraBmp = BitmapFactory.decodeFile(fileName);
//        cameraBmp = ThumbnailUtils.extractThumbnail(cameraBmp, 320, 320);
        Matrix m = new Matrix();
        m.postRotate(neededRotation(fileName));
        cameraBmp = Bitmap.createBitmap(cameraBmp, 0, 0, cameraBmp.getWidth(), cameraBmp.getHeight(), m, true);
        cameraBmp = Bitmap.createScaledBitmap(cameraBmp, (int) (cameraBmp.getWidth() * 0.2), (int) (cameraBmp.getHeight() * 0.2), true);
        return cameraBmp;
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            cameraBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //or say cameraBmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
//            imageBytesRESULT = baos.toByteArray();
    }
}
