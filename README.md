# Bottowitzsch (Discord-Bot)

[![make test](https://github.com/hizr/bottowitzsch/workflows/maven-build/badge.svg)](https://github.com/hizr/bottowitzsch/actions/workflows/maven.yml?query=workflow%3Amaven-build+branch%3Amaster++)
[![make test](https://github.com/hizr/bottowitzsch/workflows/docker-image/badge.svg)](https://github.com/hizr/bottowitzsch/actions?query=workflow%3Adocker-image+branch%3Amaster++)

**Table of Contents**

<!-- toc -->

- [About](#about)
- [Prerequisites](#prerequisites)
- [Usage](#usage)

<!-- tocstop -->

## About

Bottowitzsch is [discord](https://discord.com/) bot that is capable of playing music for you and your friends. Its
complete free and can be used however you want. The bot is Java based
using [spring boot](https://spring.io/projects/spring-boot), [discord4j](https://github.com/Discord4J/Discord4J) and
the [lavaplayer](https://github.com/sedmelluq/lavaplayer) library to do the job.

There is no public host for this bot. You need to host it on your own. Thanks to discord reactive architecture, there is
no need to open any port because this bot is acting like a consumer of discords messages.

## Prerequisites

You need a [discord developer account](https://discord.com/developers/docs/intro) with a registered application which
defined as a bot. Follow the discord [documentation](https://discord.com/developers/docs/intro) to get the job
done. [This is a pretty cool article of doing this](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token)
.

You need to build bottowitzsch to use him. There will be no prepackaged release. There are two ways of doing this.

1. Build the application with docker. Therefor docker and docker-compose must be installed. (recommended)
2. Build the application from scratch with maven. Therefor java (at least version 11) and maven 3.6 must be installed on
   your machine.

## Usage

### Docker Compose

Create or use you existing docker-compose.yml and replace the <PUB-TOKEN> with the secret bot token you can find the the
discord developer
portal ([See this article for help](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token#token-security))
.

```yaml
version: '2'
services:
  bottowitzsch:
    build:
      context: ./bottowitzsch # path to bottowitzsch repo clone or download
      dockerfile: Dockerfile
    restart: always
    environment:
      - PUB_TOKEN=<PUB-TOKEN> # replace with the secret bot token
```

### Plain Java

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