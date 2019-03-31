package com.ifba.ads.Facerecognizer.dao;


import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoPessoasDerby {

	 private static ConexaoPessoasDerby conexao;

	    private ConexaoPessoasDerby() throws Exception {
	        setConexao();
	    }
	    
	    public static ConexaoPessoasDerby getInstance() throws Exception{
	        if (conexao ==null){
	            conexao = new ConexaoPessoasDerby();
	        }
	       return conexao;
	    }
	    
	    private Connection connection;
	    private void setConexao() throws Exception{
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaCv","root","camamu");
	        connection =conn;
	    }
	    
	    public Connection getConexao(){
	        return connection;
	    }
}
