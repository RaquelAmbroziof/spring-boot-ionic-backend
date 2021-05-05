package com.raquelaf.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raquelaf.cursomc.domain.Categoria;

@Repository
public interface CategoriaResopitory extends JpaRepository<Categoria, Integer>{

}
