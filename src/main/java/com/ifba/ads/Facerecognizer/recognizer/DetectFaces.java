package com.ifba.ads.Facerecognizer.recognizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import static org.bytedeco.javacpp.opencv_core.RectVector;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

import com.ifba.ads.Facerecognizer.utils.FileUtils;

public class DetectFaces {

	static CascadeClassifier faceDetector = new CascadeClassifier("/home/alex/eclipse-workspace/Facerecognizer/cascades/frontalface.xml");
	//TODO fazer retornar a face detectada e armazenar em um array
	public static boolean detectFaces(BufferedImage image) throws IOException {
		Rect mainFace;
		Mat rgbaMat = FileUtils.BufferedImage2Mat(image);
		Mat greyMat = new Mat();
		cvtColor(rgbaMat, greyMat, CV_BGR2GRAY);
		RectVector faces = new RectVector();
		faceDetector.detectMultiScale(greyMat, faces);
		System.out.println("Faces detectadas: " + faces.size());
		//Aqui pego apenas uma face já que o objetivo e coletar fot com apenas uma pessoa
		mainFace = faces.get(0);
		Mat detectFace = new Mat(greyMat, mainFace);
		resize(detectFace, detectFace, new opencv_core.Size(160, 160));
		imwrite("/home/alex/pessoa." + 1 + "." + 1 + ".jpg", detectFace);
		
		return true;
	}
	
	
}
