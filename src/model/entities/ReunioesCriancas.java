package model.entities;

import java.io.Serializable;
import java.util.Date;

public class ReunioesCriancas  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer reu_id;
	private Date reu_data;
	private String reu_atendimento;
	private String reu_tema;
	private String reu_equipe_respons;
	private String reu_observacoes;
	
	
	
	private Pessoas pessoas;

	
	public ReunioesCriancas() {}


	public ReunioesCriancas(Integer reu_id, Date reu_data, String reu_atendimento, String reu_tema,
			String reu_equipe_respons, String reu_observacoes, Pessoas pessoas) {
		this.reu_id = reu_id;
		this.reu_data = reu_data;
		this.reu_atendimento = reu_atendimento;
		this.reu_tema = reu_tema;
		this.reu_equipe_respons = reu_equipe_respons;
		this.reu_observacoes = reu_observacoes;
		this.pessoas = pessoas;
	}


	public Integer getReu_id() {
		return reu_id;
	}


	public void setReu_id(Integer reu_id) {
		this.reu_id = reu_id;
	}


	public Date getReu_data() {
		return reu_data;
	}


	public void setReu_data(Date reu_data) {
		this.reu_data = reu_data;
	}


	public String getReu_atendimento() {
		return reu_atendimento;
	}


	public void setReu_atendimento(String reu_atendimento) {
		this.reu_atendimento = reu_atendimento;
	}


	public String getReu_tema() {
		return reu_tema;
	}


	public void setReu_tema(String reu_tema) {
		this.reu_tema = reu_tema;
	}


	public String getReu_equipe_respons() {
		return reu_equipe_respons;
	}


	public void setReu_equipe_respons(String reu_equipe_respons) {
		this.reu_equipe_respons = reu_equipe_respons;
	}


	public String getReu_observacoes() {
		return reu_observacoes;
	}


	public void setReu_observacoes(String reu_observacoes) {
		this.reu_observacoes = reu_observacoes;
	}


	public Pessoas getPessoas() {
		return pessoas;
	}


	public void setPessoas(Pessoas pessoas) {
		this.pessoas = pessoas;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reu_id == null) ? 0 : reu_id.hashCode());
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
		ReunioesCriancas other = (ReunioesCriancas) obj;
		if (reu_id == null) {
			if (other.reu_id != null)
				return false;
		} else if (!reu_id.equals(other.reu_id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ReunioesCriancas [reu_id=" + reu_id + ", reu_data=" + reu_data + ", reu_atendimento=" + reu_atendimento
				+ ", reu_tema=" + reu_tema + ", reu_equipe_respons=" + reu_equipe_respons + ", reu_observacoes="
				+ reu_observacoes + ", pessoas=" + pessoas + "]";
	}
	
	
	
	

	
	
}
