package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	
// função para acessar o Stage onde o controle que recebeu o evento se encontra
	// por exemplo, se clicar num botão, eu recebo o Stage deste botão
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
