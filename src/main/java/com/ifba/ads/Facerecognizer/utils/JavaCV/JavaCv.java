package com.ifba.ads.Facerecognizer.utils.JavaCV;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;

import com.ifba.ads.Facerecognizer.utils.File.FileUtils;
import com.ifba.ads.Facerecognizer.utils.paths.Paths;

public class JavaCv {
	
	private static CascadeClassifier faceDetector;
	private static opencv_face.FaceRecognizer recognizer; 
	private static final int IMG_SIZE = 160;
	public static JavaCv javacv;
	
	private JavaCv() {
		setFaceDetector(Paths.FRONTAL_FACE_CLASSIFIER);
		setRecognizer(Paths.EIGEN_FACES_CLASSIFIER);
		
	}
	
	public static JavaCv getInstance() {
		if(javacv == null) {
			javacv = new JavaCv();
		}
		return javacv;
	}

	public CascadeClassifier getFaceDetector() {
		return faceDetector;
	}

	public void setFaceDetector(String path) {
		faceDetector = new CascadeClassifier(path);
	}

	public opencv_face.FaceRecognizer getRecognizer() {
		return recognizer;
	}

	public void setRecognizer(String path) {
		recognizer =  opencv_face.EigenFaceRecognizer.create();
		File file = new File(path);
		recognizer.read(file.getAbsolutePath());
	}

	public List<Mat> detectFaces(BufferedImage image) throws IOException {
		
		List<Mat> detectFaces = new ArrayList<>();
		
		Mat rgbaMat = FileUtils.BufferedImage2Mat(image);
		Mat greyMat = new Mat();
		cvtColor(rgbaMat, greyMat, CV_BGR2GRAY);
		RectVector faces = new RectVector();
		faceDetector.detectMultiScale(greyMat, faces);
		System.out.println("Faces detectadas: " + faces.size());
		
		for(int i = 0 ;i < faces.size(); i++) {
			Rect mainFace;
			mainFace = faces.get(0);
			Mat detectFace = new Mat(greyMat, mainFace);
			resize(detectFace, detectFace, new opencv_core.Size(160, 160));
			detectFaces.add(detectFace);
		}
		
		return detectFaces;
	}
	
	public String recognize(Mat face) {
							
        IntPointer label = new IntPointer(1);
        DoublePointer confiability = new DoublePointer(1);
        recognizer.predict(face, label, confiability);
        int predict = label.get(0);
        String status = null;

        if(predict != -1){
            status = Integer.toString(predict);
        }
                
        return status;
	}
	
    
	public boolean train(File[] files) throws Exception{
	    
	        MatVector photos = new MatVector(files.length);
	        Mat labels = new Mat(files.length, 1, CV_32SC1);
	        IntBuffer rotulosBuffer = labels.createBuffer();
	        int counter = 0;
	        for (File image : files) {
	            Mat photo = imread(image.getAbsolutePath(), IMREAD_GRAYSCALE);
	            int classe = Integer.parseInt(image.getName().split("\\.")[1]);
	            resize(photo, photo, new Size(IMG_SIZE, IMG_SIZE));
	            photos.put(counter, photo);
	            rotulosBuffer.put(counter, classe);
	            counter++;
	        }
	        
	        recognizer.train(photos, labels);
	        File f = new File(Paths.EIGEN_FACES_CLASSIFIER);
	        f.createNewFile();
	        recognizer.save(f.getAbsolutePath());
	        return true;
	    	
	    }


}
