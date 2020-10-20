package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JOptionPane;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.ManipularImagem;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Cidades;
import model.entities.Equipes;
import model.entities.Grupos;
import model.entities.Pessoas;
import model.entities.TiposUsuarios;
import model.exceptions.ValidationException;
import model.services.CidadesService;
import model.services.EquipesService;
import model.services.GruposService;
import model.services.PessoasService;
import model.services.TiposUsuariosService;

public class PessoasFormController implements Initializable {

	// injeção de dependencia para a entidade relacionada a este formulário
	private Pessoas entity;

	// injeção dependência PessoasService
	private PessoasService service;

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
	private TextField txtNome;

	@FXML
	private TextField txtRg;

	@FXML
	private TextField txtEndereco;

	@FXML
	private TextField txtBairro;

	@FXML
	private TextField txtTelefone;

	@FXML
	private TextField txtCelular;

	@FXML
	private ComboBox<Cidades> comboBoxCidades;

	@FXML
	private ComboBox<Grupos> comboBoxGrupos;

	@FXML
	private ComboBox<TiposUsuarios> comboBoxTiposUsuarios;

	@FXML
	private ComboBox<Equipes> comboBoxEquipes;

	@FXML
	private ImageView lblImagem;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorSigla;

	@FXML
	private Button btFoto;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	private ObservableList<Cidades> obsList;
	private ObservableList<Grupos> obsList2;
	private ObservableList<Equipes> obsList3;
	private ObservableList<TiposUsuarios> obsList4;

	BufferedImage imagem;

	public void setPessoas(Pessoas entity) {
		this.entity = entity;
	}

