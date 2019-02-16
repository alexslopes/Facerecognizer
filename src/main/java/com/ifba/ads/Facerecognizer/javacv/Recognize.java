package com.ifba.ads.Facerecognizer.javacv;

import java.io.File;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_face;

public class Recognize {
	
	public static void recognize(Mat face) {
		opencv_face.FaceRecognizer recognizer =  opencv_face.EigenFaceRecognizer.create();
		File f = new File(Training.EIGEN_FACES_CLASSIFIER);
        recognizer.read(f.getAbsolutePath());
        IntPointer label = new IntPointer(1);
        DoublePointer confiability = new DoublePointer(1);
        recognizer.predict(face, label, confiability);
        
        int predict = label.get(0);
        String status;

        if(predict == -1){
            status = "Desconhecido";
        }else{
            status = "Conhecido";
        }
        
        System.out.println(status);;
	}

}
