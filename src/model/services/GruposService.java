package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.GruposDao;
import model.entities.Grupos;

public class GruposService {
	
	private GruposDao dao = DaoFactory.createGruposDao();
	
	public List<Grupos> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Grupos obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Grupos obj) {
		dao.deleteById(obj.getId());
	}
	
	public void savaOrUpdate(Grupos obj) {
		if (obj.getId() == null) {
			
			dao.insert(obj);
			
		}
		else {
			dao.update(obj);
		}
	}
	
	
	
	
	
}