package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Grupos;

public class GruposFormController implements Initializable{
	
	// inje��o de dependencia para a entidade relacionada a este formul�rio
	private Grupos entity;

	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	
	public void setGrupos(Grupos entity) {
		this.entity = entity;
	}

	
	
	@FXML
	public void onBtSalvarAction() {
		
		System.out.println("onBtSalvarAction");
		
	}
	
	@FXML
	public void onBtCancelarAction() {
		
		System.out.println("onBtCancelarAction");
		
	}
	
		
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	//fun��o para tratamento de restri��es (constraints)
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
				
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		
		
	}

}
