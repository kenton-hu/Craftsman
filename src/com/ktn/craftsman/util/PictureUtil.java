
package com.ktn.craftsman.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;

/**
 * @作者： [欢欢 email：HuanHuanFu@wistronits.com]<br>
 * @版本： [V1.0.0, 2015-1-4]<br>
 * @描述： 图片压缩与旋转公用类<br>
 */
public class PictureUtil {

    // 压缩缓存图临时目录
    public static final String TEMP_DIRECTORY = ".small";
    // 默认图片的宽
    public static final int DEFAULT_PIC_WIDTH = 480;
    // 默认图片的高
    public static final int DEFAULT_PIC_HEIGHT = 800;

    public static final String ALBUM_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/Tchat_Image/";

    /**
     * 把bitmap转换成String
     * 
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    /**
     * 计算图片的缩放值
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
    
    public static Bitmap getSmallBitmap(String filePath) {
        return getSmallBitmap(filePath, DEFAULT_PIC_WIDTH, DEFAULT_PIC_HEIGHT);
    }
    
    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     * 
     * @param imagesrc
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath,int width,int height) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options,
                width,height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int orientation = readPictureDegree(filePath);

        if (Math.abs(orientation) > 0) {
            Bitmap bitmapDegree = rotateBitmap(bitmap, orientation);
            return bitmapDegree;
        } else {
            return bitmap;
        }
    }

    /**
     * 旋转图片
     * 
     * @param imagesrc
     * @return
     */
    public static Bitmap roundBitmap(Bitmap origitBitmap, String path) {
        if (origitBitmap == null || TextUtils.isEmpty(path)) {
            return null;
        }

        int orientation = readPictureDegree(path);

        if (Math.abs(orientation) > 0) {
            Bitmap bitmapDegree = rotateBitmap(origitBitmap, orientation);
            return bitmapDegree;
        } else {
            return origitBitmap;
        }
    }

    /**
     * 根据路径获得返回bitmap用于显示
     * 
     * @param imagesrc
     * @return
     */
//    public static Bitmap getNormalBitmap(String filePath) {
//        if (TextUtils.isEmpty(filePath)) {
//            return null;
//        }
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options,
//                (Integer) MemoryCache.getInstance().getValue(MemoryCache.DeviceWidth),
//                (Integer) MemoryCache.getInstance().getValue(MemoryCache.DeviceHeight));
//
//        System.out.println("options.inSampleSize====" + options.inSampleSize);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
//        int orientation = readPictureDegree(filePath);
//
//        if (Math.abs(orientation) > 0) {
//            Bitmap bitmapDegree = rotateBitmap(bitmap, orientation);
//            return bitmapDegree;
//        } else {
//            return bitmap;
//        }
//    }

    /**
     * 存储bitmap
     * 
     * @param imagesrc
     * @return
     */
    public static String saveBitmapToSdcard(Bitmap bitmap) {

        String fName = DateFormat.format("yyyyMMddhhmmss", new Date())
                .toString() + ".jpg";

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(getAlbumDir(), TEMP_DIRECTORY
                    + fName));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        }
        String path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + TEMP_DIRECTORY + "/" + TEMP_DIRECTORY + fName;
        return path;

    }

    /**
     * 根据路径删除图片
     * 
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录
     * 
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     * 
     * @return
     */
    public static String getAlbumName() {
        return TEMP_DIRECTORY;
    }

    //生成的图片加个后缀
    private static String PIC_SUFFIX = "_smail";
    
    /**
     * @method: getSaveSmallpicPath
     * @Description: 获取保存压缩图路径
     * @param picPath
     * @return 如果异常则直接返回原先大图路径 成功则返回小图路径
     * @throws
     */
//    public static String getSaveSmallpicPath(String picPath) {
//        if (TextUtils.isEmpty(picPath)) {
//            return "";
//        }
//        //File f = new File(picPath);
//        Bitmap bm = PictureUtil.getSmallBitmap(picPath);
//        if(bm == null){
//        	return "";
//        }
//        //放在同一目录下则不需要刷新目录
//        //String filePath = f.getParentFile().getAbsoluteFile()+ "/" + System.currentTimeMillis()+ PIC_SUFFIX +".jpg"; 
//
//        String filePath = getRootPath() + "/" + System.currentTimeMillis()+ PIC_SUFFIX +".jpg"; 
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(filePath);
//            bm.compress(Bitmap.CompressFormat.JPEG, 95, fos);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            filePath = "";
//        } finally {
//            try {
//                if(fos != null){
//                    fos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                filePath = "";
//            }
//        }
////        String path = Environment
////                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
////                + "/" + TEMP_DIRECTORY + "/" + f.getName();
//        return filePath;
//    }
    
