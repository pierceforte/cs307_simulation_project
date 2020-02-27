# Simulation Design Plan
### Team Number 
9
### Names
Suomo Ammah (sna19)  
Pierce Forte (phf7)  
Donald Groh (dkg16)  
Mary Jiang (mvj6)   


## Design Overview
Describe the packages and classes you intend to create, without revealing any specific implementation details (like data structures, file formats, etc.). Using CRC cards can be a very useful way to develop your design.  

- MainController  
	- Main class that has the main() method that allows user to choose a simulation to run
	- Contains the step() function to run the selected simulation in the scene
- ConfigReader  
	- Reads in the configuration file to create a grid
- SimulationController
    - Connects SimulationModel and SimulationView
    - Contains controls for starting/stopping a simulation and updating the model and view
- SimulationModel  
	- Abstract class that sets up the basic framework for each simulation
	- Checks and updates cells according to the rules for that simulation.
    - Has subclasses that must override methods for updating cells, creating the grid, determining simulation state (like end of simulation), etc.
- SimulationView
	- Updates simulation grid and contains methods to instantiate buttons for simulation controls
- Grid
    - Keeps track of all cells in a grid and can determine neighbours of a cell
- Cell
    - Sets and gets state information and knows about row and column position for the cell
- CellView
    - Inherits a shape class to render individual cells (may become an abstract class to accomodate cells of different shapes)
    
   
Here are some initial CRC cards from the beginning of our design discussion, although our design has evolved slightly since creating them:
![CRC]("designImages/interfaceSketch.jpg")


## Design Details

Apply the rules to a cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all of its neighbors)


1. Grid determines Cell neighbours

2. For each neighbouring Cell, see if it is occupied or not

3. SimulationModel determines if Cell needs to die or live based on number of occupied Cell objects

4. SimulationModel tells Cell it needs to update its "next" state



Move to the next generation: update all cells in a simulation from their current state to their next state  

1. The grid will then iterate through each cell

2. The grid will then tell each cell to set the "current" state equal to the "next" state


Switch simulations: load a new simulation from a data file, replacing the current running simulation with the newly loaded one

1. 	ConfigurationReader classes parses data file

2. MainController creates Simulations 

## Design Considerations
Describe at least two design issues your team encountered (even if you have not yet resolved it). Address any alternatives the team considered including pros and cons from all sides of the discussion.    

-   **Issue 1: Abstraction in Simulation/Grid**
	- One issue that we are still trying to resolve is whether the Simulation class or the GridModel class will be abstract. Our idea here is that one should and one should not; if the Simulation is abstract (meaning its different implementations define different rules), the GridModel could have the same implementation regardless of the simulation (meaning the simulation would simply tell it what to do). The flip side is that the Simulation class is the same for all types of simulations – simply running the steps and asking the GridModel to update – and the GridModel would be abstract and follow different rules on each update.
    
- **Issue 2: Grid Structure**
	- Another issue we were concerned about is how to represent the grid in the backend vs. the frontend. For example, if the grid is made up of hexagons instead of squares on the frontend, can we still represent it with a basic 2D array on the backend? Our goal is to worry about the structure and appearance of the grid entirely on the frontend and simply use a 2D grid on the backend for all simulations (that follows different rules but with the same structure), but we still have some worries about the implementation of this design.

## User Interface
![UI_sketch]("designImages/interfaceSketch.jpg")

- Buttons in simulation view:
	- Exit simulation
	- Speed up simulation
    - Slow down simulation
    - Pause simulation
    - Return to simulation choice menu
    - Change color (if time allows)

## Team Responsibilities
List the parts of the project each team member plans to take primary and secondary responsibility for and a high-level plan of how the team will complete the program.  

- Suomo Ammah (sna19)  
- Pierce Forte (phf7)  
- Donald Groh (dkg16)  
- Mary Jiang (mvj6) 