package kaantelypeli.engine;

import java.util.ArrayList;
import java.util.Collection;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Level {
    private final Collection<Entity> entities;
    private Entity player;
    int gravity;
    boolean victory = false;
    
    public Level() { 
        entities = new ArrayList<>();
        gravity = 0;
    }
    
    public static Level loadLevel(int levelIndex) {
        switch (levelIndex) {
            case 0:
                return zero();
            case 1:
                return one();
            default:
                return new Level();
                
        } 
    }
    
    private static Level zero() {
        Level level = new Level();
        Entity playerZero = new Entity("player", new Point2D(0, 16));
        level.entities.add(playerZero);
        level.entities.add(new Entity("victory", new Point2D(0, 48)));

        return level;
    }
    
    private static Level one() {
        Level level = new Level();
        Entity player = new Entity("player", new Point2D(32, 32));
        level.player = player;
        level.entities.add(player);
        level.entities.add(new Entity("wall", new Point2D(96, 48)));

        for (int i = 0; i < 15; i++) {
            level.entities.add(new Entity("wall", new Point2D(i * 16, 0)));
            level.entities.add(new Entity("wall", new Point2D(i * 16, 224)));
            level.entities.add(new Entity("wall", new Point2D(0, i * 16)));
            level.entities.add(new Entity("wall", new Point2D(224, i * 16)));
        }

        level.entities.add(new Entity("victory", new Point2D(96, 16)));
        return level;
    }
    
    public Collection<Entity> getEntities() {
        return this.entities;
    }
    
    public void changeGravity(int degrees) {
        gravity += degrees;
    }

    public void gravitate() {
        entities.stream().filter(e -> (e.movable)).forEach(collider -> {
            if (victory) {
                return;
            }
            collider.move(gravity);
            
            entities.stream().filter(collidee -> collider.collide(collidee)).forEach(collidee -> {
                String action = collider.collisionAction(collidee);
                switch (action) {
                    case "victory":
                        System.out.println("You're winner!");
                        victory = true;
                        return;
                    case "open":
                        collidee.setFill(Color.PINK);
                        break;
                    case "blocked":
                        collider.move(gravity + 540);
                    default:
                        break;
                }
            });
        });
    }
}
