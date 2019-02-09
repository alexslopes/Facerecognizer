package com.ifba.ads.Facerecognizer.recognizer;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;

public class Training {
	
	public static final int IMG_SIZE = 160;

    public static final String EIGEN_FACES_CLASSIFIER = "eigenFacesClassifier.yml";
    public static final String FISHER_FACES_CLASSIFIER = "fisherFacesClassifier.yml";
    public static final String LBPH_FACES_CLASSIFIER = "lbphFacesClassifier.yml";
    
public static boolean train() throws Exception{
    	
    	File photosFolder = new File("src\\fotos");
        if (!photosFolder.exists()) return false;

        FilenameFilter imageFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
            }
        };
        
        File[] files = photosFolder.listFiles(imageFilter);
        MatVector photos = new MatVector(files.length);
        Mat labels = new Mat(files.length, 1, CV_32SC1);
        IntBuffer rotulosBuffer = labels.createBuffer();
        int counter = 0;
        for (File image : files) {
            Mat photo = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            int classe = Integer.parseInt(image.getName().split("\\.")[1]);
            resize(photo, photo, new Size(IMG_SIZE, IMG_SIZE));
            photos.put(counter, photo);
            rotulosBuffer.put(counter, classe);
            counter++;
        }
        
        FaceRecognizer eigenfaces = opencv_face.EigenFaceRecognizer.create();
        eigenfaces.train(photos, labels);
        File f = new File(photosFolder, EIGEN_FACES_CLASSIFIER);
        f.createNewFile();
        eigenfaces.save(f.getAbsolutePath());
        return true;
    	
    }

}
