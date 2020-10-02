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
import model.dao.EquipesDao;
import model.entities.Equipes;

public class EquipesDaoJDBC implements EquipesDao {

	private Connection conn;
	
	public EquipesDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Equipes findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM equipes WHERE equ_id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Equipes obj = new Equipes();
				obj.setEqu_id(rs.getInt("equ_id"));
				obj.setEqu_nome(rs.getString("equ_nome"));
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
	public List<Equipes> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM equipes ORDER BY equ_nome");
			rs = st.executeQuery();

			List<Equipes> list = new ArrayList<>();

			while (rs.next()) {
				Equipes obj = new Equipes();
				obj.setEqu_id(rs.getInt("equ_id"));
				obj.setEqu_nome(rs.getString("equ_nome"));
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
	public void insert(Equipes obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO equipes " +
				"(equ_nome) " +
				"VALUES " +
				"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getEqu_nome());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setEqu_id(id);
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
	public void update(Equipes obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE equipes " +
				"SET equ_nome = ? " +
				"WHERE equ_id = ?");

			st.setString(1, obj.getEqu_nome());
			st.setInt(2, obj.getEqu_id());

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
				"DELETE FROM equipes WHERE equ_id = ?");

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
