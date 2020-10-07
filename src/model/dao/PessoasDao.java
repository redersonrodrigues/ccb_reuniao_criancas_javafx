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
	Pessoas findByNome(String nome);
	List<Pessoas> findAll();
	List<Pessoas> findByEquipe(Equipes equipes);
	List<Pessoas> findByTiposUsuarios(TiposUsuarios tiposUsuarios);
	List<Pessoas> findByCidades(Cidades cidades);
	List<Pessoas> findByGrupos(Grupos grupos);
	
}
