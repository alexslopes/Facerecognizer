package com.ifba.ads.Facerecognizer.utils.JavaCV;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;

import com.ifba.ads.Facerecognizer.utils.File.FileUtils;
import com.ifba.ads.Facerecognizer.utils.paths.Paths;

import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import static org.bytedeco.javacpp.opencv_core.RectVector;

public class DetectFaces {
	
	private static CascadeClassifier faceDetector;
	
	//TODO fazer retornar a face detectada e armazenar em um array
	public static Mat detectFaces(BufferedImage image) throws IOException {
		
		if(faceDetector == null) {
			faceDetector = new CascadeClassifier(Paths.FRONTAL_FACE_CLASSIFIER);
		}
			
		Rect mainFace;
		Mat rgbaMat = FileUtils.BufferedImage2Mat(image);
		Mat greyMat = new Mat();
		cvtColor(rgbaMat, greyMat, CV_BGR2GRAY);
		RectVector faces = new RectVector();
		faceDetector.detectMultiScale(greyMat, faces);
		System.out.println("Faces detectadas: " + faces.size());
		//Aqui pego apenas uma face já que o objetivo e coletar foto com apenas uma pessoa
		mainFace = faces.get(0);
		Mat detectFace = new Mat(greyMat, mainFace);
		resize(detectFace, detectFace, new opencv_core.Size(160, 160));
		//return true;
		return detectFace;
	}
	
	
}
