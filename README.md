# Bottowitzsch (Discord-Bot)

[![make test](https://github.com/hizr/bottowitzsch/workflows/maven-build/badge.svg)](https://github.com/hizr/bottowitzsch/actions/workflows/maven.yml?query=workflow%3Amaven-build+branch%3Amaster++)

Damit Bottowitzsch zum Leben erweckt werden kann muss ihn ein Konfiguration in Form einer Properie-File beim Start übergeben werden!

```
java -jar target/bottowitzsch.jar --spring.config.location=file:../bottowitzsch-tmp/bottowitzsch.properties
```

Benötigte Properties:
```
token=<BOT-TOKEN>
```
