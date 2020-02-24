package cellsociety.grid;

public class GridController {
    private GridModel model;
    private GridView view;

    public GridController(GridModel model, GridView view){
        this.model = model;
        this.view = view;
    }

    public void updateView(){
        view.update(model.getCells());
    }

}
