package org.forsp.game.domain.test;

import org.forsp.game.domain.Point;
import org.forsp.game.domain.Word;
import org.forsp.game.exceptions.PuzzleException;
import org.forsp.game.exceptions.WordNotFoundException;
import org.forsp.game.service.WordPuzzle;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii Kryvtsov
 * @since 05/05/2015
 */

public class WordPuzzleTest {

    public static final String PUZZLE_CONTETN = "VNYBKGSRORANGEETRNXWPLAEALKAPMHNWMRPOCAXBGATNOMELd";

    @Test
    public void positiveTest() throws PuzzleException {
        List<String> words = new ArrayList<>();
        words.add("apple");
        words.add("banana");
        words.add("cherry");
        words.add("GRAPES");
        words.add("LEMON");
        words.add("ORANGE");
        words.add("tomato");
        WordPuzzle puzzle = new WordPuzzle(PUZZLE_CONTETN, words);
        for (String word : words) {
            Word result = puzzle.searchWord(word);
            assertValidResult(puzzle, result, word);
        }
    }

    private void assertValidResult(WordPuzzle puzzle, Word result, String word) throws PuzzleException {
        assertWord(result, word);
        Point[] points = result.getPoints().toArray(new Point[result.getPoints().size()]);
        Word testResult = puzzle.getWord(points[0], points[points.length - 1]);
        assertWord(testResult, word);
    }

    private void assertWord(Word result, String word) {
        Assert.assertNotNull("Existing word not found", result);
        Assert.assertEquals(result.getPoints().size(), word.length());
    }

    @Test(expected = WordNotFoundException.class)
    public void nonExistingWordTest() throws PuzzleException {
        List<String> words = new ArrayList<>();
        words.add("CARROT");
        WordPuzzle puzzle = new WordPuzzle(PUZZLE_CONTETN, words);
        puzzle.validateGame();
    }

    @Test(expected = PuzzleException.class)
    public void invalidPuzzleSequenceTest() throws PuzzleException {
        List<String> words = new ArrayList<>();
        words.add("puz");
        new WordPuzzle("p", words);
    }


    @Test(expected = PuzzleException.class)
    public void invalidPuzzleLengthTest() throws PuzzleException {
        List<String> words = new ArrayList<>();
        words.add("cc");
        new WordPuzzle("cc", words);
    }
}
