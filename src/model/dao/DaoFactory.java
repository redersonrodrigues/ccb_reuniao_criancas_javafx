package model.dao;

import db.DB;
import model.dao.impl.CidadesDaoJDBC;
import model.dao.impl.EquipesDaoJDBC;
import model.dao.impl.EstadosDaoJDBC;
import model.dao.impl.GruposDaoJDBC;
import model.dao.impl.PessoasDaoJDBC;
import model.dao.impl.ReunioesCriancasDaoJDBC;
import model.dao.impl.TiposUsuariosDaoJDBC;

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
	
	public static TiposUsuariosDao createTiposUsuariosDao() {
		return new TiposUsuariosDaoJDBC(DB.getConnection());
	}

	public static EquipesDao createEquipesDao() {
		return new EquipesDaoJDBC(DB.getConnection());
	}
	
	public static PessoasDao createPessoasDao() {
		return new PessoasDaoJDBC(DB.getConnection());
	}
	
	public static ReunioesCriancasDao createReunioesCriancasDao() {
		return new ReunioesCriancasDaoJDBC(DB.getConnection());
	}
	
}
