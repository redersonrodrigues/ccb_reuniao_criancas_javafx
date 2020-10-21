package gui;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.entities.Estados;
import model.entities.ReunioesCriancas;
import model.exceptions.ValidationException;
import model.services.CidadesService;
import model.services.EquipesService;
import model.services.GruposService;
import model.services.ReunioesCriancasService;
import model.services.TiposUsuariosService;

public class ReunioesCriancasFormController implements Initializable {

	// injeção de dependencia para a entidade relacionada a este formulário
	private ReunioesCriancas entity;

	// injeção dependência ReunioesCriancasService
	private ReunioesCriancasService service;

	// injeção de dependencia CidadesService, GruposService, EquipesService,
	// TiposUsuariosService
	private CidadesService cidadesService;
	private GruposService gruposService;
	private EquipesService equipesService;
	private TiposUsuariosService tiposUsuariosService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtData;

	@FXML
	private TextField txtAtendimento;
	
	@FXML
	private TextField txtTema;

	@FXML
	private TextField txtEquipeResponsavel;

	@FXML
	private TextField txtObservacoes;

	@FXML
	private TextArea txtPessoas;


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

	BufferedImage imagem;

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
	public void onBtSalvarAction(ActionEvent event) throws ParseException {
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
	private ReunioesCriancas getFormData() throws ParseException {

		ReunioesCriancas obj = new ReunioesCriancas();

		ValidationException exception = new ValidationException("Validation error");

		obj.setReu_id(Utils.tryParseToInt(txtId.getText()));

		if (txtData.getText() == null || txtData.getText().trim().equals("")) {

			exception.addError("nome", "O campo não pode ser vazio!");

		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		obj.setReu_data(sdf.parse(txtData.getText()));
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
		Constraints.setTextFieldMaxLength(txtAtendimento, 255);
		Constraints.setTextFieldMaxLength(txtTema, 255);
		Constraints.setTextFieldMaxLength(txtEquipeResponsavel, 255);
		Constraints.setTextFieldMaxLength(txtObservacoes, 255);
		
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getReu_id()));
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		txtData.setText(sdf.format(entity.getReu_data()));
		txtAtendimento.setText(entity.getReu_atendimento());
		txtTema.setText(entity.getReu_tema());
		txtEquipeResponsavel.setText(entity.getReu_equipe_respons());
		txtObservacoes.setText(entity.getReu_observacoes());
		txtPessoas.setText(entity.getPessoas().getPes_nome());
		
		
	}


	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}

	}

	public void loadAssociatedObjects() {
		
	}

}
