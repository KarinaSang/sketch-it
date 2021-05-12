import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.LinkedList;

public class Model {

    private ArrayList<IView> views = new ArrayList<IView>();
    private LinkedList<Shape> shapes = new LinkedList<Shape>();
    private Shape selectedShape;
    private Rectangle canvas;
    private Paint fillPaint;

    private Paint linePaint;
    private double lineThickness;
    private ArrayList<Double> lineStyle;

    private String action;
    private boolean isNewShape;

    Model(){
        fillPaint = Color.web("FF7F50");
        linePaint = Color.web("FF7F50");
        lineThickness = 3.0;
        lineStyle = new ArrayList<Double>();
        lineStyle.add(2d);
        action = "";
        isNewShape = false;

    }

    public void setShapes(LinkedList<Shape> shapes) {
        this.shapes = shapes;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    public double getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(Double lineThickness) {
        this.lineThickness = lineThickness;
    }

    public ArrayList<Double> getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(ArrayList<Double> lineStyle) {
        this.lineStyle = lineStyle;
    }

    public boolean getIsNewShape(){
        return isNewShape;
    }

    public void setIsNewShape(boolean isNewShape){
        this.isNewShape = isNewShape;
    }

    public Paint getFillPaint(){
        return fillPaint;
    }

    public void setFillPaint(Paint paint){
        this.fillPaint = paint;
    }


    public void setCanvas(Rectangle canvas){
        this.canvas = canvas;
    }

    public Rectangle getCanvas(){
        return canvas;
    }

    public void addShape(Shape shape){
        this.shapes.add(shape);
        isNewShape = true;
        this.notifyObservers();
    }

    public void removeShape(Shape shape){
        shapes.remove(shape);
        this.notifyObservers();
    }

    public void selectShape(Shape shape){
        this.selectedShape = shape;
        this.notifyObservers();
    }

    public void deselectShape(){
        this.selectedShape = null;
        this.notifyObservers();
    }

    public String getAction(){
        return action;
    }

    public void setAction(String string){
        this.action = string;
    }


    public LinkedList<Shape> getShapes(){
        return shapes;
    }

    public Shape getSelectedShape(){
        return selectedShape;
    }

    public void addView(IView view){
        views.add(view);
        view.updateView();
    }

    private void notifyObservers(){
        for(IView view : this.views){
            view.updateView();
        }
    }


}
