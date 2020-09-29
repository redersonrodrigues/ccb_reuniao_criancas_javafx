package model.dao;

import java.util.List;

import model.entities.Grupos;

public interface GruposDao {

	void insert(Grupos obj);
	void update(Grupos obj);
	void deleteById(Integer id);
	Grupos findById(Integer id);
	List<Grupos> findAll();
}
