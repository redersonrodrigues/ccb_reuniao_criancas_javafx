package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Estados;
import model.exceptions.ValidationException;
import model.services.EstadosService;

public class EstadosFormController implements Initializable{
	
	// inje��o de dependencia para a entidade relacionada a este formul�rio
	private Estados entity;
	
	// inje��o depend�ncia EstadosService
	private EstadosService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	

	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtSigla;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Label labelErrorSigla;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	
	public void setEstados(Estados entity) {
		this.entity = entity;
	}

	
	
	public void setEstadosService(EstadosService service) {
		this.service = service;
	}



	//metodo para se inscrever na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
		notifyDataChangeListeners();
		Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	// mandar envento para todos os listeners
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}



	// metodo para capturar o que esta nos campos do formulario e instanciar um grupo
	private Estados getFormData() {
		
		Estados obj = new Estados();
		
		ValidationException exception = new ValidationException("Validation error");

		obj.setEst_id(Utils.tryParseToInt(txtId.getText()));
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
		
			exception.addError("nome", "O campo n�o pode ser vazio!");
		
		}
		
		obj.setEst_nome(txtNome.getText());
		obj.setEst_sigla(txtSigla.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
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
	
	//fun��o para tratamento de restri��es (constraints)
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldMaxLength(txtSigla, 2);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
				
		txtId.setText(String.valueOf(entity.getEst_id()));
		txtNome.setText(entity.getEst_nome());
		txtSigla.setText(entity.getEst_sigla());
		
		
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if (fields.contains("nome")) {
			
			labelErrorNome.setText(errors.get("nome"));
			
			
		}
		
	}
	

}
