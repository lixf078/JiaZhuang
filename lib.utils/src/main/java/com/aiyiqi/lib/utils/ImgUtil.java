package com.aiyiqi.lib.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.aiyiqi.decoration.lib.constants.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hubing on 16/4/21.
 */
public class ImgUtil {

    private static String saveFilePath = "";
    private static String saveFileName = "";

    public static void isImageDownloaded(final Context context, final Uri loadUri, final LoadCacheFileListener listener) {
        if (loadUri == null) {
            listener.loadResult("", null, false);
            return;
        }
        Logger.e(Constants.TAG, "isImageDownloaded loadUri " + loadUri.toString());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future = Glide.with(context)
                        .load(loadUri)
                        .downloadOnly(256, 256);
                File file = null;
                try {
                    file = future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                saveCacheFileToPng(context, file, listener);

            }
        });
        thread.start();

//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
//        return ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey);
    }

    //return file or null
    public static File getCachedImageOnDisk(Context context, Uri loadUri) {
        File localFile = null;
        if (loadUri != null) {

            FutureTarget<File> future = Glide.with(context)
                    .load(loadUri)
                    .downloadOnly(256, 256);
            try {
                localFile = future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
//            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
//                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
//                localFile = ((FileBinaryResource) resource).getFile();
//            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
//                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
//                localFile = ((FileBinaryResource) resource).getFile();
//            }
//
            if(localFile!=null){
                localFile = saveCacheFileToPng(context, localFile, null);
            }
        }
        return localFile;
    }

    public static File saveCacheFileToPng(Context context, File f, LoadCacheFileListener listener)  {
        File file = new File(com.aiyiqi.lib.utils.AppUtil.DOWNLOAD_PATH + System.currentTimeMillis() + ".png");
        if (file.exists()) {
            file.delete();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
            bos = new BufferedOutputStream(new FileOutputStream(file));

            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = bis.read(bytes)) != -1){
                bos.write(bytes, 0 , len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        saveFilePath = file.getAbsolutePath();
        saveFileName = file.getName();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, saveFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DESCRIPTION, "aiyiqi");
        values.put(MediaStore.Images.Media.DATA, saveFilePath);
        Logger.e(Constants.TAG, "saveFilePath " + saveFilePath + ", saveFileName " + saveFileName);
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        listener.loadResult(saveFilePath, file, file == null ? false : true);
        return file;
    }

    public static File saveFileToPng(File f)  {
        File file = new File(AppUtil.DOWNLOAD_PATH + "tmp.png");
        if (file.exists()) {
            file.delete();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
            bos = new BufferedOutputStream(new FileOutputStream(file));

            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = bis.read(bytes)) != -1){
                bos.write(bytes, 0 , len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    public static String saveFileToPng(String path){
        Bitmap bitmap = decodeBitmap(path);
        String p = null;
        if(bitmap!=null){
             p = saveMyBitmap(bitmap);
        }

        return p;
    }

    private static Bitmap decodeBitmap(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (bitmap == null){
            System.out.println("bitmap为空");
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        Logger.e(Constants.TAG,"真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 计算缩放比
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);
        if (scale <= 0)
        {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Logger.e(Constants.TAG,"缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }

    private static String saveMyBitmap(Bitmap mBitmap){
        File f = new File(AppUtil.DOWNLOAD_PATH + "tmp.png");
        if(f.exists()){
            Logger.e(Constants.TAG,"saveMyBitmap >> file >> delete : "+f.delete());
        }
        Logger.e(Constants.TAG,"saveMyBitmap >> file >> exists : "+f.exists());
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.e(Constants.TAG,"在保存图片时出错："+e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            Logger.e(Constants.TAG,"在保存图片时出错：",e);
        }
        try {
            fOut.close();
        } catch (IOException e) {
            Logger.e(Constants.TAG,"在保存图片时出错：",e);
        }

        return f.getAbsolutePath();
    }

    public static interface LoadCacheFileListener{
        public void loadResult(String path, File file, boolean result);
    }


    private void saveFile(Context context, File file, String fileName) throws IOException {
        File dirFile = new File(AppUtil.DOWNLOAD_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(AppUtil.DOWNLOAD_PATH + fileName);

        saveFilePath = myCaptureFile.getAbsolutePath();
        saveFileName = fileName;

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));

        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }

        bos.flush();
        bos.close();
        bis.close();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, saveFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DESCRIPTION, "aiyiqi");
        values.put(MediaStore.Images.Media.DATA, saveFilePath);
        Logger.e(Constants.TAG, "saveFilePath " + saveFilePath + ", saveFileName " + saveFileName);
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

}
