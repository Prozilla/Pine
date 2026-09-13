<div align="center">
 <br />
 <p>
  <a href="https://pine.prozilla.dev/"><img src="https://pine.prozilla.dev/logo-dark.svg" width="500" alt="Pine Engine" /></a>
 </p>
 <p>
  <a href="https://github.com/Prozilla/Pine/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/Prozilla/Pine?style=flat-square&color=FF4D5B&label=License"></a>
  <a href="https://github.com/Prozilla/Pine"><img alt="Stars" src="https://img.shields.io/github/stars/Prozilla/Pine?style=flat-square&color=FED24C&label=%E2%AD%90"></a>
  <a href="https://github.com/Prozilla/Pine"><img alt="Forks" src="https://img.shields.io/github/forks/Prozilla/Pine?style=flat-square&color=4D9CFF&label=Forks&logo=github"></a>
 </p>
</div>

## About

Pine stands for **P**rozilla's game eng**ine**.
Pine is a lightweight Java game engine that serves as a framework for [LWJGL](https://www.lwjgl.org/).
Pine uses an [ECS](https://en.wikipedia.org/wiki/Entity_component_system) to represent game objects.

- **Entities** - Entities are unique objects that exist inside a scene with minimal data tied to them.
- **Components** - Entity data is stored in modular components.
- **Systems** - Systems perform logic based on data stored in components or user input to update the entity's state or render to the screen.

As a framework, Pine makes working with the low-level LWJGL library a lot easier and more intuitive, without sacrificing control and flexibility.
Because everything is split into layers of abstraction, you can choose how much of Pine's existing functionality you want to utilize
and where you want insert your own code.

The high-level layers of Pine are designed to let you make games with as little boilerplate as possible. 
This allows you to focus on the mechanics of your games, instead of having to deal with complex low-level game engine logic.

You can find some example projects that showcase Pine's features in [/examples](./examples).

## Links

- [Website](https://pine.prozilla.dev/)
- [GitHub](https://github.com/Prozilla/Pine)
- [Javadoc](https://javadoc.pine.prozilla.dev/)
- [Discord](https://discord.gg/JwbyQP4tdz)
- [Ko-fi](https://ko-fi.com/prozilla)

### Extensions

- [PiNet](./extensions/pi-net) - Networking library for Pine

### Related repositories

- [Prozilla/Pine-boilerplate](https://github.com/Prozilla/Pine-boilerplate) - Boilerplate code for a Pine project
- [Prozilla/Pine-site](https://github.com/Prozilla/Pine-boilerplate) - Source code of the official Pine website

## Support Pine

You can support the development of Pine in many ways:

- By donating to the developer, Prozilla, on [Ko-fi](https://ko-fi.com/prozilla)
- By starring the GitHub repository
- By creating issues or pull requests to contribute to the development of Pine directly
- By sending nice, encouraging messages to Prozilla

## License

Pine is licensed under the [GNU LGPLv3 license](./LICENSE).
