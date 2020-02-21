###High level design  

Names:  

Pierce Forte (phf7)  
Mary Jiang (mvj6)  
Suomo Ammah(sna19)  
Donald Groh (dkg16)  

*How does a Cell know what rules to apply for its simulation?*  
    - For each cell it needs to know its rule and the cells of a certain area surrounding it. It would know what rules to apply for the simulation by reading the rules.    
    - An alternative to reading in the rules is to have a different types of games that have different types of cells, so instead of reading instructions, the actions of the cells will be only dependent on the location of the other cells and then will be determined based on the type of game and the type of cell for that game.    
*How does a Cell know about its neighbors? How can it update itself without affecting its neighbors update?*   
    - A cell could know about its neighbors by receiving certain nearby cells based on the rules of the game. This area of neighbors could then be passed into the cell’s “determine action” function, which would expect a certain area of neighbors based on its implementation of the “determine action” method.    
    - To avoid affecting its neighbors’ updates, every cell will be asked to call the “determine action” function in an initial pass based on the original state of all cells, and then in the second pass they would actually call their “do action” function.   
*What behaviors does the Grid itself have? How can it update all of the Cells it contains?*  
    - The grid can save the state of all the cells before an update and then call updates on each cell. It can tell each cell about its neighbours.    
*What information about a simulation could be given in a configuration file*?  
    - The configuration file could contain rules for each of the cell objects, the initial orientation of cell objects, how large/long should the simulation be, the shape/color/size of cells.   
*How is the GUI updated after all the cells have been updated?*    
    - The GUI could be updated with another pass after the cells have all moved/updated. With a simple loop through the cells, each cell’s display could be determined based on the object within it and its state.   
