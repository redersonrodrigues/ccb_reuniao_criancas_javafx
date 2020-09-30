package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EstadosDao;
import model.entities.Estados;

public class EstadosService {
	
	private EstadosDao dao = DaoFactory.createEstadosDao();
	
	public List<Estados> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Estados obj) {
		if (obj.getEst_id() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Estados obj) {
		dao.deleteById(obj.getEst_id());
	}
	
	
	
	
	
}