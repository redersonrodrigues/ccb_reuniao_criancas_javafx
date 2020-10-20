package model.services;

import java.util.List;

import model.dao.PessoasDao;
import model.dao.DaoFactory;
import model.entities.Pessoas;

public class PessoasService {
	
	private PessoasDao dao = DaoFactory.createPessoasDao();
	
	public List<Pessoas> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Pessoas obj) {
		if (obj.getPes_id() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Pessoas obj) {
		dao.deleteById(obj.getPes_id());
	}
	
	
	
	
	
}