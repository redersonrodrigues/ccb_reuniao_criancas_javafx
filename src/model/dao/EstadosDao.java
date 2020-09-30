package model.dao;

import java.util.List;

import model.entities.Estados;

public interface EstadosDao {

	void insert(Estados obj);
	void update(Estados obj);
	void deleteById(Integer est_id);
	Estados findById(Integer est_id);
	List<Estados> findAll();
}
