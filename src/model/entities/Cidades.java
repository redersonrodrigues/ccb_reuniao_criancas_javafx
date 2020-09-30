package model.entities;

import java.io.Serializable;

public class Cidades  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer cid_id;
	private String cid_nome;
	
	private Estados estados;

	
	public Cidades() {}


	public Cidades(Integer cid_id, String cid_nome, Estados estados) {
		this.cid_id = cid_id;
		this.cid_nome = cid_nome;
		this.estados = estados;
	}


	public Integer getCid_id() {
		return cid_id;
	}


	public void setCid_id(Integer cid_id) {
		this.cid_id = cid_id;
	}


	public String getCid_nome() {
		return cid_nome;
	}


	public void setCid_nome(String cid_nome) {
		this.cid_nome = cid_nome;
	}


	public Estados getEstados() {
		return estados;
	}


	public void setEstados(Estados estados) {
		this.estados = estados;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid_id == null) ? 0 : cid_id.hashCode());
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
		Cidades other = (Cidades) obj;
		if (cid_id == null) {
			if (other.cid_id != null)
				return false;
		} else if (!cid_id.equals(other.cid_id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Cidades [cid_id=" + cid_id + ", cid_nome=" + cid_nome + ", estados=" + estados + "]";
	}

	

	

}
