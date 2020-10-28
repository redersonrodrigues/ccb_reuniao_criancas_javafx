package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.text.DateFormatter;

import com.mysql.jdbc.Util;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.ReunioesCriancas;
import model.services.PessoasService;
import model.services.ReunioesCriancasService;

public class ReunioesCriancasListController implements Initializable, DataChangeListener {

	// declarando dependencia lista de grupos mock
	private ReunioesCriancasService service;

	@FXML
	private Button btNovo;

	@FXML
	private Button btAlterar;

	@FXML
	private Button btRemover;

	@FXML
	private TableView<ReunioesCriancas> tableViewReunioesCriancas;

	@FXML
	private TableColumn<ReunioesCriancas, Integer> tableColumnReuniaoId;

	@FXML
	private TableColumn<ReunioesCriancas, Date> tableColumnReuniaoData;
	
	@FXML
	private TableColumn<ReunioesCriancas, ReunioesCriancas> tableColumnReuniaoParticipante;

	@FXML
	private Label lblReuniaoId;

	@FXML
	private Label lblReuniaoData;

	@FXML
	private Label lblReuniaoHorario;

	@FXML
	private Label lblReuniaoAtendimento;

	@FXML
	private Label lblReuniaoTema;

	@FXML
	private Label lblReuniaoEquipeResponsavel;

	@FXML
	private TextArea txtAreaReuniaoObservacoes;

	List<ReunioesCriancas> listaReuniao;

	private ObservableList<ReunioesCriancas> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		// acessa o Stage da tela referência
		Stage parentStage = Utils.currentStage(event);
		ReunioesCriancas obj = new ReunioesCriancas();
		// repasso o Stage adquirido com a classe criada para abstrai-lo como o segundo
		// paramentro para abrir a tela.
		createDialogForm(obj, "/gui/ReunioesCriancasForm.fxml", parentStage);

	}

	@FXML
	public void onBtAlterarAction(ActionEvent event) {

		ReunioesCriancas reuniao = tableViewReunioesCriancas.getSelectionModel().getSelectedItem();
		
		if (reuniao != null) {
			// acessa o Stage da tela referência
			Stage parentStage = Utils.currentStage(event);
			// repasso o Stage adquirido com a classe criada para abstrai-lo como o segundo
			// paramentro para abrir a tela.
			createDialogForm(reuniao, "/gui/ReunioesCriancasForm.fxml", parentStage);

		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Por favor, escolha um cliente na tabela!");
		}

	}

	@FXML
	public void onBtRemoverAction(ActionEvent event) {
		ReunioesCriancas reuniao = tableViewReunioesCriancas.getSelectionModel().getSelectedItem();
		if (reuniao != null) {
			removeEntity(reuniao);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Por favor, escolha um cliente na tabela!");
		}

	}

	public void setReunioesCriancasService(ReunioesCriancasService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// metodo auxiliar apra abertura das tabelas
		InitializeNodes();

		// Listem acionado diante de quaisquer alterações na seleção de itens no
		// TableView
		tableViewReunioesCriancas.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> selecionarItemTableViewReunioesCriancas(newValue));

	}

	private void removeEntity(ReunioesCriancas obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação!",
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

		
		tableColumnReuniaoId.setCellValueFactory(new PropertyValueFactory<>("reu_id"));
		tableColumnReuniaoData.setCellValueFactory(new PropertyValueFactory<>("reu_data"));
		tableColumnReuniaoParticipante.setCellValueFactory(new PropertyValueFactory<>("pessoa"));
		
		// para a tabela acompanhar altura e largura da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewReunioesCriancas.prefHeightProperty().bind(stage.heightProperty());

	}

	// carrager a obsList para depois associar a lista ReunioesCriancas e mostrar na tela
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("o service estava nulo");
		}
		List<ReunioesCriancas> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewReunioesCriancas.setItems(obsList);

	}

	private void createDialogForm(ReunioesCriancas obj, String absoluteName, Stage parenteStage) {
		try {
			// carrega a view atraves da variavel absoluteName
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			// passos para carregar dados
			ReunioesCriancasFormController controller = loader.getController();
			controller.setReunioesCriancas(obj);
			controller.setServices(new ReunioesCriancasService(), new PessoasService());// injeção de dependencia ReunioesCriancasServices para carregamento

			controller.subscribeDataChangeListener(this);// se inscrevendo para observar listeners (onDataChanged)
			controller.updateFormData();

			controller.loadAssociatedObjects(); // carrega Pessoas do banco de dados e deixa no controller
			
			// passos para abrir um formulario modal a partir de outro de referência
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados da reuniao: ");
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

	public void selecionarItemTableViewReunioesCriancas(ReunioesCriancas reuniao) {
		if (reuniao != null) {
			lblReuniaoId.setText(String.valueOf(reuniao.getReu_id()));
			
			lblReuniaoData.setText(new SimpleDateFormat("dd/MM/yyyy").format(reuniao.getReu_data()));
			
			lblReuniaoHorario.setText(reuniao.getReu_horario());
			lblReuniaoAtendimento.setText(reuniao.getReu_atendimento());
			lblReuniaoTema.setText(reuniao.getReu_tema());
			lblReuniaoEquipeResponsavel.setText(reuniao.getReu_equipe_respons());
			txtAreaReuniaoObservacoes.setText(reuniao.getReu_observacoes());
			
		} else {
			lblReuniaoId.setText("");
			lblReuniaoData.setText("");
			lblReuniaoHorario.setText("");
			lblReuniaoAtendimento.setText("");
			lblReuniaoTema.setText("");
			lblReuniaoEquipeResponsavel.setText("");
			txtAreaReuniaoObservacoes.setText("");
				}
	}

}
