package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	
// fun��o para acessar o Stage onde o controle que recebeu o evento se encontra
	// por exemplo, se clicar num bot�o, eu recebo o Stage deste bot�o
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
