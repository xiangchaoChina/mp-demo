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
		// ��Ҫѹ�����ļ�
		String path = "E://android";
		// ѹ����
		String zipPath = "E://android.zip";
		deep(new File(path),path.length());
		for (String zipEntry : SummerZipDir.zipPath) {
			String source = path+"/"+zipEntry;
			zipFile(new File(source), new File(zipPath), zipEntry);
		}
		SummerZipDir.close();
	}
	private static List<String> zipPath = new ArrayList<String>();//��Ҫѹ����·��
	private static ZipOutputStream zis = null;//ѹ����
	//�ر�ѹ����
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
			//׼����
			InputStream is = new FileInputStream(file);
			if(zis==null){
				zis = new ZipOutputStream(new FileOutputStream(zipFile));
			}
			/**
			 * ѹ��
			 */
			zis.putNextEntry(new ZipEntry(entryPath));//����ѹ����Ŀ
			//��ʼ
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
