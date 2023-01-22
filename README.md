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

Just put the .jar file into the plugins folder of your server, which will then create a new directory containing the config you can customize and a cooldowns file that you should not edit manually.

You can download the .jar file here from repo or from Spigot's website.

* Native: **Spigot 1.19.2**

## Author

- [@LifeAdmin-cmd](https://www.github.com/lifeadmin-cmd)


## License

[GNU GPLv3](https://choosealicense.com/licenses/gpl-3.0/#)

