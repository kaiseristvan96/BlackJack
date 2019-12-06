# BlackJack
A basic Blackjack type card game.

A szerver futtatása:
1. Lépjünk bele a Server mappába, ahol a pom.xml található.
2. Itt nyissunk egy powershell-t és írjuk bele, hogy mvn spring-boot:run
3. Ezzel elindult a szerver a 8080-as porton.

A kliens futtatása:
1. Lépjünk bele a  Client mappába, ahol a pom.xml található.
2. Itt nyissunk egy powershell-t és írjuk bele, hogy mvn spring-boot:run
3. Ezzel elindult a kliens a 9999-es porton.

Játék indítása:
1. Nyissuk meg a http://localhost:8080/game.xhtml linket, ahol be kell írnunk egy általunk választott port számot, ezzel látrehozzuk a játék szervert.
2. Ezután nyissuk meg a http://localhost:9999/join.xhtml linket, ahol be kell írnunk ugyan azt a számot, amit a játékszervernek adtunk, ezzel csatlakozunk a játékszerverhez.
3. Majd az alábbi linken játszhatunk a gép ellen: http://localhost:9999/play.xhtml .
4. Figyeljük a powershellben a kapott válaszokat.
