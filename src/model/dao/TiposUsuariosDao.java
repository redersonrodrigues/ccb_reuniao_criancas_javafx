package model.dao;

import java.util.List;

import model.entities.TiposUsuarios;

public interface TiposUsuariosDao {

	void insert(TiposUsuarios obj);
	void update(TiposUsuarios obj);
	void deleteById(Integer id);
	TiposUsuarios findById(Integer id);
	List<TiposUsuarios> findAll();
}
