package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.EstadosDao;
import model.entities.Estados;

public class EstadosDaoJDBC implements EstadosDao {

	private Connection conn;
	
	public EstadosDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Estados findById(Integer est_id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM estados WHERE est_id = ?");
			st.setInt(1, est_id);
			rs = st.executeQuery();
			if (rs.next()) {
				Estados obj = new Estados();
				obj.setEst_id(rs.getInt("est_id"));
				obj.setEst_nome(rs.getString("est_nome"));
				obj.setEst_sigla(rs.getString("est_sigla"));
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Estados> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM estados ORDER BY est_nome");
			rs = st.executeQuery();

			List<Estados> list = new ArrayList<>();

			while (rs.next()) {
				Estados obj = new Estados();
				obj.setEst_id(rs.getInt("est_id"));
				obj.setEst_nome(rs.getString("est_nome"));
				obj.setEst_sigla(rs.getString("est_sigla"));
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void insert(Estados obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO estados "
			+ "(est_nome, est_sigla)" 
			+	"VALUES " 
			+ "(?, ?)", 
			Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getEst_nome());
			st.setString(2, obj.getEst_sigla());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int est_id = rs.getInt(1);
					obj.setEst_id(est_id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Estados obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE estados "
			+ "SET est_nome = ?, est_sigla = ? " 
			+"WHERE est_id = ?");

			st.setString(1, obj.getEst_nome());
			st.setString(2, obj.getEst_sigla());
			st.setInt(3, obj.getEst_id());

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer est_id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM estados WHERE est_id = ?");

			st.setInt(1, est_id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
}
