package com.ifba.ads.Facerecognizer.dao;

import com.ifba.ads.Facerecognizer.model.Person;

public interface DAOPessoas {
	
	public boolean inserir(Person pessoa) throws Exception;
	public Person buscar(int id) throws Exception;
}
