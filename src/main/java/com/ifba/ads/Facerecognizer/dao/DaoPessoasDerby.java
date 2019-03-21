package com.ifba.ads.Facerecognizer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.ifba.ads.Facerecognizer.model.Person;

public class DaoPessoasDerby  implements DAOPessoas{

	@Override
	public boolean inserir(Person pessoa) throws Exception {
		Connection conn=ConexaoPessoasDerby.getInstance().getConexao();
        PreparedStatement pst=conn.prepareStatement("insert into JavaCV.pessoas (nome) values (?)");
        pst.setString(1, pessoa.getNome());
        pst.executeUpdate();
        pst.close();
        return true;
	}

	@Override
	public Person buscar(int id) throws Exception {
		Connection conn=ConexaoPessoasDerby.getInstance().getConexao();
        PreparedStatement pst=conn.prepareStatement("select * from JavaCV.pessoas where id=?");
        pst.setString(1, String.valueOf(id));
        ResultSet rs=pst.executeQuery();
        
        Person person = new Person();
        rs.next();
        person.setId(rs.getInt("id"));
        person.setNome(rs.getString("nome"));
  
        return person;
	}

}
