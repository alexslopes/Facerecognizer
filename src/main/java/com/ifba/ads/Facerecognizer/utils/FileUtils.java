package com.ifba.ads.Facerecognizer.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

public class FileUtils {

	public static void createFolderIfNotExists(String dirName) throws SecurityException {
    	File theDir = new File(dirName);
    	if (!theDir.exists()) {
    		theDir.mkdir();
    	}
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
		 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		    ImageIO.write(image, "jpg", byteArrayOutputStream);
		    byteArrayOutputStream.flush();
		    return imdecode(new Mat(byteArrayOutputStream.toByteArray()), CV_LOAD_IMAGE_UNCHANGED);
	}
}
