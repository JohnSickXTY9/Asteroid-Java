package sample;

import javafx.geometry.Point2D;
import javafx.scene.Node;

public class GameObj {

    private Node view;
    private Point2D vel = new Point2D(0,0);

    private boolean alive = true;

    public GameObj(Node view) {
        this.view = view;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() + vel.getX());
        view.setTranslateY(view.getTranslateY() + vel.getY());
    }

    public void setVel(Point2D vel) {
        this.vel = vel;
    }

    public Point2D getVel() {
        return vel;
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

    public double getRotate() {
        return view.getRotate();
    }

    public void moveForward(double drag) {
        setVel(new Point2D(Math.cos(Math.toRadians(getRotate())) - drag, Math.sin(Math.toRadians(getRotate())) - drag));
    }

    public void rotateRight() {
        view.setRotate(view.getRotate() + 5);
        moveForward(0.1);
    }

    public void rotateLeft() {
        view.setRotate(view.getRotate() - 5);
        moveForward(0.1);
    }

    public boolean isColliding(GameObj other) {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }

}
