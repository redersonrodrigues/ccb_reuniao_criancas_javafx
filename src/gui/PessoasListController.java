package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Pessoas;
import model.services.CidadesService;
import model.services.EquipesService;
import model.services.GruposService;
import model.services.PessoasService;
import model.services.TiposUsuariosService;

public class PessoasListController implements Initializable, DataChangeListener {

	// declarando dependencia lista de grupos mock
	private PessoasService service;

	@FXML
	private Button btNovo;

	@FXML
	private Button btAlterar;

	@FXML
	private Button btRemover;

	@FXML
	private TableView<Pessoas> tableViewPessoas;

	@FXML
	private TableColumn<Pessoas, String> tableColumnPessoaTelefone;

	@FXML
	private TableColumn<Pessoas, String> tableColumnPessoaNome;

	@FXML
	private TableColumn<Pessoas, Pessoas> tableColumnEDIT;

	@FXML
	private TableColumn<Pessoas, Pessoas> tableColumnREMOVE;

	@FXML
	private Label lblPessoaId;

	@FXML
	private Label lblPessoaNome;

	@FXML
	private Label lblPessoaRg;

	@FXML
	private Label lblPessoaPai;

	@FXML
	private Label lblPessoaMae;

	@FXML
	private Label lblPessoaEndereco;

	@FXML
	private Label lblPessoaBairro;

	@FXML
	private Label lblPessoaTelefone;

	@FXML
	private Label lblPessoaCelular;

	@FXML
	private Label lblPessoaCidade;

	@FXML
	private Label lblPessoaEquipe;

	@FXML
	private Label lblPessoaGrupo;

	@FXML
	private Label lblPessoaTipoUsuario;

	@FXML
	private Label lblPessoaObservacoes;

	List<Pessoas> list;

	private ObservableList<Pessoas> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		// acessa o Stage da tela refer�ncia
		Stage parentStage = Utils.currentStage(event);
		Pessoas obj = new Pessoas();
		// repasso o Stage adquirido com a classe criada para abstrai-lo como o segundo
		// paramentro para abrir a tela.
		createDialogForm(obj, "/gui/PessoasForm.fxml", parentStage);

	}

	@FXML
	public void onBtAlterarAction(ActionEvent event) {

		Pessoas pessoa = tableViewPessoas.getSelectionModel().getSelectedItem();
		if (pessoa != null) {
			// acessa o Stage da tela refer�ncia
			Stage parentStage = Utils.currentStage(event);
			// repasso o Stage adquirido com a classe criada para abstrai-lo como o segundo
			// paramentro para abrir a tela.
			createDialogForm(pessoa, "/gui/PessoasForm.fxml", parentStage);

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Por favor, escolha um cliente na tabela!");
		}

	}

	@FXML
	public void onBtRemoverAction(ActionEvent event) {
		Pessoas pessoa = tableViewPessoas.getSelectionModel().getSelectedItem();
		if (pessoa != null) {
			removeEntity(pessoa);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Por favor, escolha um cliente na tabela!");
		}

	}

	public void setPessoasService(PessoasService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// metodo auxiliar apra abertura das tabelas
		InitializeNodes();

		// Listem acionado diante de quaisquer altera��es na sele��o de itens no
		// TableView
		tableViewPessoas.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewPessoas(newValue));

	}

	private void removeEntity(Pessoas obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirma��o!",
				"Realmente deseja deletar(apagar definitivamente)?");
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null!");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void InitializeNodes() {

		tableColumnPessoaTelefone.setCellValueFactory(new PropertyValueFactory<>("pes_telefone"));
		tableColumnPessoaNome.setCellValueFactory(new PropertyValueFactory<>("pes_nome"));

		// para a tabela acompanhar altura e largura da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPessoas.prefHeightProperty().bind(stage.heightProperty());

	}

	// carrager a obsList para depois associar a lista Pessoas e mostrar na tela
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("o service estava nulo");
		}
		List<Pessoas> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewPessoas.setItems(obsList);

	}

	private void createDialogForm(Pessoas obj, String absoluteName, Stage parenteStage) {
		try {
			// carrega a view atraves da variavel absoluteName
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// passos para carregar dados
			PessoasFormController controller = loader.getController();
			controller.setPessoas(obj);
			controller.setPessoasServices(new PessoasService(), new CidadesService(), new GruposService(),
					new EquipesService(), new TiposUsuariosService());// inje��o de dependencia PessoasServices para
																		// carregamento

			controller.loadAssociatedObjects(); // carrega estados do banco de dados e deixa no controller

			controller.subscribeDataChangeListener(this);// se inscrevendo para observar listeners (onDataChanged)
			controller.updateFormData();

			// passos para abrir um formulario modal a partir de outro de refer�ncia
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados da pessoa: ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parenteStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {

		updateTableView();

	}

	public void selecionarItemTableViewPessoas(Pessoas pessoa) {
		if (pessoa != null) {
			lblPessoaId.setText(String.valueOf(pessoa.getPes_id()));
			lblPessoaNome.setText(pessoa.getPes_nome());
			lblPessoaRg.setText(pessoa.getPes_rg());
			lblPessoaPai.setText(pessoa.getPes_pai());
			lblPessoaMae.setText(pessoa.getPes_mae());
			lblPessoaEndereco.setText(pessoa.getPes_endereco());
			lblPessoaBairro.setText(pessoa.getPes_bairro());
			lblPessoaTelefone.setText(pessoa.getPes_telefone());
			lblPessoaCelular.setText(pessoa.getPes_celular());
			lblPessoaCidade.setText(pessoa.getCidades().getCid_nome());
			lblPessoaEquipe.setText(pessoa.getEquipes().getEqu_nome());
			lblPessoaGrupo.setText(pessoa.getGrupos().getGru_nome());
			lblPessoaTipoUsuario.setText(pessoa.getTiposUsuarios().getTuser_nome());
			lblPessoaObservacoes.setText(pessoa.getPes_observacoes());
		} else {
			lblPessoaId.setText("");
			lblPessoaNome.setText("");
			lblPessoaRg.setText("");
			lblPessoaPai.setText("");
			lblPessoaMae.setText("");
			lblPessoaEndereco.setText("");
			lblPessoaBairro.setText("");
			lblPessoaTelefone.setText("");
			lblPessoaCelular.setText("");
			lblPessoaCidade.setText("");
			lblPessoaEquipe.setText("");
			lblPessoaGrupo.setText("");
			lblPessoaTipoUsuario.setText("");
			lblPessoaObservacoes.setText("");
		}
	}

}
