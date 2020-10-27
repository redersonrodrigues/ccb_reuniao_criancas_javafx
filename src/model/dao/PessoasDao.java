package model.dao;

import java.util.List;

import model.entities.Cidades;
import model.entities.Equipes;
import model.entities.Grupos;
import model.entities.Pessoas;
import model.entities.TiposUsuarios;

public interface PessoasDao {

	void insert(Pessoas obj);
	void update(Pessoas obj);
	void deleteById(Integer id);
	Pessoas findById(Integer id);
	Pessoas buscar(Pessoas pessoa);
	List<Pessoas> findAll();
	Pessoas recuperar(int codigo) throws Exception;
	
}
