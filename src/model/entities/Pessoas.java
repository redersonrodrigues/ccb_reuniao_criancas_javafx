package model.entities;

import java.io.Serializable;
import java.util.Arrays;

public class Pessoas implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer pes_id;
	private String pes_nome;
	private byte[] pes_foto;
	private String pes_rg;
	private String pes_endereco;
	private String pes_bairro;
	private String pes_telefone;
	private String pes_celular;
	
	private Cidades cidades;
	private Equipes equipes;
	private Grupos grupos;
	private TiposUsuarios tiposUsuarios;
	
	public Pessoas() {}

	public Pessoas(Integer pes_id, String pes_nome, byte[] pes_foto, String pes_rg, String pes_endereco,
			String pes_bairro, String pes_telefone, String pes_celular, Cidades cidades, Equipes equipes, Grupos grupos,
			TiposUsuarios tiposUsuarios) {
		super();
		this.pes_id = pes_id;
		this.pes_nome = pes_nome;
		this.pes_foto = pes_foto;
		this.pes_rg = pes_rg;
		this.pes_endereco = pes_endereco;
		this.pes_bairro = pes_bairro;
		this.pes_telefone = pes_telefone;
		this.pes_celular = pes_celular;
		this.cidades = cidades;
		this.equipes = equipes;
		this.grupos = grupos;
		this.tiposUsuarios = tiposUsuarios;
	}

	public Integer getPes_id() {
		return pes_id;
	}

	public void setPes_id(Integer pes_id) {
		this.pes_id = pes_id;
	}

	public String getPes_nome() {
		return pes_nome;
	}

	public void setPes_nome(String pes_nome) {
		this.pes_nome = pes_nome;
	}

	public byte[] getPes_foto() {
		return pes_foto;
	}

	public void setPes_foto(byte[] pes_foto) {
		this.pes_foto = pes_foto;
	}

	public String getPes_rg() {
		return pes_rg;
	}

	public void setPes_rg(String pes_rg) {
		this.pes_rg = pes_rg;
	}

	public String getPes_endereco() {
		return pes_endereco;
	}

	public void setPes_endereco(String pes_endereco) {
		this.pes_endereco = pes_endereco;
	}

	public String getPes_bairro() {
		return pes_bairro;
	}

	public void setPes_bairro(String pes_bairro) {
		this.pes_bairro = pes_bairro;
	}

	public String getPes_telefone() {
		return pes_telefone;
	}

	public void setPes_telefone(String pes_telefone) {
		this.pes_telefone = pes_telefone;
	}

	public String getPes_celular() {
		return pes_celular;
	}

	public void setPes_celular(String pes_celular) {
		this.pes_celular = pes_celular;
	}

	public Cidades getCidades() {
		return cidades;
	}

	public void setCidades(Cidades cidades) {
		this.cidades = cidades;
	}

	public Equipes getEquipes() {
		return equipes;
	}

	public void setEquipes(Equipes equipes) {
		this.equipes = equipes;
	}

	public Grupos getGrupos() {
		return grupos;
	}

	public void setGrupos(Grupos grupos) {
		this.grupos = grupos;
	}

	public TiposUsuarios getTiposUsuarios() {
		return tiposUsuarios;
	}

	public void setTiposUsuarios(TiposUsuarios tiposUsuarios) {
		this.tiposUsuarios = tiposUsuarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pes_id == null) ? 0 : pes_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoas other = (Pessoas) obj;
		if (pes_id == null) {
			if (other.pes_id != null)
				return false;
		} else if (!pes_id.equals(other.pes_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pessoas [pes_id=" + pes_id + ", pes_nome=" + pes_nome + ", pes_foto=" + Arrays.toString(pes_foto)
				+ ", pes_rg=" + pes_rg + ", pes_endereco=" + pes_endereco + ", pes_bairro=" + pes_bairro
				+ ", pes_telefone=" + pes_telefone + ", pes_celular=" + pes_celular + ", cidades=" + cidades
				+ ", equipes=" + equipes + ", grupos=" + grupos + ", tiposUsuarios=" + tiposUsuarios + "]";
	}

		
}
