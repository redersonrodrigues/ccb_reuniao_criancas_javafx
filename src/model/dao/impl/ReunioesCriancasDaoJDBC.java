package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DaoFactory;
import model.dao.PessoasDao;
import model.dao.ReunioesCriancasDao;
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
				"SELECT * FROM reunioes WHERE reu_id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				ReunioesCriancas obj = new ReunioesCriancas();
				Pessoas pessoa = new Pessoas();
				obj.setReu_id(rs.getInt("reu_id"));
				obj.setReu_data(rs.getDate("reu_data").toLocalDate());
				obj.setReu_atendimento(rs.getString("reu_atendimento"));
				obj.setReu_tema(rs.getString("reu_tema"));
				obj.setReu_equipe_respons(rs.getString("equipe_respons"));
				obj.setReu_observacoes(rs.getString("reu_observacoes"));
				pessoa.setPes_id(rs.getInt("pes_id"));
				obj.setPessoa(pessoa);
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
	public List<ReunioesCriancas> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT reunioes.*, pessoas.* "
				+ " FROM reunioes "
				+ "INNER JOIN pessoas ON pessoas.pes_id = reunioes.idPessoas "
				+ "ORDER BY reu_data");
			rs = st.executeQuery();

			List<ReunioesCriancas> retorno = new ArrayList<>();

			while (rs.next()) {
				 ReunioesCriancas reuniao = new ReunioesCriancas();
	                Pessoas pessoa = new Pessoas();
	                

	                reuniao.setReu_id(rs.getInt("reu_id"));
	                reuniao.setReu_data(rs.getDate("reu_data").toLocalDate());
	                reuniao.setReu_horario(rs.getString("reu_horario"));
	                reuniao.setReu_atendimento(rs.getString("reu_atendimento"));
	                reuniao.setReu_tema(rs.getString("reu_tema"));
	                reuniao.setReu_equipe_respons(rs.getString("reu_equipe_responsavel"));
	                reuniao.setReu_observacoes(rs.getString("reu_observacoes"));
	                pessoa.setPes_id(rs.getInt("idPessoas"));

	                //Obtendo os dados completos da Pessoa associada a Reunião
	                PessoasDao dao = DaoFactory.createPessoasDao();
	                pessoa = dao.buscar(pessoa);

	               

	                reuniao.setPessoa(pessoa);
	                retorno.add(reuniao);
			}
			return retorno;
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
	public void insert(ReunioesCriancas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO reunioes " +
				"(reu_data, reu_horario, reu_atendimento, reu_tema, reu_equipe_respons, reu_observacoes, idPessoa) " +
				"VALUES " +
				"(?,?,?,?,?,?,?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setDate(1,Date.valueOf(obj.getReu_data()));
			st.setString(1, obj.getReu_horario());
			st.setString(3, obj.getReu_atendimento());
			st.setString(4, obj.getReu_tema());
			st.setString(5, obj.getReu_equipe_respons());
			st.setString(6, obj.getReu_observacoes());
			st.setInt(7, obj.getPessoa().getPes_id());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setReu_id(id);
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
	public void update(ReunioesCriancas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE reunioes " +
				"SET reu_data = ?, reu_atendimento = ?, reu_tema = ?, reu_equipe_respons = ?, reu_observacoes = ?, idPessoa = ? " +
				"WHERE reu_id = ?");

			st.setDate(1, Date.valueOf(obj.getReu_data()));
			st.setInt(2, obj.getReu_id());

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
				"DELETE FROM reunioes WHERE reu_id = ?");

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
