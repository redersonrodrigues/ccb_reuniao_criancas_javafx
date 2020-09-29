package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtNovoAction() {
		System.out.println("onBtNovoAction");
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
	
	
	

}
