# Bottowitzsch (Discord-Bot)

[![make test](https://github.com/hizr/bottowitzsch/workflows/maven-build/badge.svg)](https://github.com/hizr/bottowitzsch/actions/workflows/maven.yml?query=workflow%3Amaven-build+branch%3Amaster++)
[![make test](https://github.com/hizr/bottowitzsch/workflows/docker-image/badge.svg)](https://github.com/hizr/bottowitzsch/actions?query=workflow%3Adocker-image+branch%3Amaster++)

**Table of Contents**

<!-- toc -->

- [About](#about)
- [Installation](#installation)
- [Usage](#usage)

<!-- tocstop -->

## About

Bottowitzsch is a [discord](https://discord.com/) bot that is capable of playing music. Its complete free and can be
used however you want. The bot is Java based using [spring boot](https://spring.io/projects/spring-boot)
, [discord4j](https://github.com/Discord4J/Discord4J) and the [lavaplayer](https://github.com/sedmelluq/lavaplayer)
library to do the job.

There is no public host for this bot. You need to host it on your own. Thanks to discord reactive architecture, there is
no need to open any port because this bot is acting like a consumer of discords messages.

## Installation

To host a discord bot you need to do the following steps:

1. Create a discord developer account and create a bot application
2. Build and host the bot
3. Invite the Bot to your discord server

### 1. Create Discord developer account

You need a [discord developer account](https://discord.com/developers/docs/intro) with a registered application which is
defined as a bot. Follow the discord [documentation](https://discord.com/developers/docs/intro) to get the job
done. [This is a pretty cool article of doing this.](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token)

### 2. Build the bot

You need to build the application to use it. There will be no prepackaged release. There are two ways of doing this.

1. Build the application with docker. Therefor docker and docker-compose must be installed. (recommended)
2. Build the application from scratch with maven. Therefor java (at least version 11) and maven 3.6 must be installed on
   your machine.

#### 2.a Docker Compose (recommended)

Create a docker-compose ```.env``` environment file in the project directory and add the following parameter.

| Parameter  | Example                     | Description                                                                                                                                               |
|------------|-----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| PUB_TOKEN  | ...                         | The secret bot Token. [See this article for help](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token#token-security) |
| DOCKERFILE | distribution/x64/Dockerfile | The path to the dockerfile, depending on your host system.                                                                                                |
| NAME       | master                      | This is used as a suffix for the container name. So you can run multiple bot instances on one machine, without losing the overview.                       |

Example ```.env```-File:
```properties
NAME=dev
DOCKERFILE=distribution/x64/Dockerfile
PUB_TOKEN=1m-4-53cr3t
```

Start the bot with the following command:
```shell
docker-compose up --build -d
```

#### 2.b Plain Java (skip if you use docker-compose)

Build bottowitzsch using maven
```shell
mvn clean install
```

Create a properties file. For example a file called bottowitzsch.properties with the following content. Replace the <
BOT-TOKEN> with the secret token you can find in the discord developer
portal ([See this article for help](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token#token-security))
.

```properties
token=<BOT-TOKEN>
```

After the build was successful, start the bot with the following command.

```shell
java -jar target/bottowitzsch.jar --spring.config.location=file:<path to your properties file>/bottowitzsch.properties
```

### 3. Invite the bot to your discord server

After you have successfully created and hosted the bot, you have to invite him to you discord server. You can do this
using the discord [Permission Calculator](https://discordapi.com/permissions.html#8). To do his job, the bot needs the
following minimal permissions:

* Send Messages
* Speak

To create the invitation link enter your bots Client ID you can find in you development account back in "General
information" ([Another link to this awesome article](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token#adding-your-bot-to-your-server))
.

## Usage

Connect to your discord server, join one of your voice channels, grand the Bot read rights to one of your text channels
and write: ```!h``` or ```!help``` and the bot should answer with all commands he listens to.
