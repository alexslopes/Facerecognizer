package com.ifba.ads.Facerecognizer.utils.paths;

import java.io.File;
import java.net.URL;

public class Paths {

    public static final String EIGEN_FACES_CLASSIFIER = Paths.class.getClassLoader().getResource("eigenFacesClassifier.yml").getPath();
    public static final String FRONTAL_FACE_CLASSIFIER = Paths.class.getClassLoader().getResource("frontalface.xml").getPath();
    public static final String FISHER_FACES_CLASSIFIER = "fisherFacesClassifier.yml";
    public static final String LBPH_FACES_CLASSIFIER = "lbphFacesClassifier.yml";
    public static final String UPLOAD_FOLDER_PATTERN = Paths.class.getClassLoader().getResource(".").getPath() + "uploadPhotos";
	public static final String LOCAL_FACES_DETECTEDS = Paths.class.getClassLoader().getResource(".").getPath() + "detectFaces";
	
}
