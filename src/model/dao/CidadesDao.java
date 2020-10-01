package model.dao;

import java.util.List;

import model.entities.Estados;
import model.entities.Cidades;

public interface CidadesDao {

	void insert(Cidades obj);
	void update(Cidades obj);
	void deleteById(Integer id);
	Cidades findById(Integer id);
	List<Cidades> findAll();
	List<Cidades> findByEstados(Estados estados);
}
