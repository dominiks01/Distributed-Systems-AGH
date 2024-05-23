# Gniazda TCP/UDP
Projekt realizujący chat wielu użytkowników z pomocą serwera. 

# Cechy projektu

* Clienci łączą się serwerem przez protokół TCP 
* Serwer oraz każdy klient posiadają dodatkowy kanał UDP oraz w wersji multicast
* Po wpisaniu komendy ‘U’ u klienta przesyłana jest wiadomość przez UDP na serwer, który rozsyła ją do pozostałych klientów
* Serwer przyjmuje wiadomości od każdego klienta i rozsyła je do pozostałych (wraz zid/nickiem klienta)
* Serwer jest wielowątkowy

# Użyte technologie
* Java, Maven

## Uruchomienie projektu

```bash
# Compile the Project
mvn compile
```

```bash
# Run the Server
mvn exec:java -P run-server
```

```bash
# Run the Client
mvn exec:java -P run-client
```