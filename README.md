# Editor2D v.2.0.0 (unfinished)

Re-write of the Editor2D level editor (hence the 2.0.0).
Editor2D is a framework designed to function as the core for 2D tile-based level editors. The framework has been
written very modularly so that it can easily be extended any 2D game project. Not only can Editor2D be extended,
it can also be used as-is by simply running the project.
<br />
<br />

## Main class
Application.java

## Editor2D map project structure

### Scenes
Editor2D map projects consist of scenes that consist of instances of assets. Scenes can be thought of as levels
where instances, also known as **Placeables**, of assets are stored in layers designated for each asset type. In 
addition to placeables, scenes may also hold additional metadata that will be passed onto the compiler when 
constructing the final map.

### Assets
Assets are the types of instances that can be placed into a scene. The framework introduces three basic asset 
types: images, objects and data. While all assets can be placed into the scene the placeables of each asset 
have their own properties and ways that they can be interacted with via the editor tools.

**Tiles** are instances that simply render an **image** at their position. Tiles are minimalistic and often 
used in 2D games to draw static and repetitive background imagery, such as landscapes in top-down viewed games.
Tiles typically cannot be interacted with. Tiles can consist of an entire image or be parts of an image so as 
to allow the usage of tilemaps.

**Objects** represent general game objects. While tiles are static, objects are dynamic meaning that they change
their position in scene as the game runs. Objects may utilize **images** as sprites which are the visual 
presentations of the object in the editor. Unlike tiles, objects can be rotated, stretched and positioned free of
the grid of the layer that they are placed on. Additionally, objects have custom properties that can be 
specified and compiled into the final map. An example of an object could be a pickup or an enemy.

**Data objects** are used to mark areas of the map. Data objects are generally invisible, yet provide the game 
engine with information about the area of the map, for example, areas that are considered inaccessible during 
pathfinding.
<br />
<br />

These assets should in and of themselves be enough to create simple, generic maps, however, they can be 
extended due to their highly modular nature.

## Modularity
Each mentioned asset type is placed (in the Java project) inside its own package under the package "modules".
Each module is named after the asset type that it represents. All modules must have the same structure as the 
modules of the three basic asset types. All modules must also be declared inside the ``ModuleDeclarations`` so that 
Editor2D can initialize and load them into the framework as the program is ran. This structure is used so that 
new modules can be added either by extending the three basic ones or by writing completely new modules altogether.
Assets may also be removed by removing their declarations, however, this is not recommended.
