package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;

public class GameObj {

    private Node view;
    private double direction;
    //private Point2D vel = new Point2D(0,0);

    private boolean alive = true;

    public GameObj(Node view) {
        this.view = view;
    }

    //public void setVel(Point2D vel) { this.vel = vel; }

    //public Point2D getVel() { return vel; }

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

    public double getRotate() {
        return view.getRotate();
    }

    public void moveForward (double moveSpeed, double x, double y) {
        this.view.setTranslateX(this.view.getTranslateX() + Math.cos(Math.toRadians(getRotate())) * moveSpeed);
        this.view.setTranslateY(this.view.getTranslateY() + Math.sin(Math.toRadians(getRotate())) * moveSpeed);
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void rotate() {
        this.view.setRotate(this.view.getRotate() + direction);
    }

    public boolean isColliding(GameObj other) {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }

}
