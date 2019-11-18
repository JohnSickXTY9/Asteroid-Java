package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main extends Application {

    private Pane root;

    private List<GameObj> bullets = new ArrayList<>();
    private List<GameObj> asteroids = new ArrayList<>();

    private GameObj ship;

    private Parent makeStuff() {
        root = new Pane();
        root.setPrefSize(600, 600);
        root.setStyle("-fx-background-color: black;");

        ship = new Ship();
        //ship.setVel(new Point2D(0,0));
        addGameObj(ship, 300, 300);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!ship.isDead()) {
                    onUpdate();
                }
            }
        };

        timer.start();

        if (ship.isDead()) {
            timer.stop();
        }

        return root;
    }

    private void addBullet(GameObj bullet) {
        bullets.add(bullet);
        addGameObj(bullet, ship.getView().getTranslateX() + 8, ship.getView().getTranslateY());
    }

    private void addAsteroid(GameObj asteroid, double x, double y) {
        asteroids.add(asteroid);
        addGameObj(asteroid, x, y);
    }

    private void addGameObj(GameObj object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    private void onUpdate() {
        for (GameObj bullet: bullets) {
            for (GameObj asteroid: asteroids) {
                if (bullet.isColliding(asteroid)) {
                    bullet.setAlive(false);
                    asteroid.setAlive(false);

                    root.getChildren().removeAll(bullet.getView(), asteroid.getView());
                }
            }
        }

        for (GameObj asteroid: asteroids) {
            if (asteroid.isColliding(ship)) {
                ship.setAlive(false);
            }
        }

        bullets.removeIf(GameObj::isDead);
        asteroids.removeIf(GameObj::isDead);

        if (Math.random() > 20000) {
            addAsteroid(new Asteroid(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
    }

    public static Node createTriangle() {
        Polygon triangle = new Polygon();

        triangle.getPoints().addAll(new Double[]{
                20.0, 0.0,
                0.0, -10.0,
                6.0, 0.0,
                0.0, 10.0,
        });

        triangle.setRotate(270);

        triangle.setStroke(Color.WHITE);

        return  triangle;
    }

    //public static Node createb

    private static class Ship extends GameObj {
        Ship() { super(createTriangle()); }
    }

    private static class Bullet extends GameObj {
        Bullet() {
            super(new Circle(2,2,2, Color.WHITE));
        }
    }

    private static class Asteroid extends GameObj {
        Asteroid() {
            super(new Circle(15,15, 15, Color.WHITE));
        }
    }

    @Override
    public void start(Stage stage) throws Exception{
        stage.setScene(new Scene(makeStuff()));
        stage.show();

        AnimationTimer movement = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (ship.isDead()) ship.moveForward(0,0,0);
                else ship.moveForward(6,0,0);
            }
        };

        AnimationTimer rotation = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!ship.isDead()) {
                    ship.rotate();
                }
            }
        };

        stage.getScene().setOnKeyPressed( e -> {
            if (!ship.isDead()) {
                if (e.getCode() == KeyCode.LEFT) {
                    ship.setDirection(5);
                    rotation.start();
                } else if (e.getCode() == KeyCode.RIGHT) {
                    ship.setDirection(-5);
                    rotation.start();
                }

                if (e.getCode() == KeyCode.UP) {
                    movement.start();
                }

                if (e.getCode() == KeyCode.SPACE) {
                    Bullet bullet = new Bullet();
                    bullet.moveForward(12, ship.getView().getTranslateX(), ship.getView().getTranslateY());

                }
            }


        });

        stage.getScene().setOnKeyReleased( e -> {
            if (!ship.isDead()) {
                if (e.getCode() == KeyCode.LEFT) {
                    rotation.stop();
                } else if (e.getCode() == KeyCode.RIGHT) {
                    rotation.stop();
                }

                if (e.getCode() == KeyCode.UP) {
                    movement.stop();
                }

                if (e.getCode() == KeyCode.SPACE) {
                    Bullet bullet = new Bullet();
                    //bullet.setVel(ship.getVel().normalize().multiply(6));
                    //addBullet(bullet, ship.getView().getTranslateX() - 1, ship.getView().getTranslateY());
                }
            }
        });
    }
/*
    public void mUpdate() {
        if (ship.isDead()) { ship.setVel(new Point2D(0,0));
        } else {
            if (e.getActionCommand() ==) {
                ship.rotateLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                ship.rotateRight();
            }

            if (e.getCode() == KeyCode.UP) {
                ship.moveForward(0);
            }

            if (e.getCode() == KeyCode.SPACE) {
                Bullet bullet = new Bullet();
                bullet.setVel(ship.getVel().normalize().multiply(3));
                addBullet(bullet, ship.getView().getTranslateX() - 1, ship.getView().getTranslateY());
            }
        }
    }*/


    public static void main(String[] args) {
        launch(args);
    }
}
