package model.services;

import java.util.List;

import model.dao.ReunioesCriancasDao;
import model.dao.DaoFactory;
import model.entities.ReunioesCriancas;

public class ReunioesCriancasService {
	
	private ReunioesCriancasDao dao = DaoFactory.createReunioesCriancasDao();
	
	public List<ReunioesCriancas> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(ReunioesCriancas obj) {
		if (obj.getReu_id() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(ReunioesCriancas obj) {
		dao.deleteById(obj.getReu_id());
	}
	
	
	
	
	
}