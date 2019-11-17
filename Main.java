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
        ship.setVel(new Point2D(0,0));
        addGameObj(ship, 300, 300);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };

        timer.start();

        return root;
    }

    private void addBullet(GameObj bullet, double x, double y) {
        bullets.add(bullet);
        addGameObj(bullet, x, y);
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

        bullets.removeIf(GameObj::isDead);
        asteroids.removeIf(GameObj::isDead);

        bullets.forEach(GameObj::update);
        asteroids.forEach(GameObj::update);

        ship.update();

        if (Math.random() < 0.02) {
            addAsteroid(new Asteroid(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
    }

    public static Node createTriangle() {
        Polygon triangle = new Polygon();

        triangle.getPoints().addAll(new Double[]{
                30.0, 10.0,
                0.0, 0.0,
                0.0, 20.0});

        triangle.setStroke(Color.WHITE);

        return  triangle;
    }

    private static class Ship extends GameObj {
        Ship() {
            //super(new Rectangle(40, 20, Color.AQUAMARINE));
            super(createTriangle());
        }
    }

    private static class Bullet extends GameObj {
        Bullet() {
            super(new Circle(5,5,5, Color.WHITESMOKE));
        }
    }

    private static class Asteroid extends GameObj {
        Asteroid() {
            super(new Circle(15,15, 15, Color.WHEAT));
        }
    }

    @Override
    public void start(Stage stage) throws Exception{
        stage.setScene(new Scene(makeStuff()));
        stage.show();
        stage.getScene().setOnKeyPressed( e -> {
            if (e.getCode() == KeyCode.LEFT) {
                ship.rotateLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                ship.rotateRight();
            }

            if (e.getCode() == KeyCode.UP) {
                ship.moveForward(0);
            }

            if (e.getCode() == KeyCode.SPACE) {
                Bullet bullet = new Bullet();
                bullet.setVel(ship.getVel().normalize().multiply(6));
                addBullet(bullet, ship.getView().getTranslateX() - 1, ship.getView().getTranslateY());
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
