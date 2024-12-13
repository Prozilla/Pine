# Pine

Pine stands for **P**rozilla's game eng**ine**.
Pine is a lightweight Java game engine that serves as a framework for [LWJGL](https://www.lwjgl.org/).
Pine uses an [ECS](https://en.wikipedia.org/wiki/Entity_component_system) pattern in Java.

- **Entities** - Unique objects that exist in a world inside a scene.
- **Components** - Entity data is stored in modular components.
- **Systems** - Systems perform logic based on that data to update the entity's state or render to the screen.

As a framework, Pine makes working with the low-level LWJGL library a lot easier and more intuitive, without sacrificing control and flexibility.
Because everything is split into layers of abstraction, you can choose how much of Pine's existing functionality you want to utilize
and where you want insert your own code.

## Examples

These are some example projects made using Pine. You can check out their source code below by clicking on the link or try them out by running the commands below.

- [Flappy Bird](./examples/src/main/java/dev/prozilla/pine/examples/flappybird)
  > ```gradle runFlappyBird```
- [Snake](./examples/src/main/java/dev/prozilla/pine/examples/flappybird)
  >```gradle runSnake```
  
## Links

- [GitHub](https://github.com/Prozilla/Pine)
- [Discord](https://discord.gg/JwbyQP4tdz)
- [Ko-fi](https://ko-fi.com/prozilla)

## Support Pine

You can support the development of Pine by donating to the developer, Prozilla, on [Ko-fi](https://ko-fi.com/prozilla).
Feel free to star the GitHub repository if you like this project and want to see it continue to be developed.

## License

Pine is licensed under the [GNU GPLv3 license](./LICENSE).
