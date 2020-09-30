package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Grupos;
import model.services.GruposService;

public class GruposFormController implements Initializable{
	
	// injeção de dependencia para a entidade relacionada a este formulário
	private Grupos entity;
	
	// injeção dependência GruposService
	private GruposService service;

	
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

	
	
	public void setGruposService(GruposService service) {
		this.service = service;
	}



	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null ) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null ) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
		//System.out.println("onBtSalvarAction");
		entity = getFormData();
		service.saveOrUpdate(entity);
		Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	
	// metodo para capturar o que esta nos campos do formulario e instanciar um grupo
	private Grupos getFormData() {
		
		Grupos obj = new Grupos();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		
		return obj;
	}



	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		
		//System.out.println("onBtCancelarAction");
		Utils.currentStage(event).close();
		
	}
	
		
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	//função para tratamento de restrições (constraints)
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
