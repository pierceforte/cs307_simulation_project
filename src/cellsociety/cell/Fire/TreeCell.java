package cellsociety.cell.Fire;
import java.util.List;
import java.util.Random;

public class TreeCell extends FireCell{
    public static final String STATE = FireCell.TREE;
    public static final double PROB_CATCH_FIRE = 0.5;

    public TreeCell(int row, int col) {
        super(STATE, row, col);
    }


    @Override
    public void setWhatToDoNext(List<FireCell> neighbors) {
        int burningNeighbours = 0;
        for (FireCell cell : neighbors){
            if (cell.getState().equals(FireCell.BURNING)){
                burningNeighbours++;
            }
        }

        if(rnd.nextDouble() < PROB_CATCH_FIRE | burningNeighbours == 1){
            setNextState(BURNING);
        } else setNextState(TREE);
    }

}