//    // 获取日志目录
//    public static String getRootPath() {
//        File sdCardFile = null;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            sdCardFile = Environment.getExternalStorageDirectory();
//        } else {
//            sdCardFile = Environment.getDataDirectory();
//        }
//        
//        String appDataPath = AppAdapter.getInstance().getContext().getString(CPResourceUtil.getStringId("app_data_path"));
//
//        String path = sdCardFile.getPath() + "/" + appDataPath + "/" + TEMP_DIRECTORY;
//
//        //以前的small目录，现在更改为.small，防止被系统相册显示该目录下的图片
//        String oldPath = sdCardFile.getPath() + "/" + appDataPath + "/small";
//        
//        if (!TextUtils.isEmpty(oldPath) && !TextUtils.isEmpty(path)) {
//            File cache = new File(oldPath);
//            File newCash = new File(path);
//            if(cache.exists() && !(newCash.exists())){
//            	cache.renameTo(newCash);
//            }
//        }
//        
//        // make sure create cache path
//        if(!WiCacheTools.createDir(path)) {
//            //创建失败
//            Logi("WiCacheTools -- createDir fail -- path : " + path);
//        }
//
//        Logi("WiCacheTools -- getLogsRootPath -- path : " + path);
//        return path;
//    }
    
//    private static void Logi(String msg) {
//        PLog.print(msg);
//    }
    
    /**
     * @method: createSaveSmallpicPath
     * @Description: 创建并保存小图
     * @param picPath
     * @return 如果异常则直接返回原先大图路径 成功则返回小图路径
     * @throws
     */
    public static String createSaveSmallpicPath(String picPath, int width, int height) {
        if (TextUtils.isEmpty(picPath)) {
            return "";
        }
        Bitmap bm = PictureUtil.getSmallBitmap(picPath, width,height);
        //String filePath = f.getParentFile().getAbsoluteFile() + "/" + System.currentTimeMillis() + ".jpg";
        String filePath = getAlbumDir() + "/" + System.currentTimeMillis() + ".jpg";
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
        } catch(Exception e) {
            e.printStackTrace();
            filePath = "";
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                filePath = "";
            }
        }

        return filePath;
    }
    

    public static String saveFile(Bitmap bm) {
        return saveFile(bm, DateFormat.format("yyyyMMddhhmmss", new Date()).toString() + ".jpg",80);
    }

    /**
     * 保存文件
     * 
     * @param bm
     * @param filePath
     * @throws IOException
     */
    public static String saveFile(Bitmap bm, String filePath,int quality) {
        File myCaptureFile = new File(filePath);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            filePath = "";
        }
        bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        try {
        	if(bos != null){
        		bos.close();
        	}
        } catch (IOException e) {
            e.printStackTrace();
            filePath = "";
        }

        return filePath;
    }

    /**
     * @method: readPictureDegree 判断照片角度
     * @Description:
     * @param path
     * @return
     * @throws
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
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
     * @method: rotateBitmap
     * @Description: 旋转图片
     * @param bitmap
     * @param degress
     * @return
     * @throws
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    private static String compressImage(Bitmap image,String filePath) {
    	if(TextUtils.isEmpty(filePath)){
    		return null;
    	}

    	File f = new File(filePath);
    	String suff = f.getParentFile().getAbsoluteFile()+ "/";
        String lastFilePath = suff+ System.currentTimeMillis()+ PIC_SUFFIX +".jpg"; 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩        
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片

        String ret = saveFile(bitmap, lastFilePath,100);
        return ret ;
    }

    public static String getUploadImagePath(String srcPath) {
    	if(TextUtils.isEmpty(srcPath)){
    		return null;
    	}
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1920f;//这里设置高度为800f
        float ww = 1080f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        return compressImage(bitmap,srcPath);//压缩好比例大小后再进行质量压缩
    }

}
