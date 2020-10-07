package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PessoasDao;
import model.entities.Cidades;
import model.entities.Equipes;
import model.entities.Grupos;
import model.entities.Pessoas;
import model.entities.TiposUsuarios;

public class Program {

	public static void main(String[] args) {

		PessoasDao pessoasDao = DaoFactory.createPessoasDao();
		
		System.out.println("======= TEST 1: Pessoas findById ========");
		Pessoas pessoa = pessoasDao.findById(1);		
		System.out.println(pessoa);
		
		System.out.println("\n ======= TEST 2: Pessoas findByCidades ========");
		Cidades cidade = new Cidades(1, null, null);
		List<Pessoas> list = pessoasDao.findByCidades(cidade);
		for (Pessoas obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n");

		System.out.println("\n ======= TEST 3: Pessoas findByTiposUsuarios ========");
		TiposUsuarios tipoUsuario = new TiposUsuarios(1, null);
		List<Pessoas> list2 = pessoasDao.findByTiposUsuarios(tipoUsuario);
		for (Pessoas obj : list2) {
			System.out.println(obj);
		}
		System.out.println("\n");

		System.out.println("\n ======= TEST 4: Pessoas findByGrupos ========");
		Grupos grupo = new Grupos(1, null);
		List<Pessoas> list3 = pessoasDao.findByGrupos(grupo);
		for (Pessoas obj : list3) {
			System.out.println(obj);
		}
		System.out.println("\n");

		System.out.println("======= TEST 5: Pessoas findByNome ========");
		Pessoas pessoa1 = pessoasDao.findByNome("Marta Francisca dos Santos Rodrigues");		
		System.out.println(pessoa1);
		System.out.println("\n");
	
		System.out.println("\n ======= TEST 6: Pessoas findByEquipes ========");
		Equipes equipe = new Equipes(1, null);
		List<Pessoas> list4 = pessoasDao.findByEquipe(equipe);
		for (Pessoas obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n");

	}

}
