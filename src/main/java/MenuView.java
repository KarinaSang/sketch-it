import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

public class MenuView extends MenuBar implements IView {

    final String DELIMITER = ",";
    final String ENDL = "\n";

    private Model model;
    private Menu fileMenu;
    private MenuItem fileNew;
    private MenuItem fileLoad;
    private MenuItem fileSave;
    private MenuItem fileQuit;
    private Menu helpMenu;
    private MenuItem helpAbout;
    Dialog<ButtonType> dialog;
    Dialog<ButtonType> saveDialog;


    MenuView(Model model){
        this.model = model;

        dialog = new Dialog();
        dialog.setTitle("Help");
        dialog.setContentText("Sketch It! \n Karina Sang \n 20760311 \n");

        saveDialog = new Dialog();
        saveDialog.setTitle("Warning!");
        dialog.setContentText("Do you want to save the current drawing or discard it?");

        fileMenu = new Menu("File");
        fileNew = new MenuItem("New");
        fileLoad = new MenuItem("Load");
        fileSave = new MenuItem("Save");
        fileQuit = new MenuItem("Quit");
        fileMenu.getItems().addAll(fileNew, fileLoad, fileSave, fileQuit);


        helpMenu = new Menu("Help");
        helpAbout = new MenuItem("About");
        helpMenu.getItems().addAll(helpAbout);


        //Map accelerator keys to menu items
        fileNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        fileLoad.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
        fileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        fileQuit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        helpAbout.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));

        this.getMenus().addAll(fileMenu, helpMenu);

        this.registerControllers();

        model.addView(this);
    }

    @Override
    public void updateView() {

    }


    private void registerControllers() {

        //Handlers
        fileNew.setOnAction(actionEvent -> {
            tryAndSave(actionEvent);
            model.getShapes().forEach(shape -> {
                shape.setFill(Color.WHITE);
                shape.setStroke(Color.WHITE);
            });
            model.setShapes(new LinkedList<Shape>());
            model.setAction(" ");
        });

        fileLoad.setOnAction(actionEvent -> {
            tryAndSave(actionEvent);
            load("data.txt");
        });

        fileSave.setOnAction(actionEvent -> {
            save("data.txt");
        });

        fileQuit.setOnAction(actionEvent -> {
            tryAndSave(actionEvent);
            System.exit(0);
        });

        helpAbout.setOnAction(actionEvent -> {
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().setAll(okButton);
            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get() == okButton){
                actionEvent.consume();
                dialog.close();
            }
        });
    }

    private void tryAndSave(ActionEvent actionEvent) {
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType discardButton = new ButtonType("Discard", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().setAll(saveButton, discardButton);
        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == saveButton){
            save("data.txt");
            actionEvent.consume();
            dialog.close();
        }
        else if(result.isPresent() && result.get() == discardButton){
            //...
            actionEvent.consume();
            dialog.close();
        }
    }

    private ArrayList<Double> parseStrokeArray(String value){
        ArrayList<Double> strokeArr = new ArrayList<>();
        if(Double.parseDouble(value) == 25d){
            strokeArr.add(25d);
            strokeArr.add(10d);
        }
        else if(Double.parseDouble(value) == 5d){
            strokeArr.add(5d);
            strokeArr.add(21d);
        }
        else{
            strokeArr.add(2d);
            strokeArr.add(2d);
        }
        return strokeArr;
    }

    private void load(String filename){
        CanvasScene scene = (CanvasScene) model.getCanvas().getScene();
        MenuView menuView = new MenuView(model);
        menuView.prefWidthProperty().bind(model.getCanvas().getScene().widthProperty());
        ToolbarView toolbarView = new ToolbarView(model);
        toolbarView.setTranslateY(20);
        scene.getCanvas().toFront();
        scene.getGroup().getChildren().addAll(menuView, toolbarView);

        FileReader file = null;
        BufferedReader reader = null;
        String[] values;
        LinkedList<Shape> shapes = new LinkedList<Shape>();

        try{
            file = new FileReader(filename);
            reader = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{
            String line;
            while((line = reader.readLine()) != null){
                values = line.split(DELIMITER);


                if(values[0].equals("line")){
                    Line l = new Line(Double.parseDouble(values[1]),
                            Double.parseDouble(values[2]),
                            Double.parseDouble(values[3]),
                            Double.parseDouble(values[4]));
                    l.setStroke(Color.valueOf(values[5]));
                    l.getStrokeDashArray().addAll(parseStrokeArray(values[6]));
                    l.setStrokeWidth(Double.parseDouble(values[7]));
                    shapes.add(l);
                    scene.getGroup().getChildren().add(l);
                    scene.setShapeListeners(l);
                }
                else if(values[0].equals("circle")){
                    Circle c = new Circle(Double.parseDouble(values[1]),
                            Double.parseDouble(values[2]),
                            Double.parseDouble(values[3]));
                    c.setStroke(Color.valueOf(values[4]));
                    c.getStrokeDashArray().addAll(parseStrokeArray(values[5]));
                    c.setStrokeWidth(Double.parseDouble(values[6]));
                    c.setFill(Color.valueOf(values[7]));
                    shapes.add(c);
                    scene.getGroup().getChildren().add(c);
                    scene.setShapeListeners(c);
                }
                else if(values[0].equals("rect")){
                    Rectangle r = new Rectangle(Double.parseDouble(values[1]),
                            Double.parseDouble(values[2]),
                            Double.parseDouble(values[3]),
                            Double.parseDouble(values[4]));
                    r.setStroke(Color.valueOf(values[5]));
                    r.getStrokeDashArray().addAll(parseStrokeArray(values[6]));
                    r.setStrokeWidth(Double.parseDouble(values[7]));
                    r.setFill(Color.valueOf(values[8]));
                    shapes.add(r);
                    scene.getGroup().getChildren().add(r);
                    scene.setShapeListeners(r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.setShapes(shapes);
    }

    private void save(String filename) {
        FileWriter file = null;
        BufferedWriter writer = null;
        try {
            file = new FileWriter(filename);
            writer = new BufferedWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //SAMPLE OUTPUT: (RECTANGLE,X,Y,W,H,
            //linepaint,linestyle,linethickness,fillpaint)
            BufferedWriter finalWriter = writer;
            model.getShapes().forEach(shape -> {
                if (shape instanceof Line) {
                    try {
                        finalWriter.write("line" + DELIMITER +
                                ((Line) shape).getStartX() + DELIMITER +
                                ((Line) shape).getStartY() + DELIMITER +
                                ((Line) shape).getEndX() + DELIMITER +
                                ((Line) shape).getEndY() + DELIMITER +
                                shape.getStroke().toString() + DELIMITER +
                                shape.getStrokeDashArray().get(0) + DELIMITER +
                                shape.getStrokeWidth() + ENDL
                            );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (shape instanceof Circle) {
                    try {
                        finalWriter.write("circle" + DELIMITER +
                                ((Circle) shape).getCenterX() + DELIMITER +
                                ((Circle) shape).getCenterY() + DELIMITER +
                                ((Circle) shape).getRadius() + DELIMITER +
                                shape.getStroke().toString() + DELIMITER +
                                shape.getStrokeDashArray().get(0) + DELIMITER +
                                shape.getStrokeWidth() + DELIMITER +
                                shape.getFill().toString() + ENDL
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (shape instanceof Rectangle) {
                    try {
                        finalWriter.write("rect" + DELIMITER +
                                ((Rectangle) shape).getX() + DELIMITER +
                                ((Rectangle) shape).getY() + DELIMITER +
                                ((Rectangle) shape).getWidth() + DELIMITER +
                                ((Rectangle) shape).getHeight() + DELIMITER +
                                shape.getStroke().toString() + DELIMITER +
                                shape.getStrokeDashArray().get(0) + DELIMITER +
                                shape.getStrokeWidth() + DELIMITER +
                                shape.getFill().toString() + ENDL
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            writer.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
