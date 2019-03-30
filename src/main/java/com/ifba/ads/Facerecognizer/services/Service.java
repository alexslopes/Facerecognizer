package com.ifba.ads.Facerecognizer.services;

import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
import com.ifba.ads.Facerecognizer.utils.paths.Paths;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.research.ws.wadl.Resource;

@Path("/upload")
public class Service {
	
	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response register( 
			@FormParam("login") String login,
			@FormParam("password") String password,
			@FormParam("name") String name) throws IOException {
		
		Person person = new Person();
		person.setNome(name);
		
		DAOPessoas derby = new DaoPessoasDerby();
		
		try {
			if(derby.inserir(login,password,name));
				return Response.status(200).entity("Dados salvos").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(200).entity("Não foi possível salvar os dados").build();
		}
	}
	
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_JSON})
	public Person login( 
			@FormParam("login") String login,
			@FormParam("password") String password) throws IOException {

		DAOPessoas derby = new DaoPessoasDerby();
		Person person = null;
		
		try {
			person = derby.logar(login, password);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return person;
	}
	
	
	@POST
	@Path("training")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response training( 
			@FormDataParam("file") InputStream uploadedInputStream,
	        @FormDataParam("file") FormDataContentDisposition fileDetail,
	        @FormDataParam("login") String login,
	        @FormDataParam("password") String password) throws IOException {
		
		
		Mat face = null;
		File dirLocal = null;
		File faceDir = null;
		

		DAOPessoas derby = new DaoPessoasDerby();
		Person person = null;
		
		try {
			person = derby.logar(login, password);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (uploadedInputStream == null || fileDetail == null || person == null) {
			return Response.status(400).entity("Dados incompletos").build();
		}


		// create our destination folder, if it not exists
		try {
			dirLocal = FileUtils.createFolderIfNotExists(Paths.UPLOAD_FOLDER_PATTERN + person.getId());
			faceDir = FileUtils.createFolderIfNotExists(Paths.LOCAL_FACES_DETECTEDS);
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
			
			System.out.println(imwrite(faceDir.getAbsolutePath() + "/person." + person.getId() + "." + (FileUtils.qtdPhotosById(faceDir, person.getId()) + 1) + ".jpg", face));
			FileUtils.deleteFilesInAFolder(dirLocal); 
			dirLocal.delete();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(Train.train(FileUtils.getFiles(Paths.LOCAL_FACES_DETECTEDS)));
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
			dirLocal = FileUtils.createFolderIfNotExists(Paths.UPLOAD_FOLDER_PATTERN);
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
			return personId;
		else
			return "Face não reconhecida";
	}
}
