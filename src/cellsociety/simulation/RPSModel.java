package cellsociety.simulation;

import cellsociety.cell.GOL.GOLCell;
import cellsociety.cell.RPS.RPSCell;
import cellsociety.grid.Grid;

import java.util.*;

public class RPSModel extends SimModel<RPSCell>{
    public static final String CONFIG_FILE_PREFIX = "RPS";



    protected int threshold = 3;

    private List<List<RPSCell>> playablehands = new ArrayList<>();

    public RPSModel(List<List<String>> cellStates, SimController simController) {
        super(cellStates, simController);
        playablehands = createGrid(cellStates);
    }

    @Override
    protected Map<String, Class> getCellTypesMap() {
        return Map.of(RPSCell.STATE, RPSCell.class);
    }

    @Override
    protected void setNextStates(Grid<RPSCell> grid) {

    }

    @Override
    protected void updateStates(Grid<RPSCell> grid) {

    }

    //@Override
    protected List<List<RPSCell>> createGrid(List<List<String>> cellStates) {
        List<List<RPSCell>> rpsgrid = new ArrayList<>();
        for (int row = 0; row < cellStates.size(); row++) {
            rpsgrid.add(new ArrayList<>());
            for (int col = 0; col < cellStates.size(); col++) {
                RPSCell cell;
                cell = new RPSCell(cellStates.get(row).get(col), row, col);
                rpsgrid.get(row).add(cell);
            }
        }
        return rpsgrid;
    }

    //@Override
    protected void setNextStates(List<List<RPSCell>> cells) {
        for(List<RPSCell> row : cells){
            for (RPSCell cell : row){
                determineAndSetNextState(cell, getNeighbors(cell));
            }
        }

    }

    //@Override
    protected void determineAndSetNextState(RPSCell cell, List<RPSCell> neighbors) {
        Map<String,Integer> getstatemap = new HashMap<>();
        for(RPSCell neighborcell :neighbors){
            getstatemap.putIfAbsent(neighborcell.getState(),0);
            getstatemap.put(neighborcell.getState(),getstatemap.get(neighborcell.getState())+1);
        }

        List<Integer> values = new ArrayList<>();
        Collections.addAll(getstatemap.values());
        Collections.sort(values);
        Collections.reverse(values);
        String chosenkey=cell.getState();
        int keychecker = 0;


        for (int nval:values){
            for(String key : getstatemap.keySet() ){
                if(nval==getstatemap.get(key)){
                    chosenkey = key;
                    keychecker+=1;
                }
            }
            if(nval < threshold){
                if(cell.getState().hashCode() < chosenkey.hashCode()) {
                    cell.setNextState(chosenkey);
                }
            }
        }
        if(keychecker==0){
            cell.setNextState(chosenkey);
        }
    }

   // @Override
    protected void updateStates(List<List<RPSCell>> cells) {
        for (List<RPSCell> row : cells) {
            for (RPSCell cell : row) {
                cell.updateState();
            }
        }

    }

    @Override
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
    }

    @Override
    protected List<RPSCell> getNeighbors(RPSCell cell) {
        return getGrid().getAllNeighbors(cell);
    }


}

