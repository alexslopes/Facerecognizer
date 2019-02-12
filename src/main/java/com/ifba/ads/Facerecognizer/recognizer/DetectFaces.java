package com.ifba.ads.Facerecognizer.recognizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_core.RectVector;

import com.ifba.ads.Facerecognizer.utils.FileUtils;

public class DetectFaces {

	static CascadeClassifier faceDetector = new CascadeClassifier("/home/alex/eclipse-workspace/Facerecognizer/cascades/frontalface.xml");
	
	public static boolean detectFaces(BufferedImage image) throws IOException {
		//TODO converter imagens para mat
		Mat rgbaMat = FileUtils.BufferedImage2Mat(image);
		//TODO verificar a criação do greymat na udemy
		Mat greyMat = new Mat();
		cvtColor(rgbaMat, greyMat, CV_BGR2GRAY);
		RectVector faces = new RectVector();
		faceDetector.detectMultiScale(greyMat, faces);
		System.out.println("Faces detectadas: " + faces.size());
		//TODO extrair faces
		
		return true;
	}
	
	
}
