package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.ReunioesCriancasDao;
import model.entities.Pessoas;
import model.entities.ReunioesCriancas;

public class Program2 {

	public static void main(String[] args) {

		ReunioesCriancasDao reunioesCriancasDao = DaoFactory.createReunioesCriancasDao();
		
		System.out.println("======= TEST 1: ReunioesCriancas findByPessoa ========");
		Pessoas pes = new Pessoas(1, null, null, null, null, null, null, null, null, null, null, null);
		List<ReunioesCriancas> list = reunioesCriancasDao.findByPessoa(pes);		
		for (ReunioesCriancas obj : list) {	
		System.out.println(obj);
		}
		System.out.println("\n");
		
		System.out.println("======= TEST 2: ReunioesCriancas findAll ========");
		list = reunioesCriancasDao.findAll();		
		for (ReunioesCriancas obj : list) {	
		System.out.println(obj);
		}
		System.out.println("\n");

		System.out.println("======= TEST 3: ReunioesCriancas insert ========");
		ReunioesCriancas newReunioesCriancas = new ReunioesCriancas(null, new Date(), "irmão Igor", "Paixão de Cristo", "Equipe B", "nenhuma observação", pes);
				reunioesCriancasDao.insert(newReunioesCriancas);
		System.out.println("Inserted! New Id = " + newReunioesCriancas.getReu_id());
		
		System.out.println("\n");

		

	}

}
