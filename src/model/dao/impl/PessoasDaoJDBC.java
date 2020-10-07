package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.PessoasDao;
import model.entities.Cidades;
import model.entities.Equipes;
import model.entities.Estados;
import model.entities.Grupos;
import model.entities.Pessoas;
import model.entities.TiposUsuarios;

public class PessoasDaoJDBC implements PessoasDao {

	private Connection conn;

	public PessoasDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Pessoas findById(Integer pes_id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT p.*, g.nome as GpNom,g.id as id, c.cid_nome as CidNom, tu.tuser_nome as TuNom, e.equ_nome as EquNom FROM pessoas p, grupo_pessoa gp, grupos g, cidades c, tipos_usuarios tu, equipes e WHERE p.pes_id = ? AND gp.id_grupos = g.id AND p.pes_id = gp.id_pessoas");

			st.setInt(1, pes_id);
			rs = st.executeQuery();
			// if para testar se veio algum resultado
			if (rs.next()) {
				Cidades cidade = instantiateCidades(rs);
				
				Equipes equipe = instantiateEquipes(rs);
				
				Grupos grupo = instantiateGrupos(rs);
				
				TiposUsuarios tipoUsuario = instantiateTiposUsuarios(rs);
				
				Pessoas obj = instantiatePessoas(rs, cidade, equipe, grupo, tipoUsuario);
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

	

	private Pessoas instantiatePessoas(ResultSet rs, Cidades cidade, Equipes equipe, Grupos grupo,
			TiposUsuarios tipoUsuario) throws SQLException {

		Pessoas obj = new Pessoas();
		obj.setPes_id(rs.getInt("pes_id"));
		obj.setPes_nome(rs.getString("pes_nome"));
		obj.setPes_foto(rs.getBytes("pes_foto"));
		obj.setPes_rg(rs.getString("pes_rg"));
		obj.setPes_endereco(rs.getString("pes_endereco"));
		obj.setPes_bairro(rs.getString("pes_bairro"));
		obj.setPes_telefone(rs.getString("pes_telefone"));
		obj.setPes_celular(rs.getString("pes_celular"));
		obj.setCidades(cidade);
		obj.setTiposUsuarios(tipoUsuario);
		obj.setEquipes(equipe);
		obj.setGrupos(grupo);
		return obj;
	}

	private TiposUsuarios instantiateTiposUsuarios(ResultSet rs) throws SQLException {
		TiposUsuarios tipoUsuario = new TiposUsuarios();
		tipoUsuario.setTuser_id(rs.getInt("id_tipos_usuarios"));
		tipoUsuario.setTuser_nome(rs.getString("TuNom"));
		return tipoUsuario;
	}

	private Grupos instantiateGrupos(ResultSet rs) throws SQLException {
		Grupos grupo = new Grupos();
		grupo.setId(rs.getInt("id"));
		grupo.setNome(rs.getString("GpNom"));
		return grupo;
	}

	private Equipes instantiateEquipes(ResultSet rs) throws SQLException {
		Equipes equipe = new Equipes();
		equipe.setEqu_id(rs.getInt("id_equipes"));
		equipe.setEqu_nome(rs.getString("EquNom"));	
		return equipe;
	}

	private Cidades instantiateCidades(ResultSet rs) throws SQLException {
		Cidades cidade = new Cidades();
		cidade.setCid_id(rs.getInt("id_cidades"));
		cidade.setCid_nome(rs.getString("CidNom"));	
		return cidade;
	}
	

	@Override
	public List<Pessoas> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT cidades.*, estados.est_sigla as EstSigla " 
			+ " FROM cidades INNER JOIN estados "
			+ " ON cidades.id_estados =  estados.est_id " 
			+ " ORDER BY cid_nome ");


			rs = st.executeQuery();

			List<Pessoas> list = new ArrayList<>();
			Map<Integer, Grupos> map = new HashMap<>();

			while (rs.next()) {
			
				Grupos grupo = map.get(rs.getInt("id"));

				if (grupo == null) {
					grupo = instantiateGrupos(rs);
					map.put(rs.getInt("id"), grupo);
				}

				Pessoas obj = instantiatePessoas(rs, grupo);
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
	public List<Pessoas> findByCidades(Cidades cidade) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
							"SELECT cidades.*,estados.est_sigla as EstSigla " 
							+ "FROM cidades INNER JOIN estados "
							+ "ON cidades.id_estados = estados.est_id " 
							+ "WHERE estados.est_id = ? " 
							+ "ORDER BY cid_nome");

			st.setInt(1, cidade.getCid_id());

			rs = st.executeQuery();

			List<Pessoas> list = new ArrayList<>();
			Map<Integer, Estados> map = new HashMap<>();

			while (rs.next()) {

				Estados est = map.get(rs.getInt("est_id"));

				if (est == null) {
					est = instantiateEstados(rs);
					map.put(rs.getInt("est_id"), est);
				}

				Pessoas obj = instantiatePessoas(rs, est);
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
	public void insert(Pessoas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO pessoas"
					+ " (pes_nome, pes_foto, pes_rg, pes_endereco, pes_bairro, pes_telefone, pes_celular, id_cidades, id_tipos_usuarios, id_equipes) "
					+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
					,Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getPes_nome());
			st.setInt(2, obj.getCidades().getCid_id());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int pes_id = rs.getInt(1);
					obj.setPes_id(pes_id);
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
	public void update(Pessoas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE cidades " + "SET cid_nome = ?, id_estados = ? " + "WHERE cid_id = ?");

			st.setString(1, obj.getPes_nome());
			st.setInt(2, obj.getCidades().getCid_id());
			st.setInt(3, obj.getPes_id());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer pes_id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM cidades WHERE cid_id = ?");

			st.setInt(1, pes_id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Pessoas> findByEquipe(Equipes equipes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pessoas> findByTiposUsuarios(TiposUsuarios tiposUsuarios) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Pessoas> findByGrupos(Grupos grupos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pessoas findByNome(String nome) {
		// TODO Auto-generated method stub
		return null;
	}
}
