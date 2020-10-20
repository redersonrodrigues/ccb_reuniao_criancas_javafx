package model.entities;

import java.io.Serializable;

public class Grupos  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer gru_id;
	private String gru_nome;
	
	
	public Grupos() {}


	public Grupos(Integer gru_id, String gru_nome) {
		super();
		this.gru_id = gru_id;
		this.gru_nome = gru_nome;
	}


	public Integer getGru_id() {
		return gru_id;
	}


	public void setGru_id(Integer gru_id) {
		this.gru_id = gru_id;
	}


	public String getGru_nome() {
		return gru_nome;
	}


	public void setGru_nome(String gru_nome) {
		this.gru_nome = gru_nome;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gru_id == null) ? 0 : gru_id.hashCode());
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
		Grupos other = (Grupos) obj;
		if (gru_id == null) {
			if (other.gru_id != null)
				return false;
		} else if (!gru_id.equals(other.gru_id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Grupos [gru_id=" + gru_id + ", gru_nome=" + gru_nome + "]";
	}


}
