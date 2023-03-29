# 🔒 AdvancedAuth

<a href="https://ci.2lstudios.dev/job/advancedauth/">
    <img src="https://ci.2lstudios.dev/buildStatus/icon?job=advancedauth&style=flat-square" />
</a> <a href="https://github.com/2lstudios-mc/AdvancedAuth/wiki">
    <img src="https://img.shields.io/badge/Docs-Wiki-blueviolet?style=flat-square" />
</a> <a href="https://discord.com/invite/gF36AT3" alt="Discord">
    <img src="https://img.shields.io/discord/442079515498381312?style=flat-square&color=%237289da&label=Discord&logo=discord&logoColor=%237289da" /> 
</a>

An advanced and open source authentication plugin for Minecraft servers.

## ✨ Features

- 🌱 Open Source.
  - You are free to contribute and propose changes.
- 📦 Database Storage.
  - User data is stored persistently.
- 📄 Data cache.
  - Frequent data is stored in memory to optimize requests.
- 🌐 Redis support (for multiple proxy instances)
  - Sessions an data can be shared between proxy instances.
- 🔒 Various encryption methods available
  - Player passwords are never stored insecurely.
- 📙 Automatically translated.
  - The texts will be displayed in player's language.
- ⌛ Temporary sessions.
  - The user can reconnect without use the password again.
- 🧹 Console filter.
  - Sensitive data will not be displayed on the console.
- ✈️ Migration from other plugins.
  - You can migrate the data if you use other auth plugins.
- 👀 Username/uuid spoof protection.
  - Protects the server against threats from modified clients.
- 💻 DeveloperAPI.
  - Be in full control by extending the plugin's capabilities.
- 🛡️ Protect player inventory.
  - Protect player data while not authenticated.
- 🏁 Country ban.
  - Ban countries from entering your server.
- ⚔️ Faillock
  - Bans IPs who has several failed login attempts.
- 🤝 BungeeCord integration.
  - Send player to another server when logged in.
- 🔑 BungeeCord addon (to improve the security of your server)
  - Blocks proxy from moving the player if he is not logged in.
- 🕊️ Load Balancing
  - Define multiple servers as a lobby which the player will be sent to when authenticated.

## 📄 Cache Engine

|   Engine   | Library / Driver | Implemented |
| :--------: | :--------------: | :---------: |
| In-memory  | [Guava](https://guava.dev/releases/15.0/api/docs/com/google/common/cache/package-summary.html) |     ✔️     |
| Redis      | [Jedis](https://github.com/redis/jedis)                                                        |     ✔️     |

## 📦 Database storage

Some data providers are still pending implementation.

|  Database  | Library / Driver | Implemented |
| :--------: | :--------------: | :---------: |
| MongoDB    | [Milkshake](https://github.com/sammwyy/milkshake) | ✔️ |
| MariaDB    |        --        |     ❌     |
| MySQL      |        --        |     ❌     |
| PostgreSQL |        --        |     ❌     |
| SQLite     |        --        |     ❌     |

## 🔒 Encryption methods

| Algorithm  | Library  | Implemented |
| :--------: | :------: | :---------: |
| BCrypt     | [BCrypt](https://mvnrepository.com/artifact/at.favre.lib/bcrypt) | ✔️ |
| MD5        | Built-In | ✔️ |
| SHA1       | Built-In | ✔️ |
| SHA256     | Built-In | ✔️ |
| SHA512     | Built-In | ✔️ |

## ✈️ Migrations

Possibly in the future other migration systems will be implemented with more plugin support.

|   Plugin   | Implemented |
| :--------: | :---------: |
| AuthMe     |     ✔️     |

## 🤝 Contributing

Contributions, issues and feature requests are welcome!
Feel free to check [issues page](https://github.com/2lstudios-mc/AdvancedAuth/issues).

## ❤️ Show your support

Give a ⭐️ if this project helped you!

Or buy me a coffeelatte 🙌

[Ko-fi](https://ko-fi.com/sammwy) | [Patreon](https://patreon.com/sammwy)

## 📝 License

Copyright © 2023 [Sammwy](https://github.com/sammwyy)
