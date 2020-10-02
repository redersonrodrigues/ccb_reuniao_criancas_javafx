package model.entities;

import java.io.Serializable;

public class Equipes  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer equ_id;
	private String equ_nome;
	
	
	public Equipes() {}


	public Equipes(Integer equ_id, String equ_nome) {
		this.equ_id = equ_id;
		this.equ_nome = equ_nome;
	}


	public Integer getEqu_id() {
		return equ_id;
	}


	public void setEqu_id(Integer equ_id) {
		this.equ_id = equ_id;
	}


	public String getEqu_nome() {
		return equ_nome;
	}


	public void setEqu_nome(String equ_nome) {
		this.equ_nome = equ_nome;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((equ_id == null) ? 0 : equ_id.hashCode());
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
		Equipes other = (Equipes) obj;
		if (equ_id == null) {
			if (other.equ_id != null)
				return false;
		} else if (!equ_id.equals(other.equ_id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Equipes [equ_id=" + equ_id + ", equ_nome=" + equ_nome + "]";
	}
	
}
