package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ReunioesCriancasDao;
import model.entities.Pessoas;
import model.entities.ReunioesCriancas;

public class Program2 {

	public static void main(String[] args) {

		ReunioesCriancasDao runioesCriancasDao = DaoFactory.createReunioesCriancasDao();
		
		System.out.println("======= TEST 1: ReunioesCriancas findById ========");
		Pessoas pes = new Pessoas(1, null, null, null, null, null, null, null, null, null, null, null);
		List<ReunioesCriancas> list = runioesCriancasDao.findByPessoa(pes);		
		for (ReunioesCriancas obj : list) {	
		System.out.println(obj);
		}
		
	

	}

}
