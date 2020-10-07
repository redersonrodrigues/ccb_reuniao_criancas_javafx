package application;

import model.dao.DaoFactory;
import model.dao.PessoasDao;
import model.entities.Pessoas;

public class Program {

	public static void main(String[] args) {

		PessoasDao pessoasDao = DaoFactory.createPessoasDao();
		
		System.out.println("======= TEST 1: Pessoas findById ========");
		Pessoas pessoa = pessoasDao.findById(1);
		
		System.out.println(pessoa);
	}

}
