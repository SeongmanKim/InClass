/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * @author yushao 
 * CS4500 Senior Project
 * FileCache
 * 
 */
package com.example.inclass;

import java.io.File;

import android.content.Context;
//file cache for picture. 
public class FileCache {
	  private File cacheDir;  
	  /*
	   * new constructor 
	   */
	    public FileCache(Context context) {  
 
	        if (android.os.Environment.getExternalStorageState().equals(  
	                android.os.Environment.MEDIA_MOUNTED))  
	            cacheDir = new File(  
	                    android.os.Environment.getExternalStorageDirectory(),  
	                    "LazyList");  
	        else  
	            cacheDir = context.getCacheDir();  
	        if (!cacheDir.exists())  
	            cacheDir.mkdirs();  
	    }  
	  
	    /*
	     * get the file 
	     */
	    public File getFile(String url) {  
	    
	        String filename = String.valueOf(url.hashCode());  
	        // Another possible solution  
	        // String filename = URLEncoder.encode(url);  
	        File f = new File(cacheDir, filename);  
	        return f;  
	  
	    }  
	    
	    /*
	     * delete it. 
	     */
	    public void clear() {  
	        File[] files = cacheDir.listFiles();  
	        if (files == null)  
	            return;  
	        for (File f : files)  
	            f.delete();  
	    }  

}
