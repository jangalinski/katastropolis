# One Armed Bandit - A Holisticon Kata

## Kata Description

Die Aufgabe ist es, einen einarmigen Banditen (Glückspielautomat) zu implementieren. Gespielt werden soll mittels REST-Aufrufen. Die Request- und Response-Bodys
sollen im JSON-Format übergeben bzw. entgegengenommen werden.
Entwickle so gewissenhaf, wie du Sofware auch im Alltag entwickeln würdest.

### Regeln
Das Spiel funktioniert nach dem bekannten Prinzip: Ein Spieler versucht sein Glück
am Automaten und zieht am Hebel. Ein Spiel kostet 3 Kredits. Die Maschine hat drei
Räder, auf denen jeweils entweder ein Apfel, eine Banane oder eine Clementine erscheinen. Wenn zufällig alle drei Räder übereinstimmen, gewinnt der Spieler.
In diesem Fall werden je nach Obstsorte folgende Gewinnsummen ausgezahlt:
- 3 Äpfel: 10 Kredits
- 3 Bananen: 15 Kredits
- 3 Clementinen: 20 Kredits
Ein Spieler kann Geld einzahlen oder es sich auszahlen lassen.

### Nicht-funktionale Anforderungen
1. Die Anwendung ist in Java oder Kotlin geschrieben.
2. Die Anwendung ist self-contained, zum Beispiel mit Hilfe von Spring Boot.
   Sie lässt sich direkt starten mit: java -jar <application.jar>
3. Die Anwendung lässt sich via Maven oder Gradle ohne spezielle Anpassungen
   erstellen.
4. Stelle sicher, dass das Spiel funktioniert

### Optionale Anforderungen
Wenn noch ausreichend Zeit zur Verfügung steht kannst du die folgende optionale
Anforderung implementieren:
Der Spieler kann seinen Einsatz für ein Spiel erhöhen. Für seine Risikofreude wird er
im Gewinnfall mit entsprechend mehr Kredits belohnt

### Hilfestellungen
- https://de.wikipedia.org/wiki/Einarmiger_Bandit
- https://projects.spring.io/spring-boot/
- http://spring.io/guides/gs/rest-service/
- http://martinfowler.com/bliki/TestPyramid.html

## Features
- Immutable domain model
- State modeling with sealed interfaces
- Event-sourced game history
- REST API with OpenAPI/Swagger UI
- Fully tested with JUnit 5 and AssertJ

