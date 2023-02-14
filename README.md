GitHub repo: https://github.com/LifeAdmin-cmd/Scrolls/tree/SDE-resit

# SDE

## Cooperation with team-mate

Since I was already working on this project detached form the SDE course I asked to work further and improve on this project instead of starting a new project with someone from the SDE course. The Project is hosted on GitHub without the section "SDE" so it doesn't confuse third parties, but I will include it into the ZIP here.

![Proof](https://i.ibb.co/n1PhRMx/Screenshot-from-2023-01-20-15-59-09.png)

## Creational design patterns

The factory pattern is a creational design pattern that provides an interface for creating objects in a super class, but allows subclasses to alter the type of objects that will be created. It is a way to separate the logic of creating an object from the object itself.

### ScrollFactory

In the project I refactored the scroll creating process of the "ScrollCommands" class into a new class called "ScrollFactory". The newly added class "/factories/ScrollFactory" has one method called "spawnTeleportScroll" that accepts a parameter "level", which determines the returned "ItemStack". This method is responsible for calling the requiered "ScrollLevelXFactory"'s "createScroll" method. 

There are three different factory classes for each of the three available levels. Each of these classes implement a "createScroll" method, that returns a single "ItemStack" and is only responsible for creating a single object.

Since every level of a scroll has it's own creation method inside it's factroy class, the code became more maintainable and allows to easly understand and edit only a single level of a scroll.

## Structural Design Pattern

### Facades

#### InventoryFacade

The Facade Design pattern provides a simplified interface to a complex system of classes, it is used to hide the complexity of the system and provide a simple interface to the client.

In the code, the logic for upgrading the scroll was scattered all across the "InventoryEvents" class, and it was difficult to understand it and make changes. To solve this problem, I created a new class called "/events/UpgradeScrollFacade" which contains the logic for upgrading the scroll. This class provides a simplified interface for upgrading the scroll, hiding the complexity of the system from the client.

The "InventoryEvents" class uses the "UpgradeScrollFacade" class's method to upgrade the scroll whenever a user clicks on the upgrade button in the inventory. This way, the "InventoryEvents" class does not need to know the details of how the scroll is upgraded, it simply calls the upgradeScroll method provided by the "UpgradeScrollFacade" class.

#### ExperienceFacade

The Experience class provides a set of complex methods for calculating a player's experience in a game, such as getExp() or getIntLevelFromExp(). These methods can be difficult for the client to use, especially if they are not familiar with the underlying mathematics.

To solve this problem, I implemented the ExperienceFacade class, which acts as a facade for the Experience class. The ExperienceFacade class provides a simplified interface to the client, and it only exposes the methods that are necessary for the client to use. This makes the code more maintainable and easier to understand.

The ExperienceFacade class does not have an instance of Experience but it's still able to access the methods of Experience via class name. The client interacts with the ExperienceFacade class instead of the Experience class, and the ExperienceFacade class in turn calls the appropriate methods of the Experience class. This provides a simpler, more user-friendly interface to the client, while still allowing access to the underlying functionality provided by the Experience class.

### Adapter

The Adapter Design pattern is a structural pattern that allows incompatible classes to work together by converting the interface of one class into an interface that the other class understands. It acts as a bridge between two incompatible interfaces, enabling communication between them.

In the code, the logic for changing the world_name property of the Bukkit instance was not usable for being printed out since it uses "_" between instead of spaces. Inside the "ScrollEvents" and "Teleport Command" the name needs to be printed out however, for the user to actually being able to read it properly. Therefore, I refactored the responsible method into a new class called "/events/WorldNameAdapter".

## Behavioral design patterns

### Command Pattern

The Command pattern is a behavioral design pattern that encapsulates a request as an object, which allows for decoupling the command from the object that executes it. This allows for greater flexibility in the program, as the command can be passed around, stored, and executed at a later time.

In the "ScrollEvents" class, the "startTeleportEvent" method was responsible for handling the "PlayerToggleSneakEvent", checking the player's inventory for a valid scroll, validating that the scroll has a destination set, checking the player's location and distance from the destination, and finally, teleporting the player to the destination.

I refactored the "startTeleportEvent" method to use the Command pattern by creating a new class called "/events/TeleportCommand" which takes an "ItemStack" and a "Player" as parameters. The "TeleportCommand" class is responsible for encapsulating all the logic related to teleporting the player and validating the scroll, which was previously done in the startTeleportEvent method.

The "startTeleportEvent" method now creates a new instance of the "TeleportCommand" class, passing the item and player as arguments, and then it calls the "isValid()" and "execute()" methods on the "TeleportCommand" instance.

- The "isValidScroll()" method checks if the item in the main hand is a scroll.
- The "hasValidCords()" method checks if the scroll has a valid destination
- The "execute()" method contains all the logic related to teleporting the player and validating the scroll, and it also returns a boolean.

By encapsulating the logic related to teleporting the player in a separate class, the "startTeleportEvent" method is now simpler, more readable, and easier to maintain. Additionally, the "TeleportCommand" class can be reused in other parts of the codebase, or even in other projects, making the code more reusable and flexible.

### State Pattern

The State pattern is a behavioral design pattern that allows an object to alter its behavior when its internal state changes. It is often used to encapsulate the behavior of an object that changes based on its current state.

In the refactored code, the State interface defines a single method execute(), that must be implemented by any class that implements it. Two classes EnableState and DisableState were implemented which have the responsibility to handle the behavior of the plugin when the plugin is enabled or disabled. The EnableState class is called when the plugin is enabled, it retrieves the data from the file, and DisableState class is called when the plugin is disabled, it saves the data to the file. This allows for a clear separation of concerns and makes the code more modular and easy to understand.

In the Scrolls class, the current state of the plugin is stored in a private variable state, and the onEnable() and onDisable() methods assign the appropriate state (EnableState or DisableState) to this variable and call the execute method of the current state. This allows the plugin to change its behavior depending on whether it is being enabled or disabled, without the Scrolls class having to know the details of how this behavior is implemented. Also, it was possible to refactor the methods responsible for saving and retrieving the data from the file into the dependent state classes.

The State pattern used here makes the code more manageable, easier to understand, and easy to extend in the future. It also provides a way to encapsulate the behavior of the plugin, which makes the code more flexible and reusable.

# Scrolls

Scrolls is a Minecraft Spigot plugin which enables players to teleport to previously saved locations using magical scrolls.

## Features

- Customizable balancing
- In-game craftable and upgradeable
- Teleport Animations & Sound effects


## Screenshots

![Plugin Screenshot](https://i.ibb.co/hHSFwXg/2023-01-20-14-50-42.png)

## Documentation

For configuration, please see the config file and read the comments, it is pretty self-explanatory.

### Crafting

Crafting is (currently) not customizable but the crafting recipe for a Scroll Of Teleportation is this:

![Crafting Recipe](https://i.ibb.co/tmk3g96/crafting-grid.png)

### Upgrading
By performing a right-click while holding a scroll the upgrade menu opens, and it is possible to upgrade your scrolls. There are three Levels of scrolls that can be upgraded by paying with XP. The amount of XP required for a upgrade is customizable.

![Upgrade Menu](https://i.ibb.co/6yB4BRk/2023-01-20-15-20-20.png)

### Usage of the scroll

To set a destination of a Scroll Of Teleportation you have to right-click a block holding the scroll in your main hand. This will save the coordinates to the scroll. You can see the saved cords while hovering above the item in your Inventory.

To use the scroll you'll have to sneak while holding a scroll in your main hand. This will start the Animation and destroy the scroll.

### Commands

The Plugin offers basic commands that should be reserved for Admins only.

* /scrolls help -> Prints a command list
* /scrolls tp <levelOfScroll> <targetPlayer> -> Gives a Scroll Of Teleportation to the target
* /scrolls resetCd <targetPlayer / all> -> Resets cooldowns of target or all cooldowns
## Installation

Just put it into the plugins folder of your server, which will then create a new directory containing the config you can customize and a cooldowns file that you should not edit manually.

* Native: **Spigot 1.19.2**

## Author

- [@LifeAdmin-cmd](https://www.github.com/lifeadmin-cmd)


## License

[GNU GPLv3](https://choosealicense.com/licenses/gpl-3.0/#)

