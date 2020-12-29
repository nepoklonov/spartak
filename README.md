# Сайт для МХК Спартак

## Запустить сайт:
- Запустить задачу `server:run` через окошко **Gradle** или через терминал, набрав: `./gradlew :server:run`. Сайт соберётся и запустится по адресу: https://localhost:8080

## Web distribution (копипаста с оригинального описания)
- `./gradlew :server:build` will create `server/build/distributions/server-0.1.1.zip`. You can deploy it on the server and run `bin/server` to start the server. Client is included into the `lib/server.jar` 
