package model.dao;

import java.util.List;

import model.entities.ReunioesCriancas;

public interface ReunioesCriancasDao {

	void insert(ReunioesCriancas obj);
	void update(ReunioesCriancas obj);
	void deleteById(Integer id);
	ReunioesCriancas findById(Integer id);
	List<ReunioesCriancas> findAll();
	
	
}
