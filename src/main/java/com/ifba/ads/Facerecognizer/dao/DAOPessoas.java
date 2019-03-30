package com.ifba.ads.Facerecognizer.dao;

import com.ifba.ads.Facerecognizer.model.Person;

public interface DAOPessoas {
	
	public boolean inserir(String login, String password, String name) throws Exception;
	public Person logar(String login, String password) throws Exception;
	public Person buscar(int id) throws Exception;
}
