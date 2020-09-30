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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Estados;
import model.services.EstadosService;

public class EstadosListController implements Initializable, DataChangeListener {
	
	//declarando dependencia lista de grupos mock
	private EstadosService service;
	
	

	@FXML
	private TableView<Estados> tableViewEstados;
	
	@FXML
	private TableColumn<Estados, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Estados, String> tableColumnNome;
	
	@FXML
	private TableColumn<Estados, String> tableColumnSigla;
	
	
	@FXML
	private TableColumn<Estados, Estados> tableColumnEDIT;
	
	@FXML
	private TableColumn<Estados, Estados> tableColumnREMOVE;
	
	@FXML
	private Button btNovo;
	
	
	private ObservableList<Estados> obsList;
	
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		//acessa o Stage da tela referência
		Stage parentStage = Utils.currentStage(event);
		Estados obj = new Estados();
		//repasso o Stage adquirido com a classe criada para abstrai-lo como o segundo paramentro para abrir a tela.
		createDialogForm(obj, "/gui/EstadosForm.fxml", parentStage);
		
	}
	
	
	public void setEstadosService(EstadosService service) {
		this.service = service;
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Estados, Estados>(){
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Estados obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/EstadosForm.fxml", Utils.currentStage(event)
						));
			}
			
		});
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//metodo auxiliar apra abertura das tabelas
		InitializeNodes();
		
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Estados, Estados>(){
			private final Button button = new Button("deletar");
			
			@Override
			protected void updateItem(Estados obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(
						event -> removeEntity(obj)
						);
			}
			
		});
	}

	private void removeEntity(Estados obj) {
		
	Optional<ButtonType> result =	Alerts.showConfirmation("Confirmação!",	"Realmente deseja deletar(apagar definitivamente)?");
	if (result.get() == ButtonType.OK) {
		if (service == null) {
			throw new IllegalStateException("Service was null!");
		}
		try {
		service.remove(obj);
		updateTableView();
		}
		catch (DbIntegrityException e) {
			Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
		}
	}	
	}


	private void InitializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("est_id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("est_nome"));
		tableColumnSigla.setCellValueFactory(new PropertyValueFactory<>("est_sigla"));
		
		//para a tabela acompanhar altura e largura da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEstados.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	// carrager a obsList para depois associar a lista Estados e mostrar na tela
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("o service estava nulo");	
		}
		List<Estados> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewEstados.setItems(obsList);
		initEditButtons(); // acrescentará um novo botão com o texto edit em cada linha da tabela
		initRemoveButtons();// acrescentando um novo botao em cada linha, como o edit, para remoção de itens
	}
	
	private void createDialogForm(Estados obj, String absoluteName, Stage parenteStage) {
		try {
			//carrega a view atraves da variavel absoluteName
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			// passos para carregar dados
			EstadosFormController controller = loader.getController();
			controller.setEstados(obj);
			controller.setEstadosService(new EstadosService());//injeção de dependencia EstadosServices para carregamento
			controller.subscribeDataChangeListener(this);// se inscrevendo para observar listeners (onDataChanged)
			controller.updateFormData();
			
			
			// passos para abrir um formulario modal a partir de outro de referência
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do grupo: ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parenteStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
			
		} 
		catch (IOException e) {
				Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}


	@Override
	public void onDataChanged() {

		updateTableView();
		
	}
	
	
	
	

}
