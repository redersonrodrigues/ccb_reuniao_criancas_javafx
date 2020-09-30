package model.services;

import java.util.List;

import model.dao.CidadesDao;
import model.dao.DaoFactory;
import model.entities.Cidades;

public class CidadesService {
	
	private CidadesDao dao = DaoFactory.createCidadesDao();
	
	public List<Cidades> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Cidades obj) {
		if (obj.getCid_id() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Cidades obj) {
		dao.deleteById(obj.getCid_id());
	}
	
	
	
	
	
}