package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EquipesDao;
import model.entities.Equipes;

public class EquipesService {
	
	private EquipesDao dao = DaoFactory.createEquipesDao();
	
	public List<Equipes> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Equipes obj) {
		if (obj.getEqu_id() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Equipes obj) {
		dao.deleteById(obj.getEqu_id());
	}
	
	public void savaOrUpdate(Equipes obj) {
		if (obj.getEqu_id() == null) {
			
			dao.insert(obj);
			
		}
		else {
			dao.update(obj);
		}
	}
	
	
	
	
	
}