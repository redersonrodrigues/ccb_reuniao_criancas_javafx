package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Grupos;

public class GruposService {
	
	
	public List<Grupos> findAll(){
		List<Grupos> list = new ArrayList<>();
		
		list.add(new Grupos(1,"Colaboradores"));
		list.add(new Grupos(2, "Crian�as"));
		list.add(new Grupos(3, "Minist�rio"));
		return list;
	}
	

}
