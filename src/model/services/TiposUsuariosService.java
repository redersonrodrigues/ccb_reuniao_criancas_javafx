package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.TiposUsuariosDao;
import model.entities.TiposUsuarios;

public class TiposUsuariosService {
	
	private TiposUsuariosDao dao = DaoFactory.createTiposUsuariosDao();
	
	public List<TiposUsuarios> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(TiposUsuarios obj) {
		if (obj.getTuser_id() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(TiposUsuarios obj) {
		dao.deleteById(obj.getTuser_id());
	}
	
	public void savaOrUpdate(TiposUsuarios obj) {
		if (obj.getTuser_id() == null) {
			
			dao.insert(obj);
			
		}
		else {
			dao.update(obj);
		}
	}
	
	
	
	
	
}