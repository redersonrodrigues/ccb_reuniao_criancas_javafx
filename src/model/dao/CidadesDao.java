package model.dao;

import java.util.List;

import model.entities.Cidades;

public interface CidadesDao {

	void insert(Cidades obj);
	void update(Cidades obj);
	void deleteById(Integer cid_id);
	Cidades findById(Integer cid_id);
	List<Cidades> findAll();
}
