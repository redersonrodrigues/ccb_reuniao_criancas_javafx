package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class MainViewController implements Initializable {
	@FXML
	private MenuItem menuItemReunioes;
	@FXML
	private MenuItem menuItemColaboradores;
	@FXML
	private MenuItem menuItemCriancas;
	@FXML
	private MenuItem menuItemSobre;
	
	
	@FXML
	public void onMenuItemReunioesAction() {
		System.out.println("onMenuItemReunioesAction");
	}
	
	@FXML
	public void onMenuItemColaboradoresAction() {
		System.out.println("onMenuItemColaboradoresAction");
	}
	
	@FXML
	public void onMenuItemCriancasAction() {
		System.out.println("onMenuItemCriancasAction");
	}
	
	@FXML
	public void onMenuItemSobreAction() {
		//System.out.println("onMenuItemSobreAction");
		//chama evento de abrir tela
		loadView("/gui/Sobre.fxml");
	}
	
		
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	// função para abrir uma outra tela
	private synchronized void loadView(String absoluteName) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear(); // apaga todos os filhos da VBox da pagina Main
			mainVBox.getChildren().add(mainMenu); // re-adiciona o menu principal
			mainVBox.getChildren().addAll(newVBox.getChildren()); // adiciona os filhos do VBox que desejo,no caso da tela Sobre
			
		} catch (IOException e) {
			
			Alerts.showAlert("IO Exception", "Erro abrindo a view", e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	

}
