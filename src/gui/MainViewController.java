package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.CidadesService;
import model.services.EstadosService;
import model.services.GruposService;

public class MainViewController implements Initializable {
	@FXML
	private MenuItem menuItemReunioes;
	@FXML
	private MenuItem menuItemPessoas;
	@FXML
	private MenuItem menuItemGrupos;
	@FXML
	private MenuItem menuItemEstados;
	@FXML
	private MenuItem menuItemCidades;
	@FXML
	private MenuItem menuItemTiposUsuarios;
	@FXML
	private MenuItem menuItemEquipes;
	@FXML
	private MenuItem menuItemSobre;

	@FXML
	public void onMenuItemReunioesAction() {
		System.out.println("onMenuItemReunioesAction");
	}

	@FXML
	public void onMenuItemPessoasAction() {
		System.out.println("onMenuItemPessoasAction");
	}

	@FXML
	public void onMenuItemGruposAction() {
		// System.out.println("onMenuItemGruposAction");
		loadView("/gui/GruposList.fxml", (GruposListController controller) ->{
			controller.setGruposService(new GruposService());
			controller.updateTableView();
			
		});

	}

	@FXML
	public void onMenuItemEstadosAction() {
		//System.out.println("onMenuItemEstasdosAction");
		loadView("/gui/EstadosList.fxml", (EstadosListController controller) ->{
			controller.setEstadosService(new EstadosService());
			controller.updateTableView();
			
		});
		
		
	}
	
	@FXML
	public void onMenuItemCidadesAction() {
		//System.out.println("onMenuItemCidadesAction");
		loadView("/gui/CidadesList.fxml", (CidadesListController controller) ->{
			controller.setCidadesService(new CidadesService());
			controller.updateTableView();
			
		});
		
	}

	@FXML
	public void onMenuItemTiposUsuariosAction() {
		System.out.println("onMenuItemTiposUsuariosAction");
	}
	
	@FXML
	public void onMenuItemEquipesAction() {
		System.out.println("onMenuItemEquipesAction");
	}


	@FXML
	public void onMenuItemSobreAction() {
		// System.out.println("onMenuItemSobreAction");
		// chama evento de abrir tela
		loadView("/gui/Sobre.fxml", x ->{});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	// função para abrir uma outra tela
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();

			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear(); // apaga todos os filhos da VBox da pagina Main
			mainVBox.getChildren().add(mainMenu); // re-adiciona o menu principal
			mainVBox.getChildren().addAll(newVBox.getChildren()); // adiciona os filhos do VBox que desejo,no caso da
																	// tela Sobre
			
			// passos para executar a função qeu passar como argumento na abertura da tela
			T controller = loader.getController();
			initializingAction.accept(controller);
			
			
			
		} catch (IOException e) {

			Alerts.showAlert("IO Exception", "Erro abrindo a view", e.getMessage(), AlertType.ERROR);
		}

	}

	
}
