import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

public class CanvasScene extends Scene implements IView{
    static double CANVAS_WIDTH = 400;
    static double CANVAS_HEIGHT = 400;

    private Model model;
    private Group root;

    public Rectangle getCanvas() {
        return canvas;
    }

    private Rectangle canvas;


    CanvasScene(Model model){
        super(new Group(), SketchIt.WINDOW_WIDTH, SketchIt.WINDOW_HEIGHT);
        this.model = model;
        root = (Group)this.getRoot();
        canvas = model.getCanvas();

        drawShapes(canvas);


        model.addView(this);
    }


    public Group getGroup(){
        return root;
    }

    @Override
    public void updateView() {
        if(model.getIsNewShape()){
            setShapeListeners(model.getShapes().getLast());
            root.getChildren().add(model.getShapes().getLast());
        }

        model.setIsNewShape(false);
    }


    private void drawShapes(Shape block){
        Delta start = new Delta();
        Delta end = new Delta();


        block.setOnMousePressed(mouseEvent -> {
            if(model.getSelectedShape() != null){
                model.getSelectedShape().setStroke(model.getLinePaint());
                model.deselectShape();
            }
            if(model.getAction().equals("drawline")){
                start.x = mouseEvent.getSceneX();
                start.y = mouseEvent.getSceneY();
                System.out.println("drawing");
            }
            else if(model.getAction().equals("drawcircle")){
                start.x = mouseEvent.getSceneX();
                start.y = mouseEvent.getSceneY();
            }
            else if(model.getAction().equals("drawrect")){
                start.x = mouseEvent.getSceneX();
                start.y = mouseEvent.getSceneY();
            }
        });

        block.setOnMouseReleased(mouseEvent -> {
            if(model.getAction().equals("drawline")){
                end.x = mouseEvent.getSceneX();
                end.y = mouseEvent.getSceneY();
                drawLine(start, end);
                System.out.println("drew");
            }
            else if(model.getAction().equals("drawcircle")){
                end.x = mouseEvent.getSceneX();
                end.y = mouseEvent.getSceneY();
                drawCircle(start, end);
            }
            else if(model.getAction().equals("drawrect")){
                end.x = mouseEvent.getSceneX();
                end.y = mouseEvent.getSceneY();
                drawRect(start, end);
            }
        });


    }



    private void drawLine(Delta start, Delta end){
        Line line = new Line(start.x, start.y, end.x, end.y);
        line.setStroke(model.getLinePaint());
        line.getStrokeDashArray().addAll(model.getLineStyle());
        line.setStrokeWidth(model.getLineThickness());
        model.addShape(line);
    }

    private void drawCircle(Delta start, Delta end){
        double dia = Math.sqrt(Math.pow(end.x-start.x, 2)+
                Math.pow(end.y-start.y, 2));
        Circle circle = new Circle((end.x+start.x)/2,
                (end.y+start.y)/2, dia/2);
        circle.setStroke(model.getLinePaint());
        circle.getStrokeDashArray().addAll(model.getLineStyle());
        circle.setStrokeWidth(model.getLineThickness());
        circle.setFill(model.getFillPaint());
        model.addShape(circle);
    }

    private void drawRect(Delta start, Delta end){
        Rectangle rect = new Rectangle(start.x, start.y,
                Math.abs(end.x-start.x), Math.abs(end.y-start.y));
        rect.setStroke(model.getLinePaint());
        rect.getStrokeDashArray().addAll(model.getLineStyle());
        rect.setStrokeWidth(model.getLineThickness());
        rect.setFill(model.getFillPaint());
        model.addShape(rect);
    }

    //source: sample CircleHit.java
    public void setShapeListeners(final Shape block) {
        final Delta dragDelta = new Delta();
        final Delta start = new Delta();
        final Delta end = new Delta();

        EventHandler<MouseEvent> shape_pressed = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if(model.getSelectedShape() != null){
                    model.getSelectedShape().setStroke(model.getLinePaint());
                    model.deselectShape();
                }

                // record a delta distance for the drag and drop operation.
                dragDelta.x = block.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = block.getLayoutY() - mouseEvent.getSceneY();
                if(model.getAction().equals("select")){
                    //shape is selected
                    model.selectShape(block);
                    block.setStroke(Color.RED);
                    block.setCursor(Cursor.NONE);
                }
                else if(model.getAction().equals("drawline")){
                    start.x = mouseEvent.getSceneX();
                    start.y = mouseEvent.getSceneY();
                    System.out.println("drawing");
                }
                else if(model.getAction().equals("drawcircle")){
                    start.x = mouseEvent.getSceneX();
                    start.y = mouseEvent.getSceneY();
                }
                else if(model.getAction().equals("drawrect")){
                    start.x = mouseEvent.getSceneX();
                    start.y = mouseEvent.getSceneY();
                }

            }
        };

        EventHandler<MouseEvent> shape_dragged = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if(model.getAction().equals("select")) {
                    block.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                    block.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                }
            }
        };

        EventHandler<MouseEvent> shape_released = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if(model.getAction().equals("select")) {
                    block.setCursor(Cursor.HAND);
                }
                else if (model.getAction().equals("erase")) {
                    block.removeEventHandler(MouseEvent.MOUSE_RELEASED, this);
                    block.removeEventHandler(MouseEvent.MOUSE_DRAGGED, shape_dragged);
                    block.removeEventHandler(MouseEvent.MOUSE_PRESSED, shape_pressed);
                    block.toBack();
                    block.setFill(Color.WHITE);
                    block.setStroke(Color.WHITE);
                }
                else if (model.getAction().equals("fill")) {
                    block.setFill(model.getFillPaint());
                }
                else if(model.getAction().equals("drawline")){
                    end.x = mouseEvent.getSceneX();
                    end.y = mouseEvent.getSceneY();
                    drawLine(start, end);
                    System.out.println("drew");
                }
                else if(model.getAction().equals("drawcircle")){
                    end.x = mouseEvent.getSceneX();
                    end.y = mouseEvent.getSceneY();
                    drawCircle(start, end);
                }
                else if(model.getAction().equals("drawrect")){
                    end.x = mouseEvent.getSceneX();
                    end.y = mouseEvent.getSceneY();
                    drawRect(start, end);
                }
            }
        };


        block.setOnMousePressed(shape_pressed);
        block.setOnMouseDragged(shape_dragged);
        block.setOnMouseReleased(shape_released);


        this.setOnKeyPressed(event ->{
            if(event.getCode().equals(KeyCode.DELETE)) {
                Shape newSelected = model.getSelectedShape();
                if (newSelected != null) {
                    newSelected.removeEventHandler(MouseEvent.MOUSE_RELEASED, shape_released);
                    newSelected.removeEventHandler(MouseEvent.MOUSE_DRAGGED, shape_dragged);
                    newSelected.removeEventHandler(MouseEvent.MOUSE_PRESSED, shape_pressed);
                    newSelected.toBack();
                    newSelected.setFill(Color.WHITE);
                    newSelected.setStroke(Color.WHITE);
                }
            }
                else if(event.getCode().equals(KeyCode.ESCAPE)){
                    if(model.getSelectedShape() != null){
                        model.getSelectedShape().setStroke(model.getLinePaint());
                        model.deselectShape();
                    }
                }
        });
    }

    class Delta { double x, y; }


}
