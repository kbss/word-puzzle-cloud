package org.forsp.game;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.ObjectifyService;
import org.apache.commons.lang3.StringUtils;
import org.forsp.game.service.*;
import org.forsp.game.storage.entity.GameScore;
import org.forsp.game.storage.entity.Puzzle;

import java.util.*;

/**
 * Add your first API methods in this class, or you may create another class. In that case, please
 * update your web.xml accordingly.
 */
@Api(name = "puzzle", version = "v1", description = "Word puzzle game API")
public class WordPuzzleGameApi {

    @ApiMethod(name = "leaders")
    public Collection<GameScore> getLeaderBoard() {
        return getScore();

    }

    @ApiMethod(name = "load")
    public Board getGameBoard(@Named("id") Long id) {
        validateRequest(id);
        Puzzle puzzle = getPuzzle(id);
        return getPuzzle(puzzle);
    }

    private Puzzle getPuzzle(Long id) {
        Puzzle puzzle = ObjectifyService.ofy().load().type(Puzzle.class).id(id).now();

        if (puzzle == null) {
            throw new RuntimeException("Puzzle not found");
        }
        return puzzle;
    }


    private Board getPuzzle(Puzzle puzzle) {
        WordPuzzle game = new WordPuzzle(puzzle.getContent(), puzzle.getWords());
        return game.getBoard();
    }


    private WordPuzzle getGame(Long id) {
        Puzzle puzzle = getPuzzle(id);
        return new WordPuzzle(puzzle.getContent(), puzzle.getWords());
    }

    @ApiMethod(name = "add")
    public void createGame(Puzzle game) {
        validateRequest(game);

        if (StringUtils.isBlank(game.getContent())) {
            throw new RuntimeException("Sequence should not be empty");
        }
        if (StringUtils.isBlank(game.getName())) {
            throw new RuntimeException("Name is required");
        }
        if (game.getWords() == null || game.getWords().isEmpty()) {
            throw new RuntimeException("At least one word required");
        }
        game.setContent(game.getContent().toUpperCase());
        Set<String> words = new HashSet<>();
        for (String word : game.getWords()) {
            words.add(word.toUpperCase());
        }
        //Async add
        ObjectifyService.ofy().save().entity(game);
    }

    private void validateRequest(Object game) {
        if (game == null) {
            throw new RuntimeException("Invalid request");
        }
    }

    @ApiMethod(name = "getAllGames")
    public Collection<GameDto> getAllGames() {
        Collection<GameDto> games = new ArrayList<>();
        Collection<Puzzle> puzzle = ObjectifyService.ofy()
                .load()
                .type(Puzzle.class).list();
        for (Puzzle p : puzzle) {
            GameDto dto = new GameDto();
            dto.setId(p.getId());
            dto.setBoard(getPuzzle(p).getBoard());
            dto.setName(p.getName());
            dto.setWords(p.getWords());
            games.add(dto);
        }
        return games;
    }

    @ApiMethod(name = "checkWord")
    public Word checkWord(Word word) {
        validateRequest(word);
        validateRequest(word.getGameId());
        if (word.getPoints() == null || word.getPoints().size() != 2) {
            throw new RuntimeException("Expected two points");
        }
        WordPuzzle puzzle = getGame(word.getGameId());
        Iterator<Point> it = word.getPoints().iterator();
        Word result = puzzle.getWord(it.next(), it.next());
        if (result == null) {
            throw new RuntimeException("Word not found");
        }
        return result;
    }

    private Collection<GameScore> getScore() {
        return ObjectifyService.ofy()
                .load()
                .type(GameScore.class).list();

    }
}
