package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Pessoas;
import model.entities.ReunioesCriancas;

public interface ReunioesCriancasDao {

	void insert(ReunioesCriancas obj);
	void update(ReunioesCriancas obj);
	void deleteById(Integer id);
	ReunioesCriancas findByData(Date data);
	List<ReunioesCriancas> findAll();
	List<ReunioesCriancas> findByPessoas(Pessoas pessoas);
}
