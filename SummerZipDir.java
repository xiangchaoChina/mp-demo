package com.zl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SummerZipDir {
	public static void main(String[] args) throws Exception {
		// 需要压缩的文件
		String path = "E://android";
		// 压缩到
		String zipPath = "E://android.zip";
		deep(new File(path),path.length());
		for (String zipEntry : SummerZipDir.zipPath) {
			String source = path+"/"+zipEntry;
			zipFile(new File(source), new File(zipPath), zipEntry);
		}
		SummerZipDir.close();
	}
	private static List<String> zipPath = new ArrayList<String>();//需要压缩的路径
	private static ZipOutputStream zis = null;//压缩流
	//关闭压缩流
	public static void close(){
		try {
			zis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void deep(File file,int index){
		String path= file.getAbsolutePath();
		if(file.isDirectory()){
			if(path.length()>index){
				zipPath.add(path.substring(index,path.length()));
			}
			File[] files = file.listFiles();
			if(files!=null && files.length!=0){
				for (File item : files) {
					deep(item,index);
				}
			}
		}else{
			if(path.length()>index){
				zipPath.add(path.substring(index,path.length()));
			}
		}
	}
	private static void zipFile(File file,File zipFile,String entryPath) throws Exception{
		if(file.isFile()){
			//准备流
			InputStream is = new FileInputStream(file);
			if(zis==null){
				zis = new ZipOutputStream(new FileOutputStream(zipFile));
			}
			/**
			 * 压缩
			 */
			zis.putNextEntry(new ZipEntry(entryPath));//设置压缩条目
			//开始
			int ava = is.available();
			byte[] bdata = new byte[2048];
			while(is.read(bdata)!=-1){
				zis.write(bdata,0,ava>2048?2048:ava);
				ava = is.available();
			}
			is.close();
		}
	}
}
