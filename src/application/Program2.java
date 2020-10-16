package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.ReunioesCriancasDao;
import model.entities.Pessoas;
import model.entities.ReunioesCriancas;

public class Program2 {

	public static void main(String[] args)  {
		Scanner sc = new Scanner(System.in);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		ReunioesCriancasDao reunioesCriancasDao = DaoFactory.createReunioesCriancasDao();
		
		System.out.println("======= TEST 1: ReunioesCriancas findById ========");
		ReunioesCriancas res = new ReunioesCriancas();
		res = reunioesCriancasDao.findById(1);		
		System.out.println(res);
		System.out.println("\n");
			
		
		System.out.println("======= TEST 2: ReunioesCriancas findByNome ========");
		res = reunioesCriancasDao.findByNome("MARTA FRANCISCA DOS SANTOS RODRIGUES");	
		System.out.println(res);
		System.out.println("\n");
		
		System.out.println("======= TEST 3: ReunioesCriancas findByData ========");
		try {
			res = reunioesCriancasDao.findByDataReuniao(new java.sql.Date(sdf.parse("01/10/2020").getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(res);
		System.out.println("\n");
		
		
		System.out.println("======= TEST 4: ReunioesCriancas findAll ========");
		List<ReunioesCriancas> list = reunioesCriancasDao.findAll();		
		for (ReunioesCriancas obj : list) {	
		System.out.println(obj);
		}
		System.out.println("\n");

		System.out.println("======= TEST 5: ReunioesCriancas findByPessoa ========");
		Pessoas pes = new Pessoas(1,null,null,null,null,null,null,null,null,null,null,null);
		list = reunioesCriancasDao.findByPessoa(pes);
		for (ReunioesCriancas obj : list) {
			System.out.println(obj);
		}
		System.out.println("\n");
		
		
		System.out.println("======= TEST 6: ReunioesCriancas insert ========");
		reunioesCriancasDao.insert(res);
		System.out.println("Inserted! New Id = " + res.getReu_id());
		System.out.println("\n");

		System.out.println("======= TEST 7: ReunioesCriancas update ========");
		res = reunioesCriancasDao.findById(3);
		res.setReu_observacoes("Alterado via metodo update reformulado");
		reunioesCriancasDao.update(res);
		System.out.println("Update - completed");
		System.out.println("\n");

		System.out.println("======= TEST 8: ReunioesCriancas delete ========");
		System.out.println("Entre com o codigo para o teste de deleção:");
		int id = sc.nextInt();
		reunioesCriancasDao.deleteById(id);
		System.out.println("Delete - completed");
		System.out.println("\n");
		
		
	}

}
