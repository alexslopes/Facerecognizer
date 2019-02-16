package com.ifba.ads.Facerecognizer.services;

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

import com.ifba.ads.Facerecognizer.recognizer.DetectFaces;
import com.ifba.ads.Facerecognizer.recognizer.Training;
import com.ifba.ads.Facerecognizer.utils.FileUtils;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/upload")
public class Service {
	
	private static final String UPLOAD_FOLDER = "/home/alex/";

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response training( 
			@FormDataParam("file") InputStream uploadedInputStream,
	        @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		
		// check if all form parameters are provided
		if (uploadedInputStream == null || fileDetail == null) {
			return Response.status(400).entity("Invalid form data").build();
		}
				

		// create our destination folder, if it not exists
		try {
			FileUtils.createFolderIfNotExists(UPLOAD_FOLDER);
		} catch (SecurityException se) {
			return Response.status(500).entity("Can not create destination folder on server").build();
		}
		
		String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
		try {
			FileUtils.saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			return Response.status(500).entity("Can not save file").build();
		}
		
		try {
			System.out.println(uploadedFileLocation);
			BufferedImage image = ImageIO.read(new File(uploadedFileLocation));
			DetectFaces.detectFaces(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(Training.train());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).entity("File saved to " + uploadedFileLocation).build();
	}
}
