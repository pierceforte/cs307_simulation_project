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
- ConfigReader  
	- Reads in the configuration file to create a grid
- Simulation  
	- Abstract class that sets up the basic framework for each simulation (has things like a step function and a create grid function)
    - Has subclasses that must override methods for updating cells, creating the grid, determining simulation state (like end of simulation), etc.
- GridModel
	- Abstract class that keeps track of all cells in the grid. Checks and updates cells according to the rules for that simulation.
    
- GridView
	- Renders Shape objects based on the type of the GridModel
- CellModel  
- CellView




## Design Details

Describe how your packages and classes work together to handle specific features (like collaborating with other classes, using resource files, or serving as an abstraction that could be extended). Using Use Cases can be a very useful way to help make your descriptions more concrete.  

[Use Cases](http://agilemodeling.com/artifacts/systemUseCase.htm)

Apply the rules to a cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all of its neighbors)



1. Grid determines Cell neighbours

2. For each neighbouring Cell, see if it is occupied or not

3. Grid determines if Cell needs to die or live based on number of occupied Cell objects

4. Grid tells Cell it needs to update its "next" state








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
Show the overall appearance of program's user interface components and how users interact with these components.  

- Buttons
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