# Changelog

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
