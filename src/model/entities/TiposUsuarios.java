package model.entities;

import java.io.Serializable;

public class TiposUsuarios  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer tuser_id;
	private String tuser_nome;
	
	
	public TiposUsuarios() {}


	public TiposUsuarios(Integer tuser_id, String tuser_nome) {
		super();
		this.tuser_id = tuser_id;
		this.tuser_nome = tuser_nome;
	}


	public Integer getTuser_id() {
		return tuser_id;
	}


	public void setTuser_id(Integer tuser_id) {
		this.tuser_id = tuser_id;
	}


	public String getTuser_nome() {
		return tuser_nome;
	}


	public void setTuser_nome(String tuser_nome) {
		this.tuser_nome = tuser_nome;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tuser_id == null) ? 0 : tuser_id.hashCode());
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
		TiposUsuarios other = (TiposUsuarios) obj;
		if (tuser_id == null) {
			if (other.tuser_id != null)
				return false;
		} else if (!tuser_id.equals(other.tuser_id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "TiposUsuarios [tuser_id=" + tuser_id + ", tuser_nome=" + tuser_nome + "]";
	}

	
	
	
	
}
