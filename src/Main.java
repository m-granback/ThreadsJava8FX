import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Demo f�r att visa hur man kan uppdatera ett grafiskt element i JavaFX fr�n en annan tr�d �n
 * huvudtr�den. Platform.runLater() tar ett Runnable-objekt som argument och k�r det p� huvudtr�den
 * "s� fort som m�jligt". Genom att anv�nda sig av Platform.runLater() kan man undvika problem med
 * tr�ds�kerhet och synkronisering n�r man hanterar grafiska komponenter som i detta fall.
 * I detta exempel gjorde jag om s� att i st�llet f�r att uppdatera grafiska element DIREKT fr�n en annan
 * tr�d �n JavaFX-tr�den, s� utf�r jag loopen i sig p� annan tr�d, men ber sedan JavaFX-tr�den att applicera
 * f�r�ndringarna p� de grafiska elementen.
 */
public class Main extends Application {
    private static final double WINDOWSIZE = 400;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(WINDOWSIZE, WINDOWSIZE);

        for (int i = 0; i < WINDOWSIZE/40; i++) {
            for (int j = 0; j < WINDOWSIZE/40; j++) {
                Rectangle rectangle = new Rectangle(WINDOWSIZE/40-2, WINDOWSIZE/40-2);
                rectangle.setLayoutX(i*(WINDOWSIZE/40));
                rectangle.setLayoutY(j*(WINDOWSIZE /40));
                rectangle.setFill(Color.NAVY);
                anchorPane.getChildren().add(rectangle);
            }
        }

        Button button = new Button("Testa mig");
        button.setLayoutX(WINDOWSIZE/2);
        button.setOnAction((e)-> {
            Alert msgBox = new Alert(Alert.AlertType.INFORMATION);
            msgBox.show();
            System.out.println(e.getEventType().getName());
        });
        anchorPane.getChildren().add(button);

        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        Runnable updateInstructions = () -> {
            for(Node r: anchorPane.getChildren()){
                if(r instanceof Rectangle){
                    Rectangle currentRectangle = (Rectangle) r;
                    Platform.runLater(()->currentRectangle.setFill(Color.RED));
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
            }
        };
        Thread thread = new Thread(updateInstructions);
        thread.start(); // F�r att se skillnaden med endast en tr�d, kommentera ut denna...
//        updateMap(anchorPane); // ...och kommentera tillbaka denna
    }
    // Denna metod ligger kvar f�r demonstrationen av att k�ra fr�n en tr�d
    private void updateMap(AnchorPane anchorPane){
        for (Node r : anchorPane.getChildren()) {
            if(r instanceof Rectangle)
                ((Rectangle)r).setFill(Color.RED);
            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
}