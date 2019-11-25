package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    private void addBullet(GameObj bullet, double x, double y) {
        bullets.add(bullet);
        addGameObj(bullet, x, y);
        bullet.getView().setRotate(ship.getView().getRotate());
        bullet.FindVelocity(ship);
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
        for (GameObj bullet: bullets) { bullet.moveForward(12);}

        for (GameObj bullet: bullets) {
            for (GameObj asteroid: asteroids) {
                if (bullet.isColliding(asteroid)) {
                    bullet.setAlive(false);
                    asteroid.setAlive(false);

                    root.getChildren().removeAll(bullet.getView(), asteroid.getView());
                }
            }

            if((bullet.getView().getTranslateX() < -5) || (bullet.getView().getTranslateX() > 610) || (bullet.getView().getTranslateY() < -5) || (bullet.getView().getTranslateY() > 610)) {
                bullet.setAlive(false);
            }
        }

        for (GameObj asteroid: asteroids) {
            if (asteroid.isColliding(ship)) {
                ship.setAlive(false);
            }
        }

        bullets.removeIf(GameObj::isDead);
        asteroids.removeIf(GameObj::isDead);

        if (Math.random() < 0.1) {
            //addAsteroid(new Asteroid(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
    }

    public static Node createTriangle() {
        Polygon triangle = new Polygon();

        triangle.getPoints().addAll(new Double[]{
                0.0, -10.0,
                6.0, 0.0,
                0.0, 10.0,
                20.0, 0.0
        });

        triangle.setRotate(270);

        triangle.setStroke(Color.WHITE);

        return  triangle;
    }

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

        Label xAxis = new Label("X-Axis: " + Double.toString(ship.getView().getTranslateX()));
        Label yAxis = new Label("Y-Axis: " + Double.toString(ship.getView().getTranslateY()));
        yAxis.setTranslateY(10);
        Label lRotate = new Label("Rotation: " + Double.toString(ship.getRotate()));
        lRotate.setTranslateY(20);
        Label Vel = new Label("Velocity: ( " + Double.toString(ship.getVelocity().getX()) + " , " + Double.toString(ship.getVelocity().getY()) + " )");
        Vel.setTranslateY(30);
        root.getChildren().addAll(lRotate, xAxis, yAxis, Vel);

        AnimationTimer movement = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (ship.isDead()) ship.moveForward(0);
                else if (ship.getView().getTranslateX() < -10) {
                    ship.getView().setTranslateX(600 - ship.getView().getTranslateX());
                    System.out.println("1");

                } else if (ship.getView().getTranslateY() < -10) {
                    ship.getView().setTranslateY(600 - ship.getView().getTranslateY());
                    System.out.println("2");

                } else if (ship.getView().getTranslateX() > 620) {
                    ship.getView().setTranslateX(ship.getView().getTranslateX() - 600);
                    System.out.println("3");

                } else if (ship.getView().getTranslateY() > 620) {
                    ship.getView().setTranslateY(ship.getView().getTranslateY() - 600);
                    System.out.println("4");
                }
                else {
                    ship.moveForward(6/ship.getT());
                }

                ship.setT(ship.getT() + ship.getDrag());

                //DEBUG STATS

                xAxis.setText("X-Axis: " + Double.toString(ship.getView().getTranslateX()));
                yAxis.setText("Y-Axis: " + Double.toString(ship.getView().getTranslateY()));
                Vel.setText("Velocity: ( " + Double.toString(ship.getVelocity().getX()) + " , " + Double.toString(ship.getVelocity().getY()));

            }
        };

        AnimationTimer rotation = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (ship.isAlive()) {
                    ship.rotate();

                    //DEBUG STATS

                    lRotate.setText("Rotation: " + Double.toString(ship.getRotate()));
                }
            }
        };

        AnimationTimer findVel = new AnimationTimer() {
            @Override
            public void handle(long l) {
                ship.FindVelocity(ship);

            }
        };

        stage.getScene().setOnKeyPressed( e -> {
            if (ship.isAlive()) {
                movement.start();

                if (e.getCode() == KeyCode.LEFT) {
                    ship.setDirection(-2.5);
                    rotation.start();

                } else if (e.getCode() == KeyCode.RIGHT) {
                    ship.setDirection(2.5);
                    rotation.start();
                }

                if (e.getCode() == KeyCode.UP) {
                    ship.FindVelocity(ship);
                    ship.setDrag(0);
                    ship.setT(1);
                    findVel.start();
                }

                if (e.getCode() == KeyCode.DOWN) {
                    ship.setDrag(0.1);
                }

                if (e.getCode() == KeyCode.SPACE) {
                    addBullet(new Bullet(), ship.getView().getTranslateX() + 8, ship.getView().getTranslateY() - 5);
                }
            }


        });

        stage.getScene().setOnKeyReleased( e -> {
            if (ship.isAlive()) {
                if ((e.getCode() == KeyCode.LEFT) || (e.getCode() == KeyCode.RIGHT)) {
                    rotation.stop();
                }

                if (e.getCode() == KeyCode.UP) {
                    ship.setDrag(0.01);
                    findVel.stop();
                }

                if (e.getCode() == KeyCode.SPACE) {
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
