## Names  
Pierce Forte (phf7)  
Mary Jiang (mvj6)

## Initial Discussion  
The two primary design issues we currently have are duplication in the backend and the reference to 
frontend features in places that should not be worried about the GUI. We would like to focus our work 
during this lab to solving some of these issues, perhaps by using interfaces where possible and defining 
clearer packages. Another potential area of focus is our implementation of the Grid; as Professor Duvall 
suggested, we would like to pass around a grid object as opposed to a 2D array list.  

We added methods to our GridModel class (get number of rows and columns, getting a cell at a particular position) and
identified places in our code to use this to represent our cells instead of a 2D array list. 

From the design checking tool, we identified places with magic values and plan to change them as we improve our design.


