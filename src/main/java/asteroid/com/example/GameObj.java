package asteroid.com.example;

import javafx.geometry.Point2D;
import javafx.scene.Node;

// import java.awt.*;

public class GameObj {

    private Node view;
    private double direction;
    private Point2D velocity = new Point2D(0,0);
    private double drag  = 0.0;
    private double t = 1.0;

    private boolean alive = true;

    public void setT(double t) {
        this.t = t;
    }

    public double getT() {
        return t;
    }

    public void setDrag(double drag) {
        this.drag = drag;
    }

    public double getDrag() {
        return drag;
    }

    public GameObj(Node view) {
        this.view = view;
    }

    public Node getView() {
        return view;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() {
        return !alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void rotate() {
        if (((this.view.getRotate() + direction) == 355.0)) {
            this.view.setRotate(0.0);
        } else if ((this.view.getRotate() + direction) == 0.0) {
            this.view.setRotate(355);
        } else { this.view.setRotate(this.view.getRotate() + direction); }
    }

    public double getRotate() {
        return this.view.getRotate();
    }

    public void FindVelocity(GameObj object) {
        this.velocity = new Point2D(Math.cos(Math.toRadians(object.getRotate())), Math.sin(Math.toRadians(object.getRotate())));
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getVelocity() {
        return new Point2D(Math.cos(Math.toRadians(this.view.getRotate())), Math.sin(Math.toRadians(this.view.getRotate())));
    }

    public void moveForward (double momentum) {
        this.view.setTranslateX(this.view.getTranslateX() + velocity.getX() * momentum / this.getT());
        this.view.setTranslateY(this.view.getTranslateY() + velocity.getY() * momentum / this.getT());
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public boolean isColliding(GameObj other) {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }

}
