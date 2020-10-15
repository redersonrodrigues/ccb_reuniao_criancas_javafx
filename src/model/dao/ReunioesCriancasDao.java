package model.dao;

import java.util.Date;
import java.util.List;

import model.entities.Pessoas;
import model.entities.ReunioesCriancas;

public interface ReunioesCriancasDao {

	void insert(ReunioesCriancas obj);
	void update(ReunioesCriancas obj);
	void deleteById(Integer id);
	List<ReunioesCriancas> findAll();
	List<ReunioesCriancas> findByDataReuniao(Date data);
	List<ReunioesCriancas> findByPessoa(Pessoas pessoa);
}
