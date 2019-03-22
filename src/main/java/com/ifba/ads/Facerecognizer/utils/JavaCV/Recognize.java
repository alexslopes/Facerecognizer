package com.ifba.ads.Facerecognizer.utils.JavaCV;

import java.io.File;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_face;

import com.ifba.ads.Facerecognizer.utils.paths.Paths;

public class Recognize {
	
	public static String recognize(Mat face) {
		opencv_face.FaceRecognizer recognizer =  opencv_face.EigenFaceRecognizer.create();
		File f = new File(Paths.EIGEN_FACES_CLASSIFIER);
        recognizer.read(f.getAbsolutePath());
        IntPointer label = new IntPointer(1);
        DoublePointer confiability = new DoublePointer(1);
        recognizer.predict(face, label, confiability);
        //Aqui pego o primeiro index contidfo na label, já que so irei usar uma face para testes
        //TODO fazer retornar todas as faces detectadas
        int predict = label.get(0);
        String status = null;

        if(predict != -1){
            status = Integer.toString(predict);
        }
        
        System.out.println(status);
        
        return status;
	}

}
