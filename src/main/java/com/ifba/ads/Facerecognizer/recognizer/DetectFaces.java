package com.ifba.ads.Facerecognizer.recognizer;

import java.awt.image.BufferedImage;
import java.io.File;

import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;

public class DetectFaces {

	CascadeClassifier faceDetector = new CascadeClassifier("cascades/frontalface.xml");
	
	public boolean detectFaces(BufferedImage image) {
		//TODO converter imagens para mat
		//TODO extrair faces
		
		return true;
	}
	
	
}
