package model.dao;

import db.DB;
import model.dao.impl.CidadesDaoJDBC;
import model.dao.impl.EstadosDaoJDBC;
import model.dao.impl.GruposDaoJDBC;

public class DaoFactory {

	
	public static GruposDao createGruposDao() {
		return new GruposDaoJDBC(DB.getConnection());
	}
	
	public static EstadosDao createEstadosDao() {
		return new EstadosDaoJDBC(DB.getConnection());
	}
	

	public static CidadesDao createCidadesDao() {
		return new CidadesDaoJDBC(DB.getConnection());
	}
	
}
