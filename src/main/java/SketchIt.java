import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class SketchIt extends Application {

    static double WINDOW_WIDTH = 700;
    static double WINDOW_HEIGHT = 450;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();

        //Canvas
        Rectangle canvas = new Rectangle();
        canvas.setX(0);
        canvas.setY(0);
        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
        canvas.setFill(Color.WHITE);
        canvas.setStroke(Color.WHITE);
        model.setCanvas(canvas);

        MenuView menuView = new MenuView(model);
        ToolbarView toolbarView = new ToolbarView(model);
        CanvasScene canvasScene = new CanvasScene(model);

        toolbarView.setTranslateY(20);
        menuView.prefWidthProperty().bind(primaryStage.widthProperty());

        canvasScene.getGroup().getChildren().addAll(canvas, menuView, toolbarView);


        primaryStage.setScene(canvasScene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Sketch It");
        primaryStage.show();
    }
}
