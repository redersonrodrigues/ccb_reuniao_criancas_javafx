package model.dao;

import java.util.List;

import model.entities.Pessoas;
import model.entities.ReunioesCriancas;

public interface ReunioesCriancasDao {

	void insert(ReunioesCriancas obj);
	void update(ReunioesCriancas obj);
	void deleteById(Integer id);
	ReunioesCriancas findById(Integer id);
	ReunioesCriancas findByNome(String nome);
	ReunioesCriancas findByDataReuniao(java.sql.Date date);
	List<ReunioesCriancas> findAll();
	List<ReunioesCriancas> findByPessoa(Pessoas pessoa);
}
