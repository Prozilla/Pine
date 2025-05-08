# Changelog

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

## Fixes

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
