package cellsociety.simulation;

import cellsociety.cell.Cell;
import cellsociety.cell.WaTorCell;

import java.util.*;
import java.util.function.Function;

public class WaTorSimModel extends SimModel {
    public static final String EMPTY = "0"; //represented in data file as 0
    public static final String FISH = "1"; //represented in data file as 1
    public static final String SHARK = "2"; //represented in data file as 1
    public static final String CONFIG_FILE_PREFIX = "WaTor";


    private Map<String, Function<List<Cell>, String>> handleCell = Map.of(
            EMPTY, (neighbors) -> handleEmptyCell(neighbors),
            FISH, (neighbors) -> handleFishCell(neighbors),
            SHARK, (neighbors) -> handleSharkCell(neighbors));

    private List<List<String>> nextStates = new ArrayList<>();

    public WaTorSimModel(List<List<WaTorCell>> cells, SimController simController) {
        super(cells, simController);
    }

    @Override
    protected String determineNextState(Cell cell, List<Cell> neighbors) {
        int rows = getCells().size();
        int cols = getCells().get(0).size();
        for (int row = 0; row < rows; row++) {
            nextStates.add(new ArrayList<>());
            for (int col = 0; col < cols; col++) {
                nextStates.get(row).add(EMPTY);
            }
        }
        int numNeighbors = getNumNeighbors(neighbors);
        String curCellState = cell.getState();
        //return handleCell.get(curCellState).apply(numNeighbors);
        return null;
    }

    @Override
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
    }

    // TODO: properly implement the methods below
    private String handleEmptyCell(List<Cell> neighbors) {

        return "";
    }

    private String handleFishCell(List<Cell> neighbors) {
        List<List<Integer>> potentialDirections = new ArrayList<>();


        return "";
    }

    private String handleSharkCell(List<Cell> neighbors) {

        return "";
    }

    private int getNumNeighbors(List<Cell> neighbors) {
        int numNeighbors = 0;
        for (Cell neighbor : neighbors) {
            if (isCellAlive(neighbor)) numNeighbors++;
        }
        return numNeighbors;
    }

    private boolean isCellAlive(Cell cell) {
        return false;
        //return cell.getState().equals(ALIVE);
    }

    private int chooseRandomDirection(List<Integer> directions) {
        Collections.shuffle(directions);
        return directions.get(0);
    }
}

/*
BASIC RULES:
Fish
- At each chronon, a fish moves randomly to one of the adjacent unoccupied squares.
If there are no free squares, no movement takes place.
- Once a fish has survived a certain number of chronons it may reproduce.
This is done as it moves to a neighbouring square, leaving behind a new fish in its old position.
Its reproduction time is also reset to zero.

Sharks
- At each chronon, a shark moves randomly to an adjacent square occupied by a fish.
If there is none, the shark moves to a random adjacent unoccupied square. If there are no free squares, no movement takes place.
- At each chronon, each shark is deprived of a unit of energy.
- Upon reaching zero energy, a shark dies.
- If a shark moves to a square occupied by a fish, it eats the fish and earns a certain amount of energy.
- Once a shark has survived a certain number of chronons it may reproduce in exactly the same way as the fish.


CONFLICTS:
- More than one fish or sharks move to the same cell
    -  I think a good resolution here is to keep a hashmap of next state cells. In the determine next state function,
       fish and sharks will check if the cell they are trying to move to has already been "chosen" by another creature. If
       this is the case, then they will randomly choose another cell to move to for its next state.


 */
