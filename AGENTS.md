# AGENTS.md

This file provides guidance for AI coding agents working in the **Bottowitzsch** repository -- a Discord music bot built with Java, Spring Boot, and Discord4J.

## Project Overview

- **Language:** Java 11+ (CI and Docker use JDK 17)
- **Framework:** Spring Boot 2.2.13.RELEASE (reactive stack via Project Reactor)
- **Key libraries:** Discord4J 3.1.2, LavaPlayer 1.3.77, Lombok 1.18.24, Apache Commons Lang3
- **Build tool:** Maven 3.6.3 (via Maven Wrapper)
- **Package structure:** `de.hizr.discord.bottowitzsch`

## Build Commands

```bash
./mvnw clean install              # Full build (compile + test + package)
./mvnw clean install -DskipTests  # Build without tests
./mvnw clean package              # Package only (no install to local repo)
./mvnw -B clean install test --file pom.xml  # CI build (GitHub Actions)
```

## Test Commands

```bash
./mvnw test                                    # Run all tests
./mvnw test -Dtest=YouTubeSearchExtractorTest  # Run a single test class
./mvnw test -Dtest=YouTubeSearchExtractorTest#testExtractor  # Single test method
./mvnw test -Dtest="de.hizr.discord.bottowitzsch.player.trackidentifier.extractor.*"
./mvnw test -Dtest=YouTubeSearchExtractorTest -Dsurefire.useFile=false  # Verbose
```

### Test Framework

- **JUnit 5** (Jupiter) with `@ParameterizedTest` and `@MethodSource` for data-driven tests
- **Mockito** for mocking (`Mockito.mock()`, `Mockito.when()`)
- **AssertJ** for assertions (`assertThat(...).contains(...)`)
- Test classes are **package-private** (no `public` modifier) per JUnit 5 convention
- Test naming: `<ClassName>Test` (e.g., `YouTubeSearchExtractorTest`)
- Tests live in `src/test/java/` mirroring the main source path

## Lint / Formatting

No linter or formatter is configured. Follow the existing code style described below.

## Code Style Guidelines

### Indentation and Braces

- **Tabs** for indentation (not spaces)
- Opening brace on the **same line** as the declaration
- `else` on a **new line** after closing brace:

```java
if (condition) {
	doSomething();
}
else {
	doOther();
}
```

### Imports

- **Explicit imports only** -- never use wildcard imports (`import java.util.*`)
- **Order:** `static` imports first (blank line), then `java.*`, then third-party, then project-internal

### Types and Naming

- **Classes:** `PascalCase` -- **Methods/variables:** `camelCase` -- **Constants:** `UPPER_SNAKE_CASE`
- **Packages:** all lowercase, no underscores (e.g., `trackidentifier`, `eventlistener`)
- **Interfaces:** no `I` prefix (e.g., `Command`, `EventListener`, `LinkExtractor`)
- **Implementations:** descriptive names, no `Impl` suffix (e.g., `YouTubeSearchExtractor`)

### Method Parameters

- Always use `final` on method parameters:
```java
public Mono<Void> execute(final MessageCreateEvent event) { ... }
```

### Lombok Usage

- `@RequiredArgsConstructor` for constructor-based dependency injection
- `@Getter` / `@Setter` for accessors
- `@Slf4j` for logging (provides `log` field)
- `val` for local variable type inference (`val x = something()`)
- Do **not** add `@Builder` or `@Data` unless there is a clear need

### Spring Annotations

- `@Component` for general Spring beans (commands, helpers)
- `@Service` for service-layer classes (listeners, context services)
- `@Configuration` for configuration classes
- Constructor injection via `@RequiredArgsConstructor` -- do **not** use `@Autowired`

### Reactive Patterns

- Return `Mono<Void>` for void reactive operations
- Chain with `.then()`, `.flatMap()`, `.doOnNext()`, `.doOnSuccess()`
- Use `Mono.justOrEmpty()` for Optional-to-Mono conversion
- Use `Flux.fromIterable()` to iterate collections reactively
- Handle errors with `.onErrorResume()` or `.onErrorContinue()`

### Error Handling

- Custom exceptions extend `RuntimeException` (unchecked):
  - `CommandException` -- for command parsing/extraction failures
  - `GuildContextException` -- for guild context lookup failures
- Use `String.format()` for exception messages:
```java
throw new CommandException(String.format("Command cant be extracted from message '%s'", message));
```
- In reactive chains, use `CommandHelper.handleError()` which logs guild name and username context
- Pattern: `.onErrorContinue((throwable, o) -> helper.handleError(throwable, "description", event))`

### String Operations

- Use `StringUtils` from Apache Commons Lang3 for string checks (e.g., `StringUtils.startsWith()`)
- Use `String.format()` for string formatting (not concatenation in log/error messages)

## Development Workflow (ECC)

This project uses the **Everything Claude Code (ECC)** plugin (`ecc-universal`). Follow this workflow:

1. **Plan** -- Use the **planner** agent for complex features; break into phases
2. **TDD** -- Use the **tdd-guide** agent; write tests first (RED), implement (GREEN), refactor (IMPROVE)
3. **Review** -- Use **java-reviewer** agent after writing code; use **java-build-resolver** if builds fail
4. **Commit** -- Conventional commits: `<type>: <description>` (types: feat, fix, refactor, docs, test, chore, perf, ci)

### Relevant ECC Agents

| Agent | When to Use |
|-------|-------------|
| planner | Complex features, refactoring |
| tdd-guide | New features, bug fixes (write tests first) |
| java-reviewer | After writing/modifying Java code |
| java-build-resolver | When Maven build fails |
| security-reviewer | Before commits involving auth or token handling |
| code-reviewer | General code quality review |

### Security Checks (Before Commits)

- No hardcoded secrets (bot token must come from environment or properties file)
- Error messages must not leak sensitive data
- Validate all user input at system boundaries

## Project Architecture

```
src/main/java/de/hizr/discord/bottowitzsch/
  BottowitzschApplication.java   -- Spring Boot entry point
  BottowitzschConfiguration.java -- Discord client setup, event listener registration
  BottowitzschRunner.java        -- Keeps app alive by blocking on disconnect
  command/                        -- Bot commands (Command interface + implementations)
  context/                        -- Per-guild audio state management
  eventlistener/                  -- Discord event handling
  player/                         -- Audio player, track scheduling, track identification
    trackidentifier/              -- Strategy pattern for resolving user input to audio URLs
      extractor/                  -- LinkExtractor implementations
```

### Design Patterns

- **Command pattern:** Each bot command implements `Command` interface with `messageHooks()`, `description()`, `execute()`
- **Strategy pattern:** `LinkExtractor` implementations for different track sources
- **Factory pattern:** `GuildContextFactory` creates `GuildContext` instances
- **Observer pattern:** `EventListener<T>` interface for Discord gateway events

## Docker & CI/CD

- `docker build -f distribution/x64/Dockerfile -t bottowitzsch .` (x64)
- `docker build -f distribution/arm32/Dockerfile -t bottowitzsch-arm .` (ARM32/RPi)
- `docker-compose up --build` (requires `.env` with `NAME`, `DOCKERFILE`, `PUB_TOKEN`)
- GitHub Actions on push to `master`/`developer`: Maven build + test (JDK 17), Docker x64, Docker ARM32
