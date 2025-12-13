# Changelog

## v3.0.1

### Changes

- Added utility method `Application.defer()`, which executes a function in the next frame

### Fixes

- Fixed support for macOS
  - Fixed viewport not being sized correctly in fullscreen windows on retina displays
  - Disabled texture arrays on macOS
- Fixed timeouts causing concurrent modification errors

### Examples

- Added the JVM arg `-XstartOnFirstThread` to all example projects on macOS

## v3.0.0

From this version on, experimental features of Pine will be annotated with `@Experimental`. Minor versions might contain breaking changes for these experimental features.

### New Features

- Added DeferredList, which supports mutations during iterations
- Completely rewrote all properties
  - Each type of property has its own interface (most of them are functional interfaces), to support different implementations
  - Properties with primitive values for integers, floats and booleans
  - Special properties for certain objects like strings, colors, vectors, etc.
  - Properties can be derived from other properties
  - All properties are functors
- New input bindings system using properties
- Added a JSON deserializer using properties with support for hot-reloading
- Added basic 2D shape renderers
- Added 2D shape modifiers
- Added basic 2D colliders
- Added application modes
  - New headless mode which can be used for testing

### Changes

- Added texture wrap and filter options
- Added configurable default settings for loading textures
- Added utility methods that unbox boxed values with support for null values
- Added more node-related events
- Added support for drawing a single triangle or an array of triangles instead of a quad
- Refactored configuration system using properties
- Added `getKeyRepeated()` which also allows repeated key presses and made `getKeyDown()` ignore repeated key presses
- Added support for fallback values when fetching storage items
- Added properties based on storage items
- Made all standard systems final
- Made applications load scenes based on references instead of arbitrary ID's

### Fixes

- Fixed input node causing errors when removing text to the left of the cursor
- Fixed dev console failing to open after loading a scene for the second time
- Made initialization system also process the children of an activated entity
- Fixed null values causing errors when checking for input
- Fixed scenes causing errors when reloaded by a system

### Documentation

- Added custom `@Experimental` annotation

### Build Tool

- Added the option to exclude the JRE from the build to reduce build size
- Added the option to create a Windows installer for builds using NSIS

### Examples

- Flappy Bird:
  - Added simple settings and game over menus
  - Added fixed seed
  - Implemented colliders for obstacles
  - Implemented deserializer for player data
  - Implemented local storage for storing the player's highscore and seed
  - Implemented new input bindings

## v2.1.0

### New Features

- Custom font rendering using stb_truetype
- Added support for different shader types
- Added timestamps to application logs
- Added EasingParser to parse basic easing functions with
- Added CSS child selector
- Added Platform and Architecture enums that wrap around the corresponding enums from LWJGL
- Added persistent data path
- Added a functional interface to parse strings, which can be used in some places instead of/inside a stateful Parser object
- Added an abstract sequential parser
- Added audio effects that can have randomized properties
- Added a dev console with commands which can be activated in dev mode
- Added ability to toggle between fullscreen mode and windowed mode
- Added ability to lock and hide cursor by changing the cursor mode
- Added LocalStorage and SessionStorage using key-value stores

### Changes

- Added gain setter and state getter to audio sources
- Added more utility methods for destructibles, printables and transmittables
- Added more utility methods related to OpenGL and GLFW
- Updated CSS parsing
  - Added support for `auto`
  - Made parsing case-insensitive
  - Added support for `ms` in animation durations
  - Added support for type selector based on the HTML tags of nodes
- Added methods to convert vectors to directions
- Added method to check if a vector is the zero vector
- Added method to resize a list based on a wrap mode
- Added formatted printing for stylesheets
- Added utility method to get value of nullable property with an optional default value
- Added utility method to generate a list of coordinates representing a subgrid of a grid
- Added utility method to get the closest edge of a tile based on a given position 
- Prefabs can now have children
- Marked most interfaces with a single method as functional interfaces
- Added string representation of keys and mouse buttons

### Fixes

- Made asset pools return `null` when loading fails
- Fixed unnecessary event objects being created
- Fixed vector subtraction
- Fixed multi-tile hovering state
- Fixed resizing of the window and renderer 
- Fixed layout node size calculation
- Fixed cursor causing errors when text is programmatically removed from text input node
- Fixed node root not allowing nested children to be focusable
- Improved support for macOS and other platforms using older OpenGL versions

### Documentation

- Added JetBrains annotations (`@Contract` & `@NotNull`)

### Build Tool

- Added option to automatically create zip of build

### Mods

- Added an example mod that activates dev mode

### Examples

- Flappy Bird:
  - Added highscore
  - Added sound effects
  - Added ground with parallax effect
  - Fixed pipe collision detection
  - Fixed black lines appearing in background
- Sokoban:
  - Added support for multiple players (on the same device)
  - Added sound effects

## v2.0.3

From this version on, all releases will come with a sources and javadoc jar.

### New Features

- Added events for gamepads connecting and disconnecting

### Changes

- Optimized event dispatchers so they only create events when necessary
- Updated the event dispatcher with new utility methods and ephemeral listeners, which only listen for an event once
- Added a superclass for all Pine-related exceptions (PineException)
- Updated hashcode and equals methods of all vector classes
- Updated equals methods of style-related classes
- Classes that would previously resort to printing the stack trace when they did not have access to a logger, will now use the system logger to log exceptions

### Fixes

- Fixed formatting of error messages (mostly related to null-checks) being inconsistent
- Fixed out-of-bounds calculations used for culling

### Documentation

- Added custom CSS to Javadoc pages

### Dependencies

- Updated LWJGL to 3.3.6
- Updated shadow plugin (com.gradleup.shadow) to 8.3.9

## v2.0.2

### New Features

- Added ConditionalProperty class

### Changes

- Added static one() method to each vector class, to create vectors with all values set to 1
- Replaced all remaining parsing functions with stateful parsers and marked outdated methods as deprecated

### Fixes

- Fixed render scale not being reset properly
- Fixed children and components of entities not being destroyed when entity is destroyed
- Fixed renderer not flushing to reflect render region changes
- Fixed asset pools not returning null when failing to load an asset
- Fixed components and entity providers throwing exceptions instead of returning null when the entity is missing

## v2.0.1

### New Features

- Added BooleanUtils utility class
- Added EnumUtils utility class
- Added ability to set window hints, making it possible to have a window with a transparent background

### Changes

- Improved ApplicationBuilder class and added config field
- Improved equals and hashcode methods for various classes
- Made more classes cloneable

### Fixes

- Fixed applications crashing when dev mode property is not set
- Improved argument validation using checks
- Fixed isValid methods of int enums

## v2.0.0

### New Features

- Implemented OpenAL library and added audio
- Added support for gamepad input
- Added text input node for text fields in user interfaces
- Added render region to restrict rendering to a given region on the screen
- Added node borders which can be filled with a color or image
- Added utility class Pine, which can be used to query system/library information
- Added developer mode, which can be used to enable developer tools based a system property
- Added new events system and observables
- Added new types of properties:
  - LazyProperty
  - SystemProperty
  - MutableProperty
  - ObservableProperty
  - SingleSelectionProperty
- Added intervals and timeouts based on JS intervals and timeouts
- Added a rect renderer component for drawing colored rectangles

### Changes

- Replaced the Lifecycle interface with Initializable, InputHandler, Updatable, Renderable and Destructible
- Refactored checks and added fluent interfaces for complex checks
- Refactored asset pools, there are now separate asset pools for each type of asset
- When holding a key down for a while, the key will now repeatedly fire, mimicking keyboard typing behaviour
- Updated documentation
- Disabled instancing of utility classes
- Added more default colors and updated the parsing of colors
- Added colour class, a British version of the Color class

### Fixes

- Improved argument validation using checks

### Examples

- Added chat application with communication via local networks
- Added audio visualizer to demonstrate new audio feature
- Flappy bird:
  - Added a main menu 

### License

- Changed from GNU GPLv3 to GNU LGPLv3

## v1.2.1

### Changes

- Added destroy() method for components, which can be overridden to destroy objects used by the component. The default behaviour for this method is to remove the component from its entity.
- Removed unused lifecycle method start()
- Added the following missing bindings for keyboard keys:
  - CAPS_LOCK
  - SCROLL_LOCK
  - NUM_LOCK
  - BACKSPACE
  - L_SUPER and R_SUPER
  - F(13-25)
  - Numpad keys
  - Cursor control keys (DELETE, INSERT, PAGE_UP, etc.)
  - Misc keys (MENU, PAUSE, PRINT, etc.)
- Added the following missing bindings for mouse buttons:
  - MIDDLE
  - EXTRA_(0-4)

### Fixes

- Fixed entity.getParentWithTag(tag) returning children instead of parents
- Removed usage of unsupported lifecycle methods in systems
- Improved null-checks and general argument validation

## v1.2.0

### New Features

- Added animated properties to create animations with
- Added animation curves that determine how animations progress
- Added transitioned properties that start an animation each time their value is changed
- Added styled properties, stylesheets and CSS parsing to support basic CSS
- Added supplied properties to allow component fields to be dynamically set based on a lambda function

### Changes

- Refactored transmittable objects
- Refactored all UI-related features and changed naming conventions (canvas elements -> nodes), to be closer to HTML and CSS
- Added static methods to each adaptive property class to convert any value or property to an adaptive property
- Replaced static parse methods with stateful parsers to allow for better error handling
- Added support for negative dimensions and floating point precision
- Added border images for UI nodes
- Replaced require methods with Checks class, which checks certain conditions at runtime and throws exceptions if they are not met
- Added isDescendantOf(parent) method that checks whether entities are descendants of a given parent

### Fixes

- Resource pool can now load files with spaces in their paths

### Examples

- Added new Sokoban example game
- Split examples into separate projects

### Build tool

- Made icon optional
- Fixed shadow jar creation process

## v1.1.0

### New Features

- Added abstract ParticleEmitter class for components that emit particles 
- Added ParticleFlowEmitter component that continuously emits particles
- Added ParticleFlowEmitterPrefab 
- Added tooltips for canvas elements
- Added StateMachine class and State enum for creating finite-state machines (FSM)
- Added ApplicationManager class to manage application state and config externally
- Added support for custom cursor images
- Added animated properties and easing functions
- Added texture arrays and added a texture array pool to the resource pool
- Added GLUtils class with OpenGL utilities
- Added MultiTileRenderer component to support sprites that take up multiple tiles in a grid
- Added adaptive properties which can either have a fixed or dynamic value and provides a more efficient alternative for variable properties

### Changes


- Renamed Timer class to ApplicationTimer and refactored
- Added ability to toggle time scaling for update systems
- Added ability to ignore certain systems while the application is paused
- Replaced initialized, isRunning and isPaused booleans in Application class with a state machine
- Made particles affected by time scale and added animated properties for particles
- Added getFirstChild() and getLastChild() methods to Entity class
- Added methods for removing resources from the resource pool and methods for debugging the resource pool

### Fixes

- Children of a pass-through canvas group can now receive mouse input
- Destroyed tiles in a grid are now properly removed from the grid
- Each draw call now only uses one texture (or texture array) instead of a list of textures, to support more than 16-32 textures per scene
