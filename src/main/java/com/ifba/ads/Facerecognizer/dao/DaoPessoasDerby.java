package com.ifba.ads.Facerecognizer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ifba.ads.Facerecognizer.model.Person;

public class DaoPessoasDerby  implements DAOPessoas{

	@Override
	public boolean inserir(String login, String password, String name) throws Exception {
		Connection conn=ConexaoPessoasDerby.getInstance().getConexao();
        PreparedStatement pst=conn.prepareStatement("insert into JavaCv.User (login,password,name) values (?,?,?)");
        pst.setString(1, login);
        pst.setString(2, password);
        pst.setString(3, name);
        pst.executeUpdate();
        pst.close();
        return true;
	}

	@Override
	public Person buscar(int id) throws Exception {
		Connection conn=ConexaoPessoasDerby.getInstance().getConexao();
        PreparedStatement pst=conn.prepareStatement("select * from JavaCv.User where id=?");
        pst.setString(1, String.valueOf(id));
        ResultSet rs=pst.executeQuery();
        
        Person person = null;
        if(rs.next()) {
        	person = new Person();
	        person.setId(rs.getInt("id"));
	        person.setNome(rs.getString("name"));
        }
  
        return person;
	}

	@Override
	public Person logar(String login, String password) throws Exception {
		Connection conn=ConexaoPessoasDerby.getInstance().getConexao();
        PreparedStatement pst=conn.prepareStatement("select * from JavaCv.User where login=? and password=?");
        pst.setString(1, login);
        pst.setString(2, password);
        
        ResultSet rs=pst.executeQuery();
        
        Person person = null;
        if(rs.next()) {
        	person = new Person();
	        person.setId(rs.getInt("id"));
	        person.setNome(rs.getString("name"));
        }
		return person;
	}

}