## Getting Started
- Build: `./gradlew build`
- Run: `java -jar build/libs/<jarfile>.jar`
- Swagger UI: [http://localhost:27489](http://localhost:27489)

## Chat History

### 2025-06-25 Der Anfang

1. entferne das plugin spring dependency management und nutze stattdessen eine platform dependency auf spring-boot-dependencies
1. nutze die libs toml zur declartion der dependency
1. der eintrag fehlt noch in der libs toml
1. baue und fixe
1. baue nochmal
1. lege ein banner.txt mit one armed bandid und der verwendeten Spring boot version in src/main/resources von one-armed-bandid
1. ersetze one armed bandit im banner.txt durch figlet
1. das figlet soll one armed bandid sagen
1. da steht nicht "one armed bandid"
1. füge springdoc starter-ui zur libs toml und dem projekt one-armed-bandid hinzu
1. was muss ich in die application.yaml eintragen damit ein call auf localhost:27489 direckt auf die swagger-ui zeigt
1. mach das
1. hier sit eine coding kata: ... (kompletter Katalogtext)
1. erzuege mir im package domain in one-armed-bandit die Klassen um die domain logik (rollen der 3 räder mit wert Apfel, Banane , Clementine) abzubilden. Noch keine Controller usw
1. enum values ist deprecatd
1. bevorzuge expressions für functions statt bodies
1. die numerischen werte für Apfel usw. sollten im enum definiert werden
1. das package domain muss ein unterpackage der spring application klasse sein
1. fast. den ganzen pfad io.github.jangalinski ....
1. das word kata fehlt
1. vor onearmedbandit!
1. gut
1. Das Ergebnis eines Laufs sollte als Triple von 3 Fruits definiert werden. Ein Constructor, der die drei einzelwerte nimmt (für tests), einen der alle 3 zufällig ermittelt
1. beende im enum nach Clementine mit Komma, neue Zeile Semikolon
1. Das zufällige wählen eines enums sollte eine static funtion im enum selber sein
1. die auswertung (3 gleiche Symbole mit dem entsprechendem Wert) soll eine function in der data class result sein
1. dadurch brauchen wir die asList function nicht mehr
1. nutze named parameters für den secondary constructor
1. generiere junit5 tests im package domain. der erste test sollte ein parameterized test sein, der verschiedene Kombinationen von SlotResults entgegen nimmt. die drei Gewinnfälle sollten abgebildet sein sowie exemplarisch 2-3 nicht gewinn Fälle. Der parameterized test sollte die 3 Fruits und den expected payout entgegen nehmen. Vorzugsweise als CsvSource
1. die tests können nicht ausgeführt werden, Fehler: No matching tests found in any candidate test task. ...
1. ja, die test klasse liegt falsch. Orientiere dich an dem package von OneArmedBanditApplicationITest und lege den test einen domain ordner
1. nein. der pfad lautet immer noch src/test/kotlin/io/github/... er muss src/main/test/domain/ lauten
1. fast . entferne den test in io/github/... und ändere das package in domain/SlotResultTest.kt so, dass es ein unterpackage von OneArmedBanditApplicationITest ist
1. nein. wenn relativ Ordner als sub-packages angelegt werden, muss der das package in der Datei das package der root Klasse erweitern, nicht erestzen
1. super. imports organisieren und unnötige klasse im alten Pfad entfernen
1. immer noch no matching tests ist junit5 sicher korrekt konfiguriert?
1. ja, das war es
1. ok. git commit mit "start one armed bandit"
1. meine swagger ui zeigt "OpenAPI definition" ... das soll "one Armed Bandit" stehen.
1. das hat nichts geändert und in der application yaml sind die info (title,version,..) properties gelb markiert als unbekannt
1. das funktioniert nicht info.title ist nicht bekannt
1. lege eine openApi Configuration an, die title, version und description definiert und entferne die einträge aus der yaml
1. die customOpenAPI ben function direkt in die OneArmedBandidApplication schieben, wir brauchen keine extra config Klasse.
1. es ist ab sofort strikt VERBOTEN! unter src/main/kotlin verzeichnisse anzulegen, die mit io/ beginnen!
1. Wir brauchen im model package nun das Game. Ein game kann neu erstellt werden mit einer random uuid und einem initialen Coin Wert. Coin ist eine value class mit wert int. und einer Historie (List) von SlotResults. Operationen auf einem Game: ...
1. packge von Game.kt ist falsch: muss base package von OneArmedBanditApplication übernehmen und "domain" anhängen
1. Verschiebe die value class Coin in die Datei model.kt
1. entferne Coin aus Game.kt
1. Nein, sie ist noch da
1. Game ist eine Data class.
1. Random sollte eine immutable Property von Game sein.
1. die data class Game sollte komplett immutable sein. kein var für Coins. Benutze copy() um den Coin Wert und die Historie der Results zu verändern
1. random kann provate sein
1. require(coins.value >= 3) { "Not enough coins to play" } als Extra Aufgabe kann der Spieler mehr Coins setzen als 3. Darum sollte der aktuelle Einsatz als val in der data class stehen (default: 3) und mit einer neuen OPeration (copy) modifizierbar sein. Der Wert des Einsatzes darf nie größer als der aktuelle coins wert sein und mindestens 3
1. bennene withBet um in changeBet
1. fun payout in SlotReult kann eine lazy property sein, der Wert ändert sich nicht mehr
1. wir sollten ein State modell einführen. Game ist ein sealed interface mit zwei Ausprägungen: ActiveGame und EndedGame. Nur auf einem Active Game können Operationen ausgeführt werden. Das ended Game hält nur die Historie und die Id. endGame überführt das ActiveGame in ein EndedGame
1. ok
1. Ok. Weitere Idee: Die Historie sollte auch abbilden, dass Coins eingezahlt, ausgezahlt wurden oder das Spiel beendet wurde. Jede Operation sollte ein entsprechendes Event in die Historie schreiben. Dazu sollten auch die HistoryEvents eine sealed Struktur sein mit den ausprägungen: GameStarted(id, initial coins), LeverPulled mit Result , BetChanged, coinsIncreased, coinsDecreased und gameEnded. In einer extra Datei in domain, name "events.kt"
1. Ok. Ich glaube, die GameId sollte auch eine value class in model.kt sein und eine UUID enthalten
1. Super. Füge KDoc zur GameId hinzu um zu dokumentieren, was es ist
1. cool
1. Kannst du mir eine komplette Liste meiner Eingaben in diesem Chat zeigen?
1. Klasse. Erzeuge in one-armed-bandit eine README.md datei. Titel: One Armed Bandit - A Holisticon Kata
1. Den ganzen Text der kata den ich am Anfang als Input übergeben habe, als ersten Punkt der README (header level h2 (##) ) einfügen
1. Super. Jetzt einen Letzten Punkt einführen: level ## unsere gesamte Chat Historie als Liste, dabei immer meine Eingaben als top-level-list items und deine Antworten als eingerückte sub-liste
1. gib mir die numerische LIste meiner Eingaben als copy&paste volage
