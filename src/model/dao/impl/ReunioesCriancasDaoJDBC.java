package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ReunioesCriancasDao;
import model.entities.Pessoas;
import model.entities.ReunioesCriancas;

public class ReunioesCriancasDaoJDBC implements ReunioesCriancasDao {

	private Connection conn;

	public ReunioesCriancasDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(ReunioesCriancas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO reunioes "
					+ "(reu_data, reu_atendimento, reu_tema, reu_equipe_responsavel, reu_observacoes, id_pessoa) "
					+ "VALUES (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setDate(1, new java.sql.Date( obj.getReu_data().getTime()));
			st.setString(2, obj.getReu_atendimento());
			st.setString(3, obj.getReu_tema());
			st.setString(4, obj.getReu_equipe_respons());
			st.setString(5, obj.getReu_observacoes());
			st.setInt(6, obj.getPessoas().getPes_id());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int reu_id = rs.getInt(1);
					obj.setReu_id(reu_id);
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
			st = conn.prepareStatement("UPDATE reunioes "
					+ "SET reu_data = ?, reu_atendimento = ?, reu_tema = ?, reu_equipe_responsavel = ?, reu_observacoes = ?, id_pessoa = ? "
					+ "WHERE reu_id = ?");

			st.setDate(1, new java.sql.Date( obj.getReu_data().getTime()));
			st.setString(2, obj.getReu_atendimento());
			st.setString(3,obj.getReu_tema());
			st.setString(4, obj.getReu_equipe_respons());
			st.setString(5, obj.getReu_observacoes());
			st.setInt(6, obj.getPessoas().getPes_id());
			st.setInt(7, obj.getReu_id());

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
			st = conn.prepareStatement("DELETE FROM reunioes WHERE reu_id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<ReunioesCriancas> findByPessoa(Pessoas pessoa) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT reunioes.*, pessoas.pes_id as PesId, pessoas.pes_nome as PesNome "
							+ "FROM reunioes INNER JOIN pessoas "
							+ "ON reunioes.id_pessoa =  pessoas.pes_id "
							+ "WHERE pessoas.pes_id = ? "
							+ "ORDER BY pessoas.pes_nome");

			st.setInt(1, pessoa.getPes_id());

			rs = st.executeQuery();
			
			List<ReunioesCriancas> list = new ArrayList<>();
			Map<Integer, Pessoas> map = new HashMap<>();

			while (rs.next()) {
				
				Pessoas pes = map.get(rs.getInt("PesId"));
				
				if (pes == null) {
				pes = instantiatePessoas(rs);
				map.put(rs.getInt("PesId"), pes);
				}
				
				ReunioesCriancas obj = instantiateReunioesCriancas(rs, pes);
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

	private ReunioesCriancas instantiateReunioesCriancas(ResultSet rs, Pessoas pes) throws SQLException {
		ReunioesCriancas obj = new ReunioesCriancas();
		obj.setReu_id(rs.getInt("reu_id"));
		
		obj.setReu_data(rs.getDate("reu_data"));
		obj.setReu_atendimento(rs.getString("reu_atendimento"));
		obj.setReu_tema(rs.getString("reu_tema"));
		obj.setReu_equipe_respons(rs.getString("reu_equipe_responsavel"));
		obj.setReu_observacoes(rs.getString("reu_observacoes"));
		obj.setPessoas(pes);
		return obj;
	}

	private Pessoas instantiatePessoas(ResultSet rs) throws SQLException {
		Pessoas pes = new Pessoas();
		pes.setPes_id(rs.getInt("PesId"));
		pes.setPes_nome(rs.getString("PesNome"));
		return pes;
	}

	@Override
	public List<ReunioesCriancas> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT reunioes.*, pessoas.pes_id as PesId, pessoas.pes_nome as PesNome "
							+ "FROM reunioes INNER JOIN pessoas "
							+ "ON reunioes.id_pessoa =  pessoas.pes_id "
							+ "ORDER BY pessoas.pes_nome");
			
			rs = st.executeQuery();
			
			List<ReunioesCriancas> list = new ArrayList<>();
			Map<Integer, Pessoas> map = new HashMap<>();

			while (rs.next()) {
				
				Pessoas pes = map.get(rs.getInt("PesId"));
				
				if (pes == null) {
				pes = instantiatePessoas(rs);
				map.put(rs.getInt("PesId"), pes);
				}
				
				ReunioesCriancas obj = instantiateReunioesCriancas(rs, pes);
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
	public ReunioesCriancas findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT reunioes.*, pessoas.pes_id as PesId, pessoas.pes_nome as PesNome "
			+"FROM reunioes INNER JOIN pessoas "
			+"ON reunioes.id_pessoa =  pessoas.pes_id "
			+"WHERE reunioes.reu_id = ? "
			+"ORDER BY PesNome ");

			st.setInt(1, id);
			rs = st.executeQuery();
			// if para testar se veio algum resultado
			if (rs.next()) {
				Pessoas pes = instantiatePessoas(rs);
				
				ReunioesCriancas obj = instantiateReunioesCriancas(rs, pes);
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

	@Override
	public ReunioesCriancas findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT reunioes.*, pessoas.pes_id as PesId, pessoas.pes_nome as PesNome "
			+"FROM reunioes INNER JOIN pessoas "
			+"ON reunioes.id_pessoa =  pessoas.pes_id "
			+"WHERE pessoas.pes_nome = ? "
			+"ORDER BY PesNome ");

			st.setString(1, nome);
			rs = st.executeQuery();
			// if para testar se veio algum resultado
			if (rs.next()) {
				Pessoas pes = instantiatePessoas(rs);
				
				ReunioesCriancas obj = instantiateReunioesCriancas(rs, pes);
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

	@Override
	public ReunioesCriancas findByDataReuniao(Date data) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT reunioes.*, pessoas.pes_id as PesId, pessoas.pes_nome as PesNome "
			+"FROM reunioes INNER JOIN pessoas "
			+"ON reunioes.id_pessoa =  pessoas.pes_id "
			+"WHERE reunioes.reu_data = ? "
			+"ORDER BY PesNome ");
			
			st.setDate(1, data);
			rs = st.executeQuery();
			
			
			
			// if para testar se veio algum resultado
			if (rs.next()) {
				Pessoas pes = instantiatePessoas(rs);
				
				ReunioesCriancas obj = instantiateReunioesCriancas(rs, pes);
				return obj;
			}
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

}
