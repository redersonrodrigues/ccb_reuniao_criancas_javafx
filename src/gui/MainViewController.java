package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("onMenuItemSobreAction");
	}
	
		
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
