# TrashBet
[WIP] [SaltyBet](https://www.saltybet.com/) - style betting system but in Kotlin-Ktor

Opening this up to the Hacktoberfest public because I'm not good at Kotlin yet, don't make me regret this

Todo items in the [Issues](https://github.com/JoelPagliuca/trashbet/issues)

This is a kotlin project, the web code is hidden in `./app/web`

## Configuration
Environment variables
```sh
TRASHBET_PORT = 8080
TRASHBET_ENVIRONMENT = testing
```
Running tests
```
./gradlew test
```

## Running
### API
```sh
./gradlew run
```
health [endpoint](http://localhost:8080/health)

Web bundle is also being served on this port at the webroot

### Web
```sh
# from ./app/web
npm install
npm run dev
```
available on [port 5000](http://localhost:5000/)

## Contributors
[JoelPagliuca](https://github.com/JoelPagliuca)
