package com.ifba.ads.Facerecognizer.services;

import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bytedeco.javacpp.opencv_core.Mat;

import com.ifba.ads.Facerecognizer.dao.DAOPessoas;
import com.ifba.ads.Facerecognizer.dao.DaoPessoasDerby;
import com.ifba.ads.Facerecognizer.model.Person;
import com.ifba.ads.Facerecognizer.utils.File.FileUtils;
import com.ifba.ads.Facerecognizer.utils.JavaCV.DetectFaces;
import com.ifba.ads.Facerecognizer.utils.JavaCV.Recognize;
import com.ifba.ads.Facerecognizer.utils.JavaCV.Train;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/upload")
public class Service {
	
	private static final String UPLOAD_FOLDER_PATTERN = "/home/alex/fotos";
	private static final String LOCAL_FACES_DETECTEDS = "/home/alex/Code Projects/eclipse-workspace/Facerecognizer/fotos";

	
	@POST
	@Path("register")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response register( 
			@FormDataParam("name") String name) throws IOException {
		
		Person person = new Person();
		person.setNome(name);
		
		DAOPessoas derby = new DaoPessoasDerby();
		
		try {
			derby.inserir(person);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(200).entity("Não foi possível salvar os dados").build();
		}
		
		return Response.status(200).entity("Dados salvos").build();
	}
	
	@POST
	@Path("training")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response training( 
			@FormDataParam("file") InputStream uploadedInputStream,
	        @FormDataParam("file") FormDataContentDisposition fileDetail,
	        @FormDataParam("id") Integer id) throws IOException {
		
		System.out.println(id);
		
		Mat face = null;
		File dirLocal = null;		
		// TODO evitar escrever em arquivo caso o valor do id seja null
		// check if all form parameters are provided
		if (uploadedInputStream == null || fileDetail == null || id == null) {
			return Response.status(400).entity("Dados incompletos").build();
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
		// TODO substituir 1 pela qunatidade de fotos tiradas
		try {
			BufferedImage image = ImageIO.read(new File(uploadedFileLocation));
			face = DetectFaces.detectFaces(image);
			imwrite("/home/alex/Code Projects/eclipse-workspace/Facerecognizer/fotos/pessoa." + id + "." + 1 + ".jpg", face);
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

		//return Response.status(200).entity("File saved to " + uploadedFileLocation).build();
		return Response.status(200).entity("Imagem recebida").build();
	}
	
	@POST
	@Path("recognize")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String recognize( 
			@FormDataParam("file") InputStream uploadedInputStream,
	        @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		
		Mat face = null;
		File dirLocal = null;
		String personId = null;
		String nome = null;
		DAOPessoas derby = new DaoPessoasDerby();
		Person pessoa = new Person();
		
		// check if all form parameters are provided
		if (uploadedInputStream == null || fileDetail == null) {
			return "Invalid form data";
		}
				

		// create our destination folder, if it not exists
		try {
			dirLocal = FileUtils.createFolderIfNotExists(UPLOAD_FOLDER_PATTERN);
		} catch (SecurityException se) {
			return "Can not create destination folder on server";
		}
		
		String uploadedFileLocation = dirLocal.getAbsolutePath() + "/" + fileDetail.getFileName();
		try {
			FileUtils.saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			return "Can not save file";
		}
		
		try {
			System.out.println(uploadedFileLocation);
			BufferedImage image = ImageIO.read(new File(uploadedFileLocation));
			face = DetectFaces.detectFaces(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			personId = Recognize.recognize(face);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
		try {
			pessoa = derby.buscar(Integer.parseInt(personId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(pessoa.getNome());
		
		if(personId != null)
			return "Face reconhecida: " + personId;
		else
			return "Face não reconhecida";
	}
}
