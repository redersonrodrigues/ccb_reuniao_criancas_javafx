package model.entities;

import java.io.Serializable;

public class Estados  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer est_id;
	private String est_nome;
	private String est_sigla;
	
	
	public Estados() {}


	public Estados(Integer est_id, String est_nome, String est_sigla) {
		this.est_id = est_id;
		this.est_nome = est_nome;
		this.est_sigla = est_sigla;
	}


	public Integer getEst_id() {
		return est_id;
	}


	public void setEst_id(Integer est_id) {
		this.est_id = est_id;
	}


	public String getEst_nome() {
		return est_nome;
	}


	public void setEst_nome(String est_nome) {
		this.est_nome = est_nome;
	}


	public String getEst_sigla() {
		return est_sigla;
	}


	public void setEst_sigla(String est_sigla) {
		this.est_sigla = est_sigla;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((est_id == null) ? 0 : est_id.hashCode());
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
		Estados other = (Estados) obj;
		if (est_id == null) {
			if (other.est_id != null)
				return false;
		} else if (!est_id.equals(other.est_id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Estados [est_id=" + est_id + ", est_nome=" + est_nome + ", est_sigla=" + est_sigla + "]";
	}
	
	
	

}
