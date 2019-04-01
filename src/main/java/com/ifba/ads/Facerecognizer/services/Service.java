package com.ifba.ads.Facerecognizer.services;

import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import com.ifba.ads.Facerecognizer.utils.JavaCV.JavaCv;
import com.ifba.ads.Facerecognizer.utils.paths.Paths;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

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
			return Response.status(200).entity(derby.inserir(login,password,name)).build();
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
		
		File dirLocal = null;
		File faceDir = null;
		
		
			System.out.println(getClass().getClassLoader().getResource(".").getPath());
		

		DAOPessoas derby = new DaoPessoasDerby();
		JavaCv javacv = JavaCv.getInstance();
		Person person = null;
		
		try {
			person = derby.logar(login, password);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (uploadedInputStream == null || fileDetail == null || person == null) {
			return Response.status(400).entity("Dados incompletos/incorretos").build();
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
			List<Mat> faces;
			faces = javacv.detectFaces(image);
			if(faces.size() > 1)
				return Response.status(200).entity("Foto com mais de ua face, porfavor insira uma foto com apenas um rosto").build();
			
			System.out.println(imwrite(faceDir.getAbsolutePath() + "/person." + person.getId() + "." + (FileUtils.qtdPhotosById(faceDir, person.getId()) + 1) + ".jpg", faces.get(0)));
			FileUtils.deleteFilesInAFolder(dirLocal); 
			dirLocal.delete();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			javacv.train(FileUtils.getFiles(Paths.LOCAL_FACES_DETECTEDS));
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
	public List<Person> recognize( 
			@FormDataParam("file") InputStream uploadedInputStream,
	        @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
		
		List<Mat> faces = null;
		List<String> idList = null;
		List<Person> personList = null;
		File dirLocal = null;
		String nome = null;
		DAOPessoas derby = new DaoPessoasDerby();
		JavaCv javacv = JavaCv.getInstance();
		
		// check if all form parameters are provided
		if (uploadedInputStream == null || fileDetail == null) {
			System.out.println("Parâmetros incompletos");
		}
				

		// create our destination folder, if it not exists
		try {
			dirLocal = FileUtils.createFolderIfNotExists(Paths.UPLOAD_FOLDER_PATTERN);
		} catch (SecurityException se) {
			se.printStackTrace();
		}
		
		String uploadedFileLocation = dirLocal.getAbsolutePath() + "/" + fileDetail.getFileName();
		try {
			FileUtils.saveToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println(uploadedFileLocation);
			BufferedImage image = ImageIO.read(new File(uploadedFileLocation));
			faces = javacv.detectFaces(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			for(Mat face : faces) {
				idList = new ArrayList<>();
				String id = javacv.recognize(face);
				if(id != null)
					idList.add(id);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
		try {
			for(String id : idList) {
				personList = new ArrayList<>();
				Person person = derby.buscar(Integer.parseInt(id));
				if(person != null)
					personList.add(person);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return personList;
	}
}
