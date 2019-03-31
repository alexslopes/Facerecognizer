package com.ifba.ads.Facerecognizer.utils.File;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;

public class FileUtils {

	public static File createFolderIfNotExists(String dirName) throws SecurityException {
    	File theDir = new File(dirName);
    	if (!theDir.exists()) {
    		theDir.mkdir();
    	}
    	
    	return theDir;
    }
	
	public static void saveToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];

		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}
	
	public static Mat BufferedImage2Mat(BufferedImage image) throws IOException {
		OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
        return cv.convertToMat(new Java2DFrameConverter().convert(image)); 
	}
	
	public static File[] getFiles(String local) {
		File photosFolder = new File(local);
        if (!photosFolder.exists()) return null;

        FilenameFilter imageFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
            }
        };
        
        return photosFolder.listFiles(imageFilter);
	}
	
	public static int qtdPhotosById(File dir, int id) {
		int cont = 0;
        if (dir.exists()) {
            FilenameFilter imageFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
                }
            };

            File[] files = dir.listFiles(imageFilter);
            
            for(File file : files) {
            	if(Integer.parseInt(file.getName().split("\\.")[1]) == id)
            		cont++;
            }
            
            return files != null ? cont : 0;
        }
        return 0;
    }
	
	public static void deleteFilesInAFolder(File dir) {
		String[]entries = dir.list();
		for(String s: entries){
		    File currentFile = new File(dir.getPath(),s);
		    currentFile.delete();
		}
	}
}
