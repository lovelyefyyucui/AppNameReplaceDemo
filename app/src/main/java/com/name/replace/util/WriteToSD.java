package com.name.replace.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by YF on 2017/8/16.
 */

public class WriteToSD {

    public static String filePath = Environment.getExternalStorageDirectory() + "/Download";

    public static File file;

    public static void WriteToSD(Context context, String fileName){
        if(!isExist(fileName)){
            InputStream inputStream;
            try {
                inputStream = context.getResources().getAssets().open(fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[512];
                int count;
                while((count = inputStream.read(buffer)) > 0){
                    fileOutputStream.write(buffer, 0 ,count);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                LogUtil.e("success");
            } catch (IOException e) {
                LogUtil.e(e, e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public static boolean isExist(String fileName){
        file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(filePath + "/" + fileName);
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }
}
