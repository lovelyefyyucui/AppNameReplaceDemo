package com.name.replace.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;

import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BitmapUtils {
    /**
     * @Title: encodeTobase64 @Description: TODO(这里用一句话描述这个方法的作用) @param @param
     * image @param @return 设定文件 @return String 返回类型 @throws
     */
    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        image.recycle();
//        image = null;
        return imageEncoded;
    }

    public static String encodeTobase64(String path) {
        if (path != null && path.length() > 0) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(path);
            bitmap = bitmapDrawable.getBitmap();
            int bitmapDegree = BitmapUtils.getBitmapDegree(path);
            bitmap = BitmapUtils.rotateBitmapByDegree(BitmapUtils.bitmap, bitmapDegree);
        } else {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        try {
            baos.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageEncoded;
    }

    //根据图片的宽高比例得出是普通图片还是长图
    public static String getImageWidth(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        String str="180";
        if(options.outWidth / options.outHeight>=3)
        {//宽是高的三倍，属于横长图
            str="360";
        }else if(options.outHeight / options.outWidth>=3)
        {
            str="142";
        }
        return str;
    }


    /**
     * @param imgPath
     * @param bitmap
     * @param imgFormat 图片格式
     * @return
     */
    public static Bitmap bitmap = null;

    public static String getImgBase64(String imgPath, int width, int height) {

        if (imgPath != null && imgPath.length() > 0) {
            bitmap = getBitmapFromFile(imgPath, width, height);
            int bitmapDegree = BitmapUtils.getBitmapDegree(imgPath);
            bitmap = BitmapUtils.rotateBitmapByDegree(BitmapUtils.bitmap, bitmapDegree);
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            bitmap = null;
            bitmap.recycle();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String imgToBase64(String imgPath) {
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = getBitmapFromFile(imgPath, 480, 800);
            int bitmapDegree = BitmapUtils.getBitmapDegree(imgPath);
            bitmap = BitmapUtils.rotateBitmapByDegree(BitmapUtils.bitmap, bitmapDegree);
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            bitmap.recycle();
            bitmap = null;
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /***
     * 等比例压缩图片
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
                                   int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;

//		float scale1 = scale < scale2 ? scale : scale2;

        // 保证图片不变形.
        matrix.postScale(scale, scale2);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
    }

    //  通过二次采样，二次采样的原理如下：
    // 1.第一次，先取出图片的宽高，不取它的真实大小，因此bitmap是空的；
    // 2.第二次，取出它的大小，并进行压缩处理。
    public static Bitmap getBitmapFromFile(String path, int width, int height) {
        if (!TextUtils.isEmpty(path)) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();//设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                // 第一次：设为true时，仅仅得到边界，即宽高
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                //这里一定要将其设置回false，表示不仅仅加载边框，因为之前我们将其设置成了true
                opts.inJustDecodeBounds = false;
                //将边框缩减到原来宽高的 30 / 100；
//                opts.inSampleSize = 30 / 100;// 设置压缩比例
                opts.inSampleSize = opts.inSampleSize + 2;
                ;
                // 第二次：将options的值设为Config.RGB_565，会比默认的Config.ARGB_8888减少一半内存；
                opts.inPreferredConfig = Config.RGB_565;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(path, opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    /**
     * 计算图片的缩放值
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    public static String videoTobase64(String filepath) {
        FileInputStream objFileIS = null;
        ByteArrayOutputStream objByteArrayOS = null;
        try {
            objFileIS = new FileInputStream(filepath);
            objByteArrayOS = new ByteArrayOutputStream();
            byte[] byteBufferString = new byte[1024];
            int readNum;
            while ((readNum = objFileIS.read(byteBufferString)) != -1) {
                objByteArrayOS.write(byteBufferString, 0, readNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                objByteArrayOS.flush();
                objByteArrayOS.close();
                objFileIS.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Base64.encodeToString(objByteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap drawable2Bitmap(Drawable d) {
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }



    public static Bitmap mergeBitmap(ArrayList<Bitmap> bitmaps, int osize, int density, int padding) {
        int size = bitmaps.size();
        Bitmap bitmap = Bitmap.createBitmap(osize, osize, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0xfffafafa);
        if (size == 3) {
            canvas.drawBitmap(bitmaps.get(0), density / 2 + padding / 2, 0, null);
//            canvas.drawBitmap(bitmaps.get(1), density / 2 + padding/2, 0, null);
            canvas.drawBitmap(bitmaps.get(1), 0, density + padding, null);
//            canvas.drawBitmap(bitmaps.get(0), 0, density + padding, null);
            canvas.drawBitmap(bitmaps.get(2), density + padding, density + padding, null);
        } else if (size == 4) {
            canvas.drawBitmap(bitmaps.get(0), 0, 0, null);
            canvas.drawBitmap(bitmaps.get(1), density + padding, 0, null);
            canvas.drawBitmap(bitmaps.get(2), 0, density + padding, null);
            canvas.drawBitmap(bitmaps.get(3), density + padding, density + padding, null);
        } else if (size == 5) {
            canvas.drawBitmap(bitmaps.get(0), density / 2 + padding / 2, density / 2, null);
            canvas.drawBitmap(bitmaps.get(1), (density + padding) * 3 / 2, density / 2, null);
            canvas.drawBitmap(bitmaps.get(2), 0, density * 3 / 2 + padding * 3 / 2, null);
            canvas.drawBitmap(bitmaps.get(3), density + padding, density * 3 / 2 + padding * 3 / 2, null);
            canvas.drawBitmap(bitmaps.get(4), density * 2 + padding * 2, density * 3 / 2 + padding * 3 / 2, null);
        } else if (size == 6) {
            canvas.drawBitmap(bitmaps.get(0), 0, density / 2, null);
            canvas.drawBitmap(bitmaps.get(1), density + padding, density / 2, null);
            canvas.drawBitmap(bitmaps.get(2), density * 2 + padding * 2, density / 2, null);
            canvas.drawBitmap(bitmaps.get(3), 0, density * 3 / 2 + padding * 2, null);
            canvas.drawBitmap(bitmaps.get(4), density + padding, density * 3 / 2 + padding * 2, null);
            canvas.drawBitmap(bitmaps.get(5), density * 2 + padding * 2, density * 3 / 2 + padding * 2, null);
        } else if (size == 7) {
            canvas.drawBitmap(bitmaps.get(0), density + padding, 0, null);
            canvas.drawBitmap(bitmaps.get(1), 0, density + padding, null);
            canvas.drawBitmap(bitmaps.get(2), density + padding, density + padding, null);
            canvas.drawBitmap(bitmaps.get(3), density * 2 + padding * 2, density + padding, null);
            canvas.drawBitmap(bitmaps.get(4), 0, density * 2 + padding * 2, null);
            canvas.drawBitmap(bitmaps.get(5), density + padding, density * 2 + padding * 2, null);
            canvas.drawBitmap(bitmaps.get(6), density * 2 + padding * 2, density * 2 + padding * 2, null);
        } else if (size == 8) {
            canvas.drawBitmap(bitmaps.get(0), density / 2 + padding / 2, 0, null);
            canvas.drawBitmap(bitmaps.get(1), (density + padding) * 3 / 2, 0, null);
            canvas.drawBitmap(bitmaps.get(2), 0, density + padding, null);
            canvas.drawBitmap(bitmaps.get(3), density + padding, density + padding, null);
            canvas.drawBitmap(bitmaps.get(4), density * 2 + padding * 2, density + padding, null);
            canvas.drawBitmap(bitmaps.get(5), 0, density * 2 + padding * 2, null);
            canvas.drawBitmap(bitmaps.get(6), density + padding, density * 2 + padding * 2, null);
            canvas.drawBitmap(bitmaps.get(7), density * 2 + padding * 2, density * 2 + padding * 2, null);
        } else if (size >= 9) {
            canvas.drawBitmap(bitmaps.get(0), 0, 0, null);
            canvas.drawBitmap(bitmaps.get(1), density + padding, 0, null);
            canvas.drawBitmap(bitmaps.get(2), density * 2 + padding * 2, 0, null);
            canvas.drawBitmap(bitmaps.get(3), 0, density + padding, null);
            canvas.drawBitmap(bitmaps.get(4), density + padding, density + padding, null);
            canvas.drawBitmap(bitmaps.get(5), density * 2 + padding * 2, density + padding, null);
            canvas.drawBitmap(bitmaps.get(6), 0, density * 2 + padding * 3, null);
            canvas.drawBitmap(bitmaps.get(7), density + padding, density * 2 + padding * 2, null);
            canvas.drawBitmap(bitmaps.get(8), density * 2 + padding * 2, density * 2 + padding * 2, null);
        }
        return bitmap;
    }

    public static String getImgName(int i, String name) {
        i++;
        LogUtil.e("getImgName", "name" + name);
        int start = name.lastIndexOf("/");
        int end = name.lastIndexOf(".");
        String substring = name.substring(start, end);
        substring = substring + "_" + i;
        LogUtil.e("getImgName", "substring" + substring);
        return substring;
    }

    public static String getNowTime(int i) {
        return Calendar.getInstance().getTimeInMillis() + "_" + i;
    }
}
