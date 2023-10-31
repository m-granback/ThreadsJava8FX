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
 * Demo för att visa hur man kan uppdatera ett grafiskt element i JavaFX från en annan tråd än
 * huvudtråden. Platform.runLater() tar ett Runnable-objekt som argument och kör det på huvudtråden
 * "så fort som möjligt". Genom att använda sig av Platform.runLater() kan man undvika problem med
 * trådsäkerhet och synkronisering när man hanterar grafiska komponenter som i detta fall.
 * I detta exempel gjorde jag om så att i stället för att uppdatera grafiska element DIREKT från en annan
 * tråd än JavaFX-tråden, så utför jag loopen i sig på annan tråd, men ber sedan JavaFX-tråden att applicera
 * förändringarna på de grafiska elementen.
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
        thread.start(); // För att se skillnaden med endast en tråd, kommentera ut denna...
//        updateMap(anchorPane); // ...och kommentera tillbaka denna
    }
    // Denna metod ligger kvar för demonstrationen av att köra från en tråd
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