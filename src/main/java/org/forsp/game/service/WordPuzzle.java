package org.forsp.game.service;

import org.apache.commons.lang3.StringUtils;
import org.forsp.game.domain.Board;
import org.forsp.game.domain.Point;
import org.forsp.game.domain.Word;
import org.forsp.game.exceptions.PuzzleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author Serhii Kryvtsov
 * @since 04/05/2015.
 */
public class WordPuzzle {

    private final static Logger LOGGER = LoggerFactory.getLogger(WordPuzzle.class.getName());
    private static final Point[] SEARCH_DIRECTIONS = {
            new Point(0, 1), new Point(0, -1), new Point(1, 0),
            new Point(1, 1), new Point(1, -1), new Point(-1, 0),
            new Point(-1, 1), new Point(-1, -1)
    };
    private Board board;
    private int dim;
    private Set<String> words;
    private Map<Character, Collection<Point>> charMap;


    public WordPuzzle(String puzzle, Collection<String> puzzleWords) throws PuzzleException {
        if (puzzle == null) {
            throw new PuzzleException("Invalid char sequence");
        }
        if (puzzleWords == null || puzzleWords.isEmpty()) {
            throw new PuzzleException("Expected at least one word");
        }
        int size = (int) Math.sqrt(puzzle.length());
        if ((size * size) < puzzle.length()) {
            LOGGER.warn("Invalid char sequence, board will be cutted");
        }
        dim = size;

        if (dim < 2) {
            throw new PuzzleException("Minimum board size is 2");
        }
        int length = puzzle.length();
        int cells = dim * dim;
        if (length < cells) {
            throw new PuzzleException(String.format("Wrong char sequence, expected: %s chars but found: %s", cells, length));
        }
        if (length > cells) {
            LOGGER.warn("Char sequence is bigger than board size (expected: {}, actual: {}), extra charters will be ignored", cells, length);
        }
        board = build(puzzle, dim);
        LOGGER.info(String.format("Board %sx%s \n%s", dim, dim, board));
        fillCharMap();
        words = new HashSet<>(puzzleWords.size());
        for (String word : puzzleWords) {
            words.add(word.toUpperCase());
        }
    }

    private static Board build(String words, int dim) {
        char[][] board = new char[dim][dim];
        char[] chars = words.toCharArray();
        for (int i = 0; i < dim; i++) {
            System.arraycopy(chars, i * dim, board[i], 0, dim);
        }
        return new Board(board);
    }

    private int getDirection(int x1, int x2) {
        int result = x2 - x1;
        if (result == 0) {
            return 0;
        } else if (result > 0) {
            return 1;
        }
        return -1;
    }

    public Board getBoard() {
        return board;
    }

    public Word getWord(Point p1, Point p2) throws PuzzleException {
        if (p1 == null || p2 == null) {
            throw new PuzzleException("Invalid points");
        }
        char[][] puzzleBoard = board.getBoard();
        StringBuilder sb = new StringBuilder();
        Collection<Point> points = new ArrayList<>();
        int xDirection = getDirection(p1.getX(), p2.getX());
        int yDirection = getDirection(p1.getY(), p2.getY());
        int destX = p2.getX() + xDirection;
        int destY = p2.getY() + yDirection;

        LOGGER.info(p1.getX() + " = " + destX + ", " + p1.getY() + " = " + destY);
        for (int x = p1.getX(), y = p1.getY(); x != destX || y != destY; x = x + xDirection, y = y + yDirection) {
            sb.append(puzzleBoard[x][y]);
            points.add(new Point(x, y));
        }

        LOGGER.info("Word: " + sb.toString().toUpperCase());
        if (!words.contains(sb.toString().toUpperCase()) && !words.contains(sb.reverse().toString().toUpperCase())) {
            return null;
        }
        Word word = new Word();
        word.setPoints(points);
        word.setWord(sb.toString());
        return word;
    }

    private Collection<Point> searchWord(String word, Point startPos, Point direction) {
        int wordLength = word.length();
        char[] chars = StringUtils.trimToEmpty(word).toUpperCase().toCharArray();
        char[][] puzzleBoard = board.getBoard();
        int x = startPos.getX();
        int y = startPos.getY();
        Collection<Point> points = new ArrayList<>(wordLength);
        for (int i = 0; i < wordLength; i++) {
            if (!isValid(x, y)) {
                return null;
            }
            char c = chars[i];
            char found = puzzleBoard[x][y];
            if (c != found) {
                return null;
            }
            points.add(new Point(x, y));
            x = x + direction.getX();
            y = y + direction.getY();
        }
        return points;
    }

    private void fillCharMap() {
        charMap = new HashMap<>();
        char[][] puzzleBoard = board.getBoard();
        for (int x = 0; x < puzzleBoard.length; x++) {
            for (int y = 0; y < puzzleBoard.length; y++) {
                char c = puzzleBoard[x][y];
                Collection<Point> chars = charMap.get(c);
                if (chars == null) {
                    chars = new ArrayList<>();
                    charMap.put(c, chars);
                }
                chars.add(new Point(x, y));
            }
        }
    }

    public Word searchWord(String word) {
        String searchWord = word.toUpperCase();
        long start = System.currentTimeMillis();
        LOGGER.info("Searching word: " + searchWord);
        char[] chars = searchWord.toCharArray();

        if (chars.length > dim) {
            LOGGER.info("Word bigger than board size");
            return null;
        }
        Collection<Point> points = charMap.get(chars[0]);
        if (points == null || points.isEmpty()) {
            LOGGER.info(String.format("There is no char: %s on board", chars[0]));
            return null;
        }
        for (Point startPos : points) {
            for (Point searchDirection : SEARCH_DIRECTIONS) {
                Collection<Point> resultPoints = searchWord(searchWord, startPos, searchDirection);
                if (resultPoints != null && !resultPoints.isEmpty()) {
                    LOGGER.info(String.format("Word found, Search time: %s s.", (System.currentTimeMillis() - start) / 1000f));
                    Word result = new Word();
                    result.setWord(searchWord);
                    result.setPoints(resultPoints);
                    return result;
                }
            }
        }
        LOGGER.info(String.format("Word %s not found", word));
        return null;
    }

    private boolean isValid(int x, int y) {
        return !(x < 0 || y < 0 || x > dim - 1 || y > dim - 1);
    }
}