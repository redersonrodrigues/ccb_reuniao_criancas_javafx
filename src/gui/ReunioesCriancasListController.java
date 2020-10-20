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
import model.entities.ReunioesCriancas;
import model.services.ReunioesCriancasService;
import model.services.EstadosService;

public class ReunioesCriancasListController implements Initializable, DataChangeListener {
	
	//declarando dependencia lista de grupos mock
	private ReunioesCriancasService service;
	
	

	@FXML
	private TableView<ReunioesCriancas> tableViewReunioesCriancas;
	
	@FXML
	private TableColumn<ReunioesCriancas, Integer> tableColumnId;
	
	@FXML
	private TableColumn<ReunioesCriancas, String> tableColumnNome;
	
	@FXML
	private TableColumn<ReunioesCriancas, ReunioesCriancas> tableColumnEDIT;
	
	@FXML
	private TableColumn<ReunioesCriancas, ReunioesCriancas> tableColumnREMOVE;
	
	@FXML
	private Button btNovo;
	
	
	private ObservableList<ReunioesCriancas> obsList;
	
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		//acessa o Stage da tela refer�ncia
		Stage parentStage = Utils.currentStage(event);
		ReunioesCriancas obj = new ReunioesCriancas();
		//repasso o Stage adquirido com a classe criada para abstrai-lo como o segundo paramentro para abrir a tela.
		createDialogForm(obj, "/gui/ReunioesCriancasForm.fxml", parentStage);
		
	}
	
	
	public void setReunioesCriancasService(ReunioesCriancasService service) {
		this.service = service;
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<ReunioesCriancas, ReunioesCriancas>(){
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(ReunioesCriancas obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ReunioesCriancasForm.fxml", Utils.currentStage(event)
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
		tableColumnREMOVE.setCellFactory(param -> new TableCell<ReunioesCriancas, ReunioesCriancas>(){
			private final Button button = new Button("deletar");
			
			@Override
			protected void updateItem(ReunioesCriancas obj, boolean empty) {
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

	private void removeEntity(ReunioesCriancas obj) {
		
	Optional<ButtonType> result =	Alerts.showConfirmation("Confirma��o!",	"Realmente deseja deletar(apagar definitivamente)?");
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

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("reu_id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("reu_atendimento"));

		
		//para a tabela acompanhar altura e largura da tela
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
		initEditButtons(); // acrescentar� um novo bot�o com o texto edit em cada linha da tabela
		initRemoveButtons();// acrescentando um novo botao em cada linha, como o edit, para remo��o de itens
	}
	
	private void createDialogForm(ReunioesCriancas obj, String absoluteName, Stage parenteStage) {
		try {
			//carrega a view atraves da variavel absoluteName
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			// passos para carregar dados
			ReunioesCriancasFormController controller = loader.getController();
			controller.setReunioesCriancas(obj);
			controller.setReunioesCriancasServices(new ReunioesCriancasService(), new EstadosService());//inje��o de dependencia ReunioesCriancasServices para carregamento
			
			controller.loadAssociatedObjects(); // carrega estados do banco de dados e deixa no controller
			
 			controller.subscribeDataChangeListener(this);// se inscrevendo para observar listeners (onDataChanged)
			controller.updateFormData();
			
			
			// passos para abrir um formulario modal a partir de outro de refer�ncia
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do grupo: ");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parenteStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
			
		} 
		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}


	@Override
	public void onDataChanged() {

		updateTableView();
		
	}
	
	
	
	

}
