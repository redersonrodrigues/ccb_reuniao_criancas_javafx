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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Cidades;
import model.entities.Estados;
import model.exceptions.ValidationException;
import model.services.CidadesService;
import model.services.EstadosService;

public class CidadesFormController implements Initializable{
	
	// inje��o de dependencia para a entidade relacionada a este formul�rio
	private Cidades entity;
	
	// inje��o depend�ncia CidadesService
	private CidadesService service;
	
	// inje��o de dependencia EstadosService
	private EstadosService estadosService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	

	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private ComboBox<Estados> comboBoxEstados;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Label labelErrorSigla;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	
	private ObservableList<Estados> obsList;
	
	
	
	
	public void setCidades(Cidades entity) {
		this.entity = entity;
	}

	
	
	public void setCidadesServices(CidadesService service, EstadosService estadoService) {
		this.service = service;
		this.estadosService = estadoService;
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
	private Cidades getFormData() {
		
		Cidades obj = new Cidades();
		
		ValidationException exception = new ValidationException("Validation error");

		obj.setCid_id(Utils.tryParseToInt(txtId.getText()));
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
		
			exception.addError("nome", "O campo n�o pode ser vazio!");
		
		}
		
		obj.setCid_nome(txtNome.getText());
		
		obj.setEstados(comboBoxEstados.getValue());
		
		
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
		
		initializeComboBoxEstados();
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
				
		txtId.setText(String.valueOf(entity.getCid_id()));
		txtNome.setText(entity.getCid_nome());
		
		if ( entity.getEstados() == null) {
			comboBoxEstados.getSelectionModel().selectFirst();
		}
		else {
		comboBoxEstados.setValue(entity.getEstados());
		}
		
		
	}
	
	public void loadAssociatedObjects() {
		if (estadosService == null) {
			throw new IllegalStateException("EstadosService was null");
		}
		
		List<Estados> list = estadosService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxEstados.setItems(obsList);
	}
	
	
	private void setErrorMessages(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));	
		}
		
	}
	
	private void initializeComboBoxEstados() {
		Callback<ListView<Estados>, ListCell<Estados>> factory = lv ->  new ListCell<Estados>() {
			@Override
			protected void updateItem(Estados item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getEst_nome());
			}
			
		};
		
		comboBoxEstados.setCellFactory(factory);
		comboBoxEstados.setButtonCell(factory.call(null));
	}
	

}
