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
import model.dao.TiposUsuariosDao;
import model.entities.TiposUsuarios;

public class TiposUsuariosDaoJDBC implements TiposUsuariosDao {

	private Connection conn;
	
	public TiposUsuariosDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public TiposUsuarios findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM tipos_usuarios WHERE tuser_id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				TiposUsuarios obj = new TiposUsuarios();
				obj.setTuser_id(rs.getInt("tuser_id"));
				obj.setTuser_nome(rs.getString("tuser_nome"));
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
	public List<TiposUsuarios> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM tipos_usuarios ORDER BY tuser_nome");
			rs = st.executeQuery();

			List<TiposUsuarios> list = new ArrayList<>();

			while (rs.next()) {
				TiposUsuarios obj = new TiposUsuarios();
				obj.setTuser_id(rs.getInt("tuser_id"));
				obj.setTuser_nome(rs.getString("tuser_nome"));
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
	public void insert(TiposUsuarios obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO tipos_usuarios " +
				"(tuser_nome) " +
				"VALUES " +
				"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getTuser_nome());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setTuser_id(id);
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
	public void update(TiposUsuarios obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE tipos_usuarios " +
				"SET tuser_nome = ? " +
				"WHERE tuser_id = ?");

			st.setString(1, obj.getTuser_nome());
			st.setInt(2, obj.getTuser_id());

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
				"DELETE FROM tipos_usuarios WHERE tuser_id = ?");

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
