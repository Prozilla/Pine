# Changelog

## v1.1.1

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
