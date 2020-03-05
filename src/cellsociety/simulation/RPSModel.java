package cellsociety.simulation;

import cellsociety.cell.GOL.GOLCell;
import cellsociety.cell.RPSCell;
import cellsociety.cell.Segregation.SegregationCell;

import java.util.*;

public class RPSModel extends SimModel<RPSCell>{
    public static final String CONFIG_FILE_PREFIX = "RPS";


    protected int threshold = 3;

    private List<List<RPSCell>> playablehands = new ArrayList<>();

    public RPSModel(List<List<String>> cellStates, SimController simController) {
        super(cellStates, simController);
     //
    }

    @Override
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

    @Override
    protected void setNextStates(List<List<RPSCell>> cells) {
        for(List<RPSCell> row : cells){
            for (RPSCell cell : row){
                determineAndSetNextState(cell, getNeighbors(cell));
            }
        }

    }

    @Override
    protected void determineAndSetNextState(RPSCell cell, List<RPSCell> neighbors) {
        Map<String,Integer> getstatemap = new TreeMap<>();
        for(RPSCell neighborcell :neighbors){
            getstatemap.putIfAbsent(neighborcell.getState(),0);
            getstatemap.put(neighborcell.getState(),getstatemap.get(neighborcell.getState())+1);
        }
        List<Integer> values = new ArrayList<>();
        for(int val: getstatemap.values() ){
            values.add(val);
        }
        Collections.sort(values);
        Collections.reverse(values);
        String chosenkey="";
        for (int nval:values){
            for(String key : getstatemap.keySet() ){
                if(nval==getstatemap.get(key)){
                    chosenkey = key;
                }
            }
            if(nval>threshold && chosenkey.hashCode()>cell.getState().hashCode()){
                cell.setNextState(chosenkey);
            }
        }
        if(chosenkey.length()==0){
            cell.setNextState(cell.getState());
        }
    }

    @Override
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
            return getGridModel().getAllNeighbors(cell);
    }


}
