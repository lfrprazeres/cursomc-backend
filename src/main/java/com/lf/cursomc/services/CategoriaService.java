package com.lf.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.lf.cursomc.domain.Categoria;
import com.lf.cursomc.repositories.CategoriaRepository;
import com.lf.cursomc.services.exception.DataIntegrityException;
import com.lf.cursomc.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repositorio;

	/* usa o recurso GET setado no CategoriaResources para buscar categoria pelo ID,
	 * orElseThrow é usado como padrão no .findById, retornando algo caso não consiga achar o ID,
	 * nesse caso é retornado um ObjectNotFoundException (feito manualmente) com a mensagem de erro */
	public Categoria find(Integer id) {
		Categoria obj = repositorio.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
		return obj;
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repositorio.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		/* Aproveitando o código de cima para verificar se esse id existe, caso ele exista lançar uma exceção */
		find(obj.getId());
		return repositorio.save(obj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repositorio.deleteById(id);	
		} catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possua produtos");
		}
	}

	public List<Categoria> findAll() {
		return repositorio.findAll();
	}
	
}
