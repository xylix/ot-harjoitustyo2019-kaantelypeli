package kaantelypeli.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles level generation and level management. 
 * Exposes loadLevel static method for level generation and
 * gravitate() method for binding gravitation to game ticks.
 */
public class Level {
    public static final String VICTORY = "victory";
    
    private List<Entity> entities;
    int gravity;
    boolean won;
    boolean lost;
    
    private Level() { 
        entities = new ArrayList<>();
        gravity = 0;
        won = false;
        lost = false;
    }

    /**
     * Creates level from entity list.
     * LevelEditor uses this.
     * @param entities list of entities to place on level
     */
    public Level(List<Entity> entities) {
        this();
        this.entities = entities;
    }

    /**
     * Boolean to externally check if level has ended.
     * @return is the level over
     */
    public boolean over() {
        return won;
    }

    /**
     * Changes the gravity by `degrees`.
     *
     * @param   degrees   Wanted change in degrees
     */
    public void changeGravity(int degrees) {
        gravity += degrees;
    }

    /**
     * Applies gravity to all entities on the level.
     * Movable get moved here.
     */
    public void gravitate() {
        if (won) {
            return;
        } else if (lost) {
            entities.forEach(e -> {
                e.hitbox.setTranslateX(0);
                e.hitbox.setTranslateY(0);
            });
            lost = false;
            return;
        }
        
        entities.stream().filter(e -> (e.movable)).forEach(collider -> {
            collider.move(gravity);
            entities.stream().filter(collider::collide).forEach(collidee -> {
                String action = collider.collisionAction(collidee);
                if (!action.isBlank()) {
                    Logger.trace(action);
                }
                switch (action) {
                    case VICTORY:
                        System.out.println("You're winner!");
                        won = true;
                        return;
                    case "loss":
                        lost = true;
                        break;
                    case "open":
                        collidee.passable = true;
                        collidee.hitbox.setFill(Color.TRANSPARENT);
                        collider.movable = false;
                        collider.hitbox.setFill(Color.TRANSPARENT);
                        break;
                    default:
                        if (collidee.passable) {
                            break;
                        } else {
                            collider.move(gravity + 540);
                        }
                }
            });
        });
    }

    /**
     * Converts the level to JSON.
     * @return JSON representation of the level
     */
    public String getJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public List<Rectangle> getHitboxes() {
        return entities.stream().map(e -> e.hitbox).collect(Collectors.toList());
    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }
}
