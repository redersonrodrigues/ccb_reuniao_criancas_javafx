package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ReunioesCriancasDao;
import model.entities.Estados;
import model.entities.Pessoas;
import model.entities.ReunioesCriancas;

public class ReunioesCriancasDaoJDBC implements ReunioesCriancasDao {

	private Connection conn;

	public ReunioesCriancasDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public ReunioesCriancas findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT cidades.*, estados.est_sigla as EstSigla " 
			+ " FROM cidades INNER JOIN estados "
			+ " ON cidades.id_estados =  estados.est_id " 
			+ " WHERE cidades.cid_id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Estados est = instantiateEstados(rs);
				ReunioesCriancas obj = instantiateReunioesCriancas(rs, est);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private ReunioesCriancas instantiateReunioesCriancas(ResultSet rs, Estados est) throws SQLException {
		ReunioesCriancas obj = new ReunioesCriancas();
		obj.setCid_id(rs.getInt("cid_id"));
		obj.setCid_nome(rs.getString("cid_nome"));
		obj.setEstados(est);
		return obj;
	}

	private Estados instantiateEstados(ResultSet rs) throws SQLException {
		Estados est = new Estados();
		est.setEst_id(rs.getInt("id_estados"));
		est.setEst_nome(rs.getString("EstSigla"));
		return est;
	}

	@Override
	public List<ReunioesCriancas> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT cidades.*, estados.est_sigla as EstSigla " 
			+ " FROM cidades INNER JOIN estados "
			+ " ON cidades.id_estados =  estados.est_id " 
			+ " ORDER BY cid_nome ");


			rs = st.executeQuery();

			List<ReunioesCriancas> list = new ArrayList<>();
			Map<Integer, Estados> map = new HashMap<>();

			while (rs.next()) {
			
				Estados est = map.get(rs.getInt("id_estados"));

				if (est == null) {
					est = instantiateEstados(rs);
					map.put(rs.getInt("id_estados"), est);
				}

				ReunioesCriancas obj = instantiateReunioesCriancas(rs, est);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	


	@Override
	public List<ReunioesCriancas> findByEstados(Estados estado) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
							"SELECT cidades.*,estados.est_sigla as EstSigla " 
							+ "FROM cidades INNER JOIN estados "
							+ "ON cidades.id_estados = estados.est_id " 
							+ "WHERE estados.est_id = ? " 
							+ "ORDER BY cid_nome");

			st.setInt(1, estado.getEst_id());

			rs = st.executeQuery();

			List<ReunioesCriancas> list = new ArrayList<>();
			Map<Integer, Estados> map = new HashMap<>();

			while (rs.next()) {

				Estados est = map.get(rs.getInt("est_id"));

				if (est == null) {
					est = instantiateEstados(rs);
					map.put(rs.getInt("est_id"), est);
				}

				ReunioesCriancas obj = instantiateReunioesCriancas(rs, est);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	
	}
	
	
	
	
	

	@Override
	public void insert(ReunioesCriancas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO cidades " + "(cid_nome, id_estados)" + "VALUES " + "(?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getCid_nome());
			st.setInt(2, obj.getEstados().getEst_id());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int cid_id = rs.getInt(1);
					obj.setCid_id(cid_id);
				}
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(ReunioesCriancas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE cidades " + "SET cid_nome = ?, id_estados = ? " + "WHERE cid_id = ?");

			st.setString(1, obj.getCid_nome());
			st.setInt(2, obj.getEstados().getEst_id());
			st.setInt(3, obj.getCid_id());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM cidades WHERE cid_id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public ReunioesCriancas findByData(Date data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReunioesCriancas> findByPessoas(Pessoas pessoas) {
		// TODO Auto-generated method stub
		return null;
	}
}
