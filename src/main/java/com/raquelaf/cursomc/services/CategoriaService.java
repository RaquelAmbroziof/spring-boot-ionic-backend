package com.raquelaf.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raquelaf.cursomc.domain.Categoria;
import com.raquelaf.cursomc.repositories.CategoriaResopitory;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaResopitory repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElse(null);
	}

}
