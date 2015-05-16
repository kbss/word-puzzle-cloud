package org.forsp.game.storage.entity;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

/**
 * @author Serhii Kryvtsov
 * @since 07/05/2015
 */
@Entity
public class GameScore {

    @Id
    private Long id;

    @Parent
    @Load
    private Ref<Puzzle> game;
    private String name;
    @Index
    private Long score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Puzzle getGame() {
        if (game == null) {
            return null;
        }
        return game.get();
    }

    public void setGame(Puzzle game) {
        this.game = Ref.create(game);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
