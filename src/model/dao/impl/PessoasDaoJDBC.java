package model.dao.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import javafx.scene.image.Image;
import model.dao.PessoasDao;
import model.entities.Cidades;
import model.entities.Equipes;
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
					"SELECT pessoas.*, cidades.cid_nome, equipes.equ_nome as EquNom, grupos.gru_id, grupos.gru_nome, tipos_usuarios.tuser_nome as TuNom "
					+ "FROM pessoas "
					+ "INNER JOIN cidades ON cidades.cid_id = pessoas.id_cidades "
					+ "INNER JOIN equipes ON equipes.equ_id = pessoas.id_equipes "
					+ "INNER JOIN grupos ON grupos.gru_id = pessoas.id_grupos "
					+ "INNER JOIN tipos_usuarios ON tipos_usuarios.tuser_id = pessoas.id_tipos_usuarios "
					+ "WHERE pessoas.pes_id = ? "
			);

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
		obj.setPes_rg(rs.getString("pes_rg"));
		obj.setPes_pai(rs.getString("pes_pai"));
		obj.setPes_mae(rs.getString("pes_mae"));
		obj.setPes_endereco(rs.getString("pes_endereco"));
		obj.setPes_bairro(rs.getString("pes_bairro"));
		obj.setPes_telefone(rs.getString("pes_telefone"));
		obj.setPes_celular(rs.getString("pes_celular"));
		obj.setPes_observacoes(rs.getString("pes_observacoes"));
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
		grupo.setGru_id(rs.getInt("gru_id"));
		grupo.setGru_nome(rs.getString("gru_nome"));
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
		cidade.setCid_nome(rs.getString("cid_nome"));	
		return cidade;
	}
	

	@Override
	public List<Pessoas> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pessoas.*, cidades.cid_nome, equipes.equ_nome as EquNom, grupos.gru_id, grupos.gru_nome, tipos_usuarios.tuser_nome as TuNom "
							+ "FROM pessoas "
							+ "INNER JOIN cidades ON cidades.cid_id = pessoas.id_cidades "
							+ "INNER JOIN equipes ON equipes.equ_id = pessoas.id_equipes "
							+ "INNER JOIN grupos ON grupos.gru_id = pessoas.id_grupos "
							+ "INNER JOIN tipos_usuarios ON tipos_usuarios.tuser_id = pessoas.id_tipos_usuarios "
							+ "ORDER BY pessoas.pes_nome "
					);


			rs = st.executeQuery();

			List<Pessoas> list = new ArrayList<>();
			Map<Integer, Grupos> map = new HashMap<>();

			while (rs.next()) {
			
				Grupos grupo = map.get(rs.getInt("pes_id"));

				if (grupo == null) {
					grupo = instantiateGrupos(rs);
					map.put(rs.getInt("pes_id"), grupo);
				}

				Equipes equipe = instantiateEquipes(rs);
				
				Cidades cidade = instantiateCidades(rs);
				
				TiposUsuarios tipoUsuario = instantiateTiposUsuarios(rs);
				
				Pessoas obj = instantiatePessoas(rs, cidade, equipe, grupo, tipoUsuario);
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
	public List<Pessoas> findByCidades(Cidades cid) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pessoas.*, grupos.gru_nome, grupos.gru_id, cidades.cid_nome as CidNom, tipos_usuarios.tuser_nome as TuNom, equipes.equ_nome as EquNom "
							+ "FROM pessoas "
							+ "INNER JOIN grupos ON grupos.gru_id = pessoas.id_grupos "
							+ "INNER JOIN cidades ON cidades.cid_id = pessoas.id_cidades "
							+ "INNER JOIN tipos_usuarios ON tipos_usuarios.tuser_id = pessoas.id_tipos_usuarios "
							+ "INNER JOIN equipes ON equipes.equ_id = pessoas.id_equipes "
							+ "WHERE pessoas.id_cidades = ? "
							+ "ORDER BY pessoas.pes_nome ");   
			
			st.setInt(1, cid.getCid_id());
			rs = st.executeQuery();
			
			List<Pessoas> list = new ArrayList<>();
			Map<Integer, Cidades> map = new HashMap<Integer, Cidades>();
			
			// if para testar se veio algum resultado
			while (rs.next()) {
				
				Cidades cidade = map.get(rs.getInt("id_cidades"));
				
				if(cidade == null) {
					cidade = instantiateCidades(rs);
					map.put(rs.getInt("id_cidades"), cidade);
				}
				
				
				Equipes equipe = instantiateEquipes(rs);
				
				Grupos grupo = instantiateGrupos(rs);
				
				TiposUsuarios tipoUsuario = instantiateTiposUsuarios(rs);
				
				Pessoas obj = instantiatePessoas(rs, cidade, equipe, grupo, tipoUsuario);
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
					"INSERT INTO pessoas "
					+ " (pes_nome, pes_rg, pes_pai, pes_mae, pes_endereco, pes_bairro, pes_telefone, pes_celular, id_cidades, id_grupos, id_equipes, id_tipos_usuarios, pes_observacoes) "
					+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
					, Statement.RETURN_GENERATED_KEYS);	
												
					

			st.setString(1, obj.getPes_nome());
			st.setString(2, obj.getPes_rg());
			st.setString(3, obj.getPes_pai());
			st.setString(4, obj.getPes_mae());
			st.setString(5, obj.getPes_endereco());
			st.setString(6, obj.getPes_bairro());
			st.setString(7, obj.getPes_telefone());
			st.setString(8, obj.getPes_celular());
			st.setInt(9, obj.getCidades().getCid_id());
			st.setInt(10, obj.getGrupos().getGru_id());
			st.setInt(11, obj.getEquipes().getEqu_id());
			st.setInt(12, obj.getTiposUsuarios().getTuser_id());
			st.setString(13, obj.getPes_observacoes());
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
			st = conn.prepareStatement("UPDATE pessoas "
					+ "SET pes_nome = ?, pes_rg = ?, pes_pai = ?, pes_mae = ?, pes_endereco = ?, pes_bairro = ?, pes_telefone = ?, pes_celular = ?, id_cidades = ?, id_grupos = ?, id_equipes = ?, id_tipos_usuarios = ?, pes_observacoes = ? "
					+ "WHERE pes_id = ?");

			st.setString(1, obj.getPes_nome());
			st.setString(2, obj.getPes_rg());
			st.setString(3, obj.getPes_pai());
			st.setString(4, obj.getPes_mae());
			st.setString(5, obj.getPes_endereco());
			st.setString(6, obj.getPes_bairro());
			st.setString(7, obj.getPes_telefone());
			st.setString(8, obj.getPes_celular());
			st.setInt(9, obj.getCidades().getCid_id());
			st.setInt(10, obj.getGrupos().getGru_id());
			st.setInt(11, obj.getEquipes().getEqu_id());
			st.setInt(12, obj.getTiposUsuarios().getTuser_id());
			st.setString(13, obj.getPes_observacoes());
			st.setInt(14, obj.getPes_id());

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
			st = conn.prepareStatement("DELETE FROM pessoas WHERE pes_id = ?");

			st.setInt(1, pes_id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Pessoas> findByEquipe(Equipes equ) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT pessoas.*, grupos.gru_nome, grupos.gru_id, cidades.cid_nome as CidNom, tipos_usuarios.tuser_nome as TuNom, equipes.equ_nome as EquNom "
							+ "FROM pessoas "
							+ "INNER JOIN grupos ON grupos.gru_id = pessoas.id_grupos "
							+ "INNER JOIN cidades ON cidades.cid_id = pessoas.id_cidades "
							+ "INNER JOIN tipos_usuarios ON tipos_usuarios.tuser_id = pessoas.id_tipos_usuarios "
							+ "INNER JOIN equipes ON equipes.equ_id = pessoas.id_equipes "
							+ "WHERE pessoas.id_equipes = ? "
							+ "ORDER BY pessoas.pes_nome ");      
			
			st.setInt(1, equ.getEqu_id());
			rs = st.executeQuery();
			
			List<Pessoas> list = new ArrayList<>();
			Map<Integer, Equipes> map = new HashMap<Integer, Equipes>();
			
			// if para testar se veio algum resultado
			while (rs.next()) {
				
				Equipes equipe = map.get(rs.getInt("id_equipes"));
				
				if(equipe == null) {
					equipe = instantiateEquipes(rs);
					map.put(rs.getInt("id_equipes"), equipe);
				}
				
				
				Cidades cidade = instantiateCidades(rs);
				
				Grupos grupo = instantiateGrupos(rs);
				
				TiposUsuarios tipoUsuario = instantiateTiposUsuarios(rs);
				
				Pessoas obj = instantiatePessoas(rs, cidade, equipe, grupo, tipoUsuario);
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
	public List<Pessoas> findByTiposUsuarios(TiposUsuarios tus) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT pessoas.*, grupos.gru_nome, grupos.gru_id, cidades.cid_nome as CidNom, tipos_usuarios.tuser_nome as TuNom, equipes.equ_nome as EquNom "
			+ "FROM pessoas "
			+ "INNER JOIN grupos ON grupos.gru_id = pessoas.id_grupos "
			+ "INNER JOIN cidades ON cidades.cid_id = pessoas.id_cidades "
			+ "INNER JOIN tipos_usuarios ON tipos_usuarios.tuser_id = pessoas.id_tipos_usuarios "
			+ "INNER JOIN equipes ON equipes.equ_id = pessoas.id_equipes "
			+ "WHERE pessoas.id_tipos_usuarios = ? "
			+ "ORDER BY pessoas.pes_nome ");   
			
			st.setInt(1, tus.getTuser_id());
			rs = st.executeQuery();
			
			List<Pessoas> list = new ArrayList<>();
			Map<Integer, TiposUsuarios> map = new HashMap<Integer, TiposUsuarios>();
			
			// if para testar se veio algum resultado
			while (rs.next()) {
				
				TiposUsuarios tipoUsuario = map.get(rs.getInt("id_tipos_usuarios"));
				
				if(tipoUsuario == null) {
					tipoUsuario = instantiateTiposUsuarios(rs);
					map.put(rs.getInt("id_tipos_usuarios"), tipoUsuario);
				}
				
				
				Equipes equipe = instantiateEquipes(rs);
				
				Grupos grupo = instantiateGrupos(rs);
				
				Cidades cidade = instantiateCidades(rs);
				
				Pessoas obj = instantiatePessoas(rs, cidade, equipe, grupo, tipoUsuario);
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
	public List<Pessoas> findByGrupos(Grupos gru) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT pessoas.*, grupos.gru_nome, grupos.gru_id, cidades.cid_nome as CidNom, tipos_usuarios.tuser_nome as TuNom, equipes.equ_nome as EquNom "
			+ "FROM pessoas "
			+ "INNER JOIN grupos ON grupos.gru_id = pessoas.id_grupos "
			+ "INNER JOIN cidades ON cidades.cid_id = pessoas.id_cidades "
			+ "INNER JOIN tipos_usuarios ON tipos_usuarios.tuser_id = pessoas.id_tipos_usuarios "
			+ "INNER JOIN equipes ON equipes.equ_id = pessoas.id_equipes "
			+ "WHERE pessoas.id_grupos = ? "
			+ "ORDER BY pessoas.pes_nome");   
			
			st.setInt(1, gru.getGru_id());
			rs = st.executeQuery();
			
			List<Pessoas> list = new ArrayList<>();
			Map<Integer, Grupos> map = new HashMap<Integer, Grupos>();
			
			// if para testar se veio algum resultado
			while (rs.next()) {
				
				Grupos grupo = map.get(rs.getInt("gru_id"));
				
				if(grupo == null) {
					grupo = instantiateGrupos(rs);
					map.put(rs.getInt("gru_id"), grupo);
				}
				
				
				Equipes equipe = instantiateEquipes(rs);
				
				Cidades cidade = instantiateCidades(rs);
				
				TiposUsuarios tipoUsuario = instantiateTiposUsuarios(rs);
				
				Pessoas obj = instantiatePessoas(rs, cidade, equipe, grupo, tipoUsuario);
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
	public Pessoas findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT pessoas.*, grupos.gru_nome, grupos.gru_id, cidades.cid_nome as CidNom, tipos_usuarios.tuser_nome as TuNom, equipes.equ_nome as EquNom "
			+ "FROM pessoas "
			+ "INNER JOIN grupos ON grupos.gru_id = pessoas.id_grupos "
			+ "INNER JOIN cidades ON cidades.cid_id = pessoas.id_cidades "
			+ "INNER JOIN tipos_usuarios ON tipos_usuarios.tuser_id = pessoas.id_tipos_usuarios "
			+ "INNER JOIN equipes ON equipes.equ_id = pessoas.id_equipes "
			+ "WHERE pessoas.pes_nome = ?");

			st.setString(1, nome);
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
}
