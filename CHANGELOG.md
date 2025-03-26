# Changelog

## v1.1.0

### New Features

- Added abstract ParticleEmitter class for components that emit particles 
- Added ParticleFlowEmitter component that continuously emits particles
- Added ParticleFlowEmitterPrefab 
- Added tooltips for canvas elements
- Added StateMachine class and State enum for creating finite-state machines (FSM)
- Added ApplicationManager class to manage application state and config externally

## Changes


- Renamed Timer class to ApplicationTimer and refactored
- Added ability to toggle time scaling for update systems
- Added ability to ignore certain systems while the application is paused

### Fixes
