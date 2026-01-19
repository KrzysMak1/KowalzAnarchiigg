# FancyNpcs API references

## Official docs (current)
- FancyInnovations FancyNpcs docs: https://fancyinnovations.com/docs/minecraft-plugins/fancynpcs

## API docs (legacy but contains dependency snippets)
The legacy docs still include the API getting-started guide, including Gradle/Maven coordinates and the FancyInnovations Maven repository.

- API getting started: https://docs.fancyplugins.de/fancynpcs/api/getting-started/

### Dependency coordinates (from API getting started)
**Gradle**
```kotlin
repositories {
    maven("https://repo.fancyinnovations.com/releases")
}

dependencies {
    compileOnly("de.oliver:FancyNpcs:VERSION")
}
```

**Maven**
```xml
<repository>
    <id>fancyinnovations-releases</id>
    <name>FancyInnovations Repository</name>
    <url>https://repo.fancyinnovations.com/releases</url>
</repository>

<dependency>
    <groupId>de.oliver</groupId>
    <artifactId>FancyNpcs</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
</dependency>
```

Replace `VERSION` with the FancyNpcs API version you want to target.

## Commands used to fetch sources
- `curl -L 'https://r.jina.ai/http://docs.fancyplugins.de/fancynpcs/api/getting-started/'`
- `curl -L 'https://r.jina.ai/http://fancyinnovations.com/docs/minecraft-plugins/fancynpcs'`
