import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ToolbarView extends VBox implements IView{
    private Model model;
    private Label selectionLabel;
    private Label eraseLabel;
    private Label drawLineLabel;
    private Label drawCircleLabel;
    private Label drawRectLabel;
    private Label fillLabel;
    private Label lineColorLabel;
    private Label fillColorLabel;
    private Label lineThicknessLabel;
    private Label lineStyleLabel;
    private ColorPicker lineColorPicker;
    private ColorPicker fillColorPicker;
    private ChoiceBox lineThicknessBox;
    private ChoiceBox lineStyleBox;
    private GridPane toolPane;
    private GridPane palettePane;


    ToolbarView(Model model){
        this.model = model;

        //images
        Image selectionImg = new Image(getClass().getResource("select.png").toExternalForm());
        ImageView view = new ImageView(selectionImg);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        selectionLabel = new Label("Select");
        selectionLabel.setGraphic(view);

        Image eraseImg = new Image(getClass().getResource("eraser.png").toExternalForm());
        view = new ImageView(eraseImg);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        eraseLabel = new Label("Eraser");
        eraseLabel.setGraphic(view);


        Image lineImg = new Image(getClass().getResource("line.png").toExternalForm());
        view = new ImageView(lineImg);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        drawLineLabel = new Label("Line");
        drawLineLabel.setGraphic(view);


        Image circleImg = new Image(getClass().getResource("circle.jpeg").toExternalForm());
        view = new ImageView(circleImg);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        drawCircleLabel = new Label("Circle");
        drawCircleLabel.setGraphic(view);


        Image rectImg = new Image(getClass().getResource("rect.png").toExternalForm());
        view = new ImageView(rectImg);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        drawRectLabel = new Label("Rectangle");
        drawRectLabel.setGraphic(view);


        Image fillImg = new Image(getClass().getResource("fill.png").toExternalForm());
        view = new ImageView(fillImg);
        view.setFitHeight(30);
        view.setPreserveRatio(true);
        fillLabel = new Label("Fill");
        fillLabel.setGraphic(view);

        lineColorLabel = new Label("Line Colour: ");
        lineColorPicker = new ColorPicker();
        lineColorPicker.setValue(Color.CORAL);

        fillColorLabel = new Label("Fill Colour: ");
        fillColorPicker = new ColorPicker();
        fillColorPicker.setValue(Color.CORAL);

        lineThicknessLabel = new Label("Line Thickness: ");
        lineThicknessBox = new ChoiceBox(FXCollections.observableArrayList(
                "Small", "Medium", "Large"
        ));
        lineThicknessBox.setValue("Medium");

        lineStyleLabel = new Label("Line Style: ");
        lineStyleBox = new ChoiceBox(FXCollections.observableArrayList(
                "Solid", "Dashed", "Long Dash"
        ));
        lineStyleBox.setValue("Solid");


        //tools
        toolPane = new GridPane();

        toolPane.setPadding(new Insets(10));
        toolPane.setVgap(5);
        toolPane.setHgap(5);
        toolPane.setMinSize(250, 100);

        toolPane.add(drawLineLabel, 0, 0);
        toolPane.add(drawCircleLabel, 1, 0);
        toolPane.add(drawRectLabel, 2, 0);
        toolPane.add(selectionLabel, 0, 1);
        toolPane.add(eraseLabel, 1, 1);
        toolPane.add(fillLabel, 2, 1);

        //palettes
        palettePane = new GridPane();
        palettePane.setPadding(new Insets(10));
        palettePane.setVgap(5);
        palettePane.setHgap(10);
        palettePane.setMinSize(250, 200);

        palettePane.add(lineColorLabel, 0, 0);
        palettePane.add(lineColorPicker, 1, 0);
        palettePane.add(fillColorLabel, 0, 1);
        palettePane.add(fillColorPicker, 1, 1);
        palettePane.add(lineThicknessLabel, 0, 2);
        palettePane.add(lineThicknessBox, 1, 2);
        palettePane.add(lineStyleLabel, 0, 3);
        palettePane.add(lineStyleBox, 1, 3);

        this.setSpacing(20);
        this.getChildren().addAll(toolPane, palettePane);
        this.registerControllers();

        model.addView(this);
    }


    @Override
    public void updateView() {
        this.toFront();
        if(model.getSelectedShape() != null) {
            lineColorPicker.setValue((Color) model.getSelectedShape().getStroke());
            fillColorPicker.setValue((Color) model.getSelectedShape().getFill());
            double tempStyle = model.getSelectedShape().getStrokeDashArray().get(0);
            if (tempStyle == 25d) {
                lineStyleBox.setValue("Dashed");
            } else if (tempStyle == 5d) {
                lineStyleBox.setValue("Long Dash");
            } else {
                lineStyleBox.setValue("Solid");
            }

            double tempThickness = model.getSelectedShape().getStrokeWidth();
            if (tempThickness == 1.0) {
                lineThicknessBox.setValue("Small");
            } else if (tempThickness == 3.0) {
                lineThicknessBox.setValue("Medium");
            } else {
                lineThicknessBox.setValue("Large");
            }
        }
    }

    private void registerControllers(){
        drawLineLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.setAction("drawline");
                System.out.println("drawline");
            }
        });

        drawCircleLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.deselectShape();
                model.setAction("drawcircle");
            }
        });

        drawRectLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.setAction("drawrect");
            }
        });

        selectionLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.setAction("select");
            }
        });

        eraseLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.setAction("erase");
            }
        });

        fillLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.setAction("fill");
            }
        });

        lineColorPicker.setOnAction(new EventHandler(){

            @Override
            public void handle(Event event) {
                model.setLinePaint(lineColorPicker.getValue());
            }
        });

        fillColorPicker.setOnAction(new EventHandler(){

            @Override
            public void handle(Event event) {
                model.setFillPaint(fillColorPicker.getValue());
            }
        });

        lineThicknessBox.setOnAction(new EventHandler(){

            @Override
            public void handle(Event event) {
                if(lineThicknessBox.getValue().equals("Small")){
                    model.setLineThickness(1.0);
                }
                else if(lineThicknessBox.getValue().equals("Medium")){
                    model.setLineThickness(3.0);
                }
                else if(lineThicknessBox.getValue().equals("Large")){
                    model.setLineThickness(5.0);
                }
            }
        });

        lineStyleBox.setOnAction(new EventHandler(){


            @Override
            public void handle(Event event) {
                ArrayList<Double> arr = new ArrayList<>();
                if(lineStyleBox.getValue().equals("Dashed")){
                    arr.add(25d);
                    arr.add(10d);
                }
                else if(lineStyleBox.getValue().equals("Long Dash")){
                    arr.add(5d);
                    arr.add(21d);
                }
                else{
                    arr.add(2d);
                    arr.add(2d);
                }
                model.setLineStyle(arr);
            }
        });


    }
}
