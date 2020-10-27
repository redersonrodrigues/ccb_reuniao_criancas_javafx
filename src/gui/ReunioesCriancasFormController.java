package gui;

import java.net.URL;
import java.sql.Date;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import model.entities.Pessoas;
import model.entities.ReunioesCriancas;
import model.exceptions.ValidationException;
import model.services.ReunioesCriancasService;

public class ReunioesCriancasFormController implements Initializable {

	// injeção de dependencia para a entidade relacionada a este formulário
	private ReunioesCriancas entity;

	// injeção dependência ReunioesCriancasService
	private ReunioesCriancasService service;

	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtData;

	@FXML
	private TextField txtHorario;
	
	@FXML
	private TextField txtAtendimento;

	@FXML
	private TextField txtTema;

	@FXML
	private TextField txtEquipeResponsavel;

	@FXML
	private TextArea txtObservacoes;

	@FXML
	private ComboBox<Pessoas> comboBoxPessoas;
	

	@FXML
	private ImageView lblImagem;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorSigla;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;
	
	@FXML
	private Button btAdicionar;

	
	public void setReunioesCriancas(ReunioesCriancas entity) {
		this.entity = entity;
	}

	public void setReunioesCriancasServices(ReunioesCriancasService service) {
		this.service = service;
		
	}

	// metodo para se inscrever na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			// System.out.println("onBtSalvarAction");
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	// mandar envento para todos os listeners
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	// metodo para capturar o que esta nos campos do formulario e instanciar um
	// grupo
	private ReunioesCriancas getFormData() {

		ReunioesCriancas obj = new ReunioesCriancas();

		ValidationException exception = new ValidationException("Validation error");

		obj.setReu_id(Utils.tryParseToInt(txtId.getText()));

		if (txtData.getText() == null || txtData.getText().trim().equals("")) {

			exception.addError("nome", "O campo não pode ser vazio!");

		}

		obj.setReu_data(Date.valueOf(txtData.getText()).toLocalDate());
		obj.setReu_horario(txtHorario.getText());
		obj.setReu_atendimento(txtAtendimento.getText());
		obj.setReu_tema(txtTema.getText());
		obj.setReu_equipe_respons(txtEquipeResponsavel.getText());
		obj.setReu_observacoes(txtObservacoes.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;

	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {

		// System.out.println("onBtCancelarAction");
		Utils.currentStage(event).close();

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	// função para tratamento de restrições (constraints)
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtData, 20);
		Constraints.setTextFieldMaxLength(txtHorario, 20);
		Constraints.setTextFieldMaxLength(txtAtendimento, 255);
		Constraints.setTextFieldMaxLength(txtTema, 255);
		Constraints.setTextFieldMaxLength(txtEquipeResponsavel, 255);
		initializeComboBoxPessoas();
		}

	private void initializeComboBoxPessoas() {
		Callback<ListView<Pessoas>, ListCell<Pessoas>> factory = lv -> new ListCell<Pessoas>() {
			@Override
			protected void updateItem(Pessoas item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getPes_nome());
			}

		};

		comboBoxPessoas.setCellFactory(factory);
		comboBoxPessoas.setButtonCell(factory.call(null));
	}

	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getReu_id()));
		txtData.setText(String.valueOf(entity.getReu_data()).toString());
		txtHorario.setText(entity.getReu_horario());
		txtAtendimento.setText(entity.getReu_atendimento());
		txtTema.setText(entity.getReu_tema());
		txtEquipeResponsavel.setText(entity.getReu_equipe_respons());
		txtObservacoes.setText(entity.getReu_observacoes());
		
		
	}

	public void loadAssociatedObjects() {
		/*if (pessoasService == null) {
			throw new IllegalStateException("PessoasService was null");
		}

		List<Pessoas> listP = pessoasService.findAll();
		obsListP = FXCollections.observableArrayList(listP);
		comboBoxPessoas.setItems(obsListP);
*/
		

	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}

	}

}
