package org.forsp.game.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.ObjectifyService;
import org.apache.commons.lang3.StringUtils;
import org.forsp.game.exceptions.PuzzleException;
import org.forsp.game.service.*;
import org.forsp.game.storage.entity.GameScore;
import org.forsp.game.storage.entity.Puzzle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Word puzzle game API.
 * <p>add game<p/>
 * <p>update game<p/>
 * <p>remove game<p/>
 * <p>check word<p/>
 */
@Api(name = "puzzle", version = "v1", description = "Word puzzle game API")
public class WordPuzzleGameApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordPuzzleGameApi.class);

    @ApiMethod(name = "leaders")
    public Collection<GameScore> getLeaderBoard() {
        return getScore();

    }

    /**
     * Removes puzzle from storage by given id.
     *
     * @param id game id
     * @throws PuzzleException if given id is null
     */
    @ApiMethod(name = "delete")
    public void deleteGame(@Named("id") Long id) throws PuzzleException {
        validateRequest(id);
        //Delete async
        ObjectifyService.ofy().delete().type(Puzzle.class).id(id);
    }

    private Puzzle getPuzzle(Long id) throws PuzzleException {
        if (id == null) {
            throw new PuzzleException("Invalid puzzle id");
        }
        Puzzle puzzle = ObjectifyService.ofy().load().type(Puzzle.class).id(id).now();

        if (puzzle == null) {
            throw new PuzzleException("Puzzle not found");
        }
        return puzzle;
    }


    private Board getPuzzle(Puzzle puzzle) throws PuzzleException {
        WordPuzzle game = new WordPuzzle(puzzle.getContent(), puzzle.getWords());
        return game.getBoard();
    }


    private WordPuzzle getGame(Long id) throws PuzzleException {
        Puzzle puzzle = getPuzzle(id);
        return new WordPuzzle(puzzle.getContent(), puzzle.getWords());
    }

    /**
     * Saves game to storage.
     *
     * @param game puzzle game object
     * @throws PuzzleException if game is invalid
     */
    @ApiMethod(name = "save")
    public void saveGame(Puzzle game) throws PuzzleException {
        validateRequest(game);

        if (StringUtils.isBlank(game.getContent())) {
            throw new PuzzleException("Char sequence should not be empty");
        }
        if (StringUtils.isBlank(game.getName())) {
            throw new PuzzleException("Name is required");
        }
        if (game.getWords() == null || game.getWords().isEmpty()) {
            throw new PuzzleException("At least one word required");
        }
        game.setContent(game.getContent().toUpperCase());
        Set<String> words = new HashSet<>();
        for (String word : game.getWords()) {
            words.add(word.toUpperCase());
        }
        testGame(game);
        //Async add
        ObjectifyService.ofy().save().entity(game);
    }

    private void testGame(Puzzle game) throws PuzzleException {
        WordPuzzle puzzle = new WordPuzzle(game.getContent(), game.getWords());
        List<String> errorWords = new ArrayList<>(game.getWords().size());
        for (String word : game.getWords()) {
            if (StringUtils.isBlank(StringUtils.trimToEmpty(word))) {
                throw new PuzzleException("Empty words not allowed");
            }
            Word result = puzzle.searchWord(word);
            if (result == null) {
                errorWords.add(word);
            }
        }
        if (!errorWords.isEmpty()) {
            throw new PuzzleException(String.format("Words not found in puzzle: %s", Arrays.toString(errorWords.toArray())));
        }
    }

    private void validateRequest(Object game) throws PuzzleException {
        if (game == null) {
            throw new PuzzleException("Invalid request");
        }
    }

    /**
     * Returns all available puzzle games.
     *
     * @return collection of word puzzle games
     */
    @ApiMethod(name = "getAllGames")
    public Collection<GameDto> getAllGames() {
        LOGGER.debug("Loading all games...");
        Collection<GameDto> games = new ArrayList<>();
        Collection<Puzzle> puzzle = ObjectifyService.ofy()
                .load()
                .type(Puzzle.class).list();
        for (Puzzle p : puzzle) {
            try {
                GameDto dto = new GameDto();
                dto.setId(p.getId());
                dto.setBoard(getPuzzle(p).getBoard());
                dto.setName(p.getName());
                dto.setWords(p.getWords());
                games.add(dto);
            } catch (PuzzleException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return games;
    }

    /**
     * Checks if given combination of points is valid word
     *
     * @param word
     * @return word coordinates
     * @throws PuzzleException
     */
    @ApiMethod(name = "checkWord")
    public Word checkWord(Word word) throws PuzzleException {
        validateRequest(word);
        validateRequest(word.getGameId());
        if (word.getPoints() == null || word.getPoints().size() != 2) {
            throw new PuzzleException("Expected two points");
        }
        WordPuzzle puzzle = getGame(word.getGameId());
        Iterator<Point> it = word.getPoints().iterator();
        Word result = puzzle.getWord(it.next(), it.next());
        if (result == null) {
            throw new PuzzleException("Word not found");
        }
        return result;
    }

    private Collection<GameScore> getScore() {
        return ObjectifyService.ofy()
                .load()
                .type(GameScore.class).list();

    }
}
