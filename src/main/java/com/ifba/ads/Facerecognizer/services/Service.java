package com.ifba.ads.Facerecognizer.services;

import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bytedeco.javacpp.opencv_core.Mat;

import com.ifba.ads.Facerecognizer.javacv.DetectFaces;
import com.ifba.ads.Facerecognizer.javacv.Recognize;
import com.ifba.ads.Facerecognizer.javacv.Train;
import com.ifba.ads.Facerecognizer.utils.FileUtils;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/upload")
public class Service {
	
	private static final String UPLOAD_FOLDER_PATTERN = "/home/alex/fotos";
	private static final String LOCAL_FACES_DETECTEDS = "/home/alex/eclipse-workspace/Facerecognizer/fotos";

	@POST
	@Path("1")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response training( 
			@FormDataParam("file") InputStream uploadedInputStream,
	        @FormDataParam("file") FormDataContentDisposition fileDetail,
	        @FormDataParam("nome") Integer id) throws IOException {
		
		Mat face = null;
		File dirLocal = null;
		
		
		// check if all form parameters are provided
		if (uploadedInputStream == null || fileDetail == null) {
			return Response.status(400).entity("Invalid form data").build();
		}
				

		// create our destination folder, if it not exists
		try {
			dirLocal = FileUtils.createFolderIfNotExists(UPLOAD_FOLDER_PATTERN + id);
		} catch (SecurityException se) {
			return Response.status(500).entity("Can not create destination folder on server").build();
		}
		
		String uploadedFileLocation = dirLocal.getAbsolutePath() + "/" + fileDetail.getFileName();
		System.out.println(uploadedFileLocation);
		try {
			FileUtils.saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			return Response.status(500).entity("Can not save file").build();
		}
		
		try {
			BufferedImage image = ImageIO.read(new File(uploadedFileLocation));
			face = DetectFaces.detectFaces(image);
			imwrite("/home/alex/eclipse-workspace/Facerecognizer/fotos/pessoa." + id + "." + 1 + ".jpg", face);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(Train.train(FileUtils.getFiles(LOCAL_FACES_DETECTEDS)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).entity("File saved to " + uploadedFileLocation).build();
	}
	
	@POST
	@Path("2")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response recognize( 
			@FormDataParam("file") InputStream uploadedInputStream,
	        @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		
		Mat face = null;
		File dirLocal = null;
		
		// check if all form parameters are provided
		if (uploadedInputStream == null || fileDetail == null) {
			return Response.status(400).entity("Invalid form data").build();
		}
				

		// create our destination folder, if it not exists
		try {
			dirLocal = FileUtils.createFolderIfNotExists(UPLOAD_FOLDER_PATTERN);
		} catch (SecurityException se) {
			return Response.status(500).entity("Can not create destination folder on server").build();
		}
		
		String uploadedFileLocation = dirLocal.getAbsolutePath() + "/" + fileDetail.getFileName();
		try {
			FileUtils.saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			return Response.status(500).entity("Can not save file").build();
		}
		
		try {
			System.out.println(uploadedFileLocation);
			BufferedImage image = ImageIO.read(new File(uploadedFileLocation));
			face = DetectFaces.detectFaces(image);
			imwrite("/home/alex/pessoa."  + 1 + ".jpg", face);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Recognize.recognize(face);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).entity("File saved to " + uploadedFileLocation).build();
	}
}
