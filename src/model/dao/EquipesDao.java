package model.dao;

import java.util.List;

import model.entities.Equipes;

public interface EquipesDao {

	void insert(Equipes obj);
	void update(Equipes obj);
	void deleteById(Integer id);
	Equipes findById(Integer id);
	List<Equipes> findAll();
}
