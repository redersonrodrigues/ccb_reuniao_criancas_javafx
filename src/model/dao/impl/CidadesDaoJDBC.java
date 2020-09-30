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
import model.dao.CidadesDao;
import model.entities.Cidades;

public class CidadesDaoJDBC implements CidadesDao {

	private Connection conn;
	
	public CidadesDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Cidades findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM cidades WHERE est_id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Cidades obj = new Cidades();
				obj.setCid_id(rs.getInt("est_id"));
				obj.setCid_nome(rs.getString("nome"));
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
	public List<Cidades> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM cidades ORDER BY cid_nome");
			rs = st.executeQuery();

			List<Cidades> list = new ArrayList<>();

			while (rs.next()) {
				Cidades obj = new Cidades();
				obj.setCid_id(rs.getInt("cid_id"));
				obj.setCid_nome(rs.getString("cid_nome"));
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
	public void insert(Cidades obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO cidades "
			+ "(cid_nome)" 
			+	"VALUES " 
			+ "(?, ?)", 
			Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getCid_nome());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setCid_id(id);
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
	public void update(Cidades obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE cidades "
			+ "SET cid_nome = ?" 
			+"WHERE cid_id = ?");

			st.setString(1, obj.getCid_nome());
			st.setInt(2, obj.getCid_id());

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
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM cidades WHERE cid_id = ?");

			st.setInt(1, id);

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
