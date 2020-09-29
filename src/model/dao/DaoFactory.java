package model.dao;

import db.DB;
import model.dao.impl.GruposDaoJDBC;

public class DaoFactory {

	
	public static GruposDao createGruposDao() {
		return new GruposDaoJDBC(DB.getConnection());
	}
}
