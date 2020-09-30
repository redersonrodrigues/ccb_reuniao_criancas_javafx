package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Grupos;
import model.services.GruposService;

public class GruposListController implements Initializable {
	
	//declarando dependencia lista de grupos mock
	private GruposService service;
	
	

	@FXML
	private TableView<Grupos> tableViewGrupos;
	
	@FXML
	private TableColumn<Grupos, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Grupos, String> tableColumnNome;
	
	@FXML
	private Button btNovo;
	
	
	private ObservableList<Grupos> obsList;
	
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		//acessa o Stage da tela referência
		Stage parentStage = Utils.currentStage(event);
		Grupos obj = new Grupos();
		//repasso o Stage adquirido com a classe criada para abstrai-lo como o segundo paramentro para abrir a tela.
		createDialogForm(obj, "/gui/GruposForm.fxml", parentStage);
		
	}
	
	
	public void setGruposService(GruposService service) {
		this.service = service;
	}


	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//metodo auxiliar apra abertura das tabelas
		InitializeNodes();
		
	}



	private void InitializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		//para a tabela acompanhar altura e largura da tela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewGrupos.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	// carrager a obsList para depois associar a lista Grupos e mostrar na tela
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("o service estava nulo");	
		}
		List<Grupos> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewGrupos.setItems(obsList);
	}
	
	private void createDialogForm(Grupos obj, String absoluteName, Stage parenteStage) {
		try {
			//carrega a view atraves da variavel absoluteName
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			// passos para carregar dados
			GruposFormController controller = loader.getController();
			controller.setGrupos(obj);
			controller.setGruposService(new GruposService());//injeção de dependencia GruposServices para carregamento
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
	

}
