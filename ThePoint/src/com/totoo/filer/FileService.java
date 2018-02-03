package com.totoo.filer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

public class FileService {
	public static Boolean copyMode = true;
	public static Boolean createMode = true;
	public static String copypath;
	public static String newfilepath;

	public static List<person> filelist = new ArrayList<person>();

	public static List<person> getfilepath(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		person entity = new person();
		if (file.getPath() != "/") {
			entity = new person("/", "��Ŀ�");
			entity.pic = 10;
			entity.textColor = Color.BLACK;
			filelist.add(entity);
		}
		if (!file.getParent().equals("/")) {
			entity = new person(file.getParent(), "����");
			entity.pic = 9;
			entity.textColor = Color.BLACK;
			filelist.add(entity);
		}
		for (int i = 0; i < files.length; i++) {
			entity = new person(files[i].getPath(), "");
			if (files[i].isDirectory()) {
				// ���ļ��A��ʱ��
				entity.pic = 3;
				entity.tel = "�ļ��A";
			} else {
				if (entity.name.endsWith(".txt")) {
					entity.pic = 7;
					entity.tel = "�ęn";
				} else if (entity.name.endsWith(".jpg")
						|| entity.name.endsWith(".png")
						|| entity.name.endsWith(".gif")
						|| entity.name.endsWith(".jpeg")) {
					entity.pic = 4;
					entity.tel = "��Ƭ";
				} else if (entity.name.endsWith(".aac")
						|| entity.name.endsWith(".amr")
						|| entity.name.endsWith(".mpeg")
						|| entity.name.endsWith(".mp4")) {
					entity.pic = 5;
					entity.tel = "Ӱ��";
				} else if (entity.name.endsWith(".mp3")) {
					entity.pic = 6;
					entity.tel = "��";
				} else if (entity.name.endsWith(".apk")) {
					entity.pic = 1;
					entity.tel = "ܛ��";
				} else if (entity.name.endsWith(".zip")) {
					entity.pic = 8;
					entity.tel = "���s";
				} else if (entity.name.endsWith(".exe")) {
					entity.pic = 0;
					entity.tel = "����";
				} else {
					entity.pic = 2;
					entity.tel = "�ض�";// 0 R.drawable.ic_launcher,
					// 1R.drawable.apk,
					// 2R.drawable.file,
					// 3R.drawable.folder,
					// 4.drawable.jpg,
					// R5.drawable.mp,
					// R.6drawable.mp3,
					// R.7drawable.txt,
					// R.draw8able.zip,
					// R.drawa9ble.uptohigh,
				}
			}
			filelist.add(entity);
		}
		return filelist;
	}

	public static boolean create(String fn) {
		File file = new File(newfilepath + "/" + fn);

		if (createMode) {
			try {
				file.createNewFile();
				return false;
			} catch (Exception e) {
			}
		} else {

			try {
				file.mkdir();
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	// copy
	public static boolean copy() {
		File file = new File(copypath);
		File newfile = new File(newfilepath);
		if (copyMode == true) {
			try {
				newfile.createNewFile();
				InputStream inputStream = new FileInputStream(file);
				OutputStream outputStream = new FileOutputStream(newfile + "/"
						+ file.getName());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, len);
				}
				inputStream.close();
				outputStream.close();
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			try {
				file.delete();
			} catch (Exception e) {
				return false;
			}
			return true;
		}
	}

}
