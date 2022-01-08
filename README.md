# squaremap-Towny
A Towny and squaremap add-on Spigot plugin that enables towns to show up on the squaremap.

## Dependencies
This plugin requires you to have the following plugins on your server:
- [squaremap](https://github.com/jpenilla/squaremap/)
- [Towny](https://github.com/TownyAdvanced/Towny) 0.97.1 (or later)

## Features
Honestly, nothing uber special about this plugin other than it works, but if you really want to know:
- Pretty Configurable
- Async Processing (may or may not be because the algorithms are a little computationally heavy *shhh*)
- Unit-tested Custom Polygon Outline (contour) Algorithm and Negative Space (contour "hole-finding") Algorithm

## Installing
You can install the plugin from the [releases page](https://github.com/silverwolfg11/Pl3xMap-Towny/releases). Simply put it into your `plugins` directory and start your server.

## Usage and Configuration
The plugin should be ready for use out of the box. The one thing that may need to be adjusted is the `enabled-worlds` property in the `config.yml` to add the world names that you want town claims to show up on. For more information about the plugin's commands and configuring the plugin, see the [wiki](https://github.com/silverwolfg11/Pl3xMap-Towny/wiki).

## Plugin API:
See [this wiki page](https://github.com/silverwolfg11/Pl3xMap-Towny/wiki/Pl3xMap-Towny-API) for more info.

## Building
This plugin is a simple maven project, and thus can be built via `mvn clean install` (or `mvn clean package` whatever you prefer).

## Licensing
This plugin is licensed under the MIT license. While highly permissible, I do kindly hope that you create pull-requests for any bug fixes or useful features so they can help the entire community.