	public void setPessoasServices(PessoasService service, CidadesService cidadeService, GruposService gruposService,
			EquipesService equipesService, TiposUsuariosService tiposUsuariosService) {
		this.service = service;
		this.cidadesService = cidadeService;
		this.gruposService = gruposService;
		this.equipesService = equipesService;
		this.tiposUsuariosService = tiposUsuariosService;
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
	private Pessoas getFormData() {

		Pessoas obj = new Pessoas();

		ValidationException exception = new ValidationException("Validation error");

		obj.setPes_id(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {

			exception.addError("nome", "O campo não pode ser vazio!");

		}

		obj.setPes_nome(txtNome.getText());
		// obj.setPes_foto(imageViewFoto.getImage();
		obj.setPes_rg(txtRg.getText());
		obj.setPes_endereco(txtEndereco.getText());
		obj.setPes_bairro(txtBairro.getText());
		obj.setPes_telefone(txtTelefone.getText());
		obj.setPes_celular(txtCelular.getText());

		obj.setCidades(comboBoxCidades.getValue());
		obj.setGrupos(comboBoxGrupos.getValue());
		obj.setEquipes(comboBoxEquipes.getValue());
		obj.setTiposUsuarios(comboBoxTiposUsuarios.getValue());

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

	@FXML
	public void onBtFotoAction(ActionEvent event) {
		Stage primaryStage = null;
		primaryStage.setTitle("JavaFX App");

		FileChooser fc = new FileChooser();

		Button button = new Button("Select File");
		button.setOnAction(e -> {
			File file = fc.showOpenDialog(primaryStage);

			if (file != null) {

				imagem = ManipularImagem.setImagemDimensao(file.getAbsolutePath(), 160, 160);
				FileInputStream input;
				try {
					input = new FileInputStream(imagem.toString());
					Image img = new Image(input);
					ImageView imageView = new ImageView(img);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// lblImagem.setIcon(new ImageIcon(imagem));

				// System.out.println(ex.printStackTrace().toString());

			} else {
				JOptionPane.showMessageDialog(null, "Voce nao selecionou nenhum arquivo.");
			}

		});

		VBox vBox = new VBox(button);
		Scene scene = new Scene(vBox, 960, 600);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	// função para tratamento de restrições (constraints)
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldMaxLength(txtRg, 20);
		Constraints.setTextFieldMaxLength(txtEndereco, 255);
		Constraints.setTextFieldMaxLength(txtBairro, 255);
		Constraints.setTextFieldMaxLength(txtTelefone, 20);
		Constraints.setTextFieldMaxLength(txtCelular, 20);

		initializeComboBoxCidades();
		initializeComboBoxGrupos();
		initializeComboBoxTiposUsuarios();
		initializeComboBoxEquipes();
	}

	private void initializeComboBoxCidades() {
		Callback<ListView<Cidades>, ListCell<Cidades>> factory = lv -> new ListCell<Cidades>() {
			@Override
			protected void updateItem(Cidades item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getCid_nome());
			}

		};

		comboBoxCidades.setCellFactory(factory);
		comboBoxCidades.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxEquipes() {
		Callback<ListView<Equipes>, ListCell<Equipes>> factory = lv -> new ListCell<Equipes>() {
			@Override
			protected void updateItem(Equipes item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getEqu_nome());
			}

		};

		comboBoxEquipes.setCellFactory(factory);
		comboBoxEquipes.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxTiposUsuarios() {
		Callback<ListView<TiposUsuarios>, ListCell<TiposUsuarios>> factory = lv -> new ListCell<TiposUsuarios>() {
			@Override
			protected void updateItem(TiposUsuarios item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getTuser_nome());
			}

		};

		comboBoxTiposUsuarios.setCellFactory(factory);
		comboBoxTiposUsuarios.setButtonCell(factory.call(null));
	}

	private void initializeComboBoxGrupos() {
		Callback<ListView<Grupos>, ListCell<Grupos>> factory = lv -> new ListCell<Grupos>() {
			@Override
			protected void updateItem(Grupos item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getGru_nome());
			}

		};

		comboBoxGrupos.setCellFactory(factory);
		comboBoxGrupos.setButtonCell(factory.call(null));
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getPes_id()));
		txtNome.setText(entity.getPes_nome());
		lblImagem.setImage(null);

		txtRg.setText(entity.getPes_rg());
		txtEndereco.setText(entity.getPes_endereco());
		txtBairro.setText(entity.getPes_bairro());
		txtTelefone.setText(entity.getPes_telefone());
		txtCelular.setText(entity.getPes_celular());

		if (entity.getCidades() == null) {
			comboBoxCidades.getSelectionModel().selectFirst();
		} else {
			comboBoxCidades.setValue(entity.getCidades());
		}

		if (entity.getGrupos() == null) {
			comboBoxGrupos.getSelectionModel().selectFirst();
		} else {
			comboBoxGrupos.setValue(entity.getGrupos());
		}

		if (entity.getEquipes() == null) {
			comboBoxEquipes.getSelectionModel().selectFirst();
		} else {
			comboBoxEquipes.setValue(entity.getEquipes());
		}

		if (entity.getTiposUsuarios() == null) {
			comboBoxTiposUsuarios.getSelectionModel().selectFirst();
		} else {
			comboBoxTiposUsuarios.setValue(entity.getTiposUsuarios());
		}

	}

	public void loadAssociatedObjects() {
		if (cidadesService == null) {
			throw new IllegalStateException("CidadesService was null");
		}

		List<Cidades> list = cidadesService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxCidades.setItems(obsList);

		if (gruposService == null) {
			throw new IllegalStateException("GruposService was null");
		}

		List<Grupos> list2 = gruposService.findAll();
		obsList2 = FXCollections.observableArrayList(list2);
		comboBoxGrupos.setItems(obsList2);

		if (equipesService == null) {
			throw new IllegalStateException("EquipesService was null");
		}

		List<Equipes> list3 = equipesService.findAll();
		obsList3 = FXCollections.observableArrayList(list3);
		comboBoxEquipes.setItems(obsList3);

		if (tiposUsuariosService == null) {
			throw new IllegalStateException("TiposUsuariosService was null");
		}

		List<TiposUsuarios> list4 = tiposUsuariosService.findAll();
		obsList4 = FXCollections.observableArrayList(list4);
		comboBoxTiposUsuarios.setItems(obsList4);

	}

	private void setErrorMessages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}

	}

}
