# OcadoTest

Aplikacja w Javie umożliwiająca wybór optymalnej kombinacji metod płatności dla zamówień, z uwzględnieniem rabatów i limitów. Obsługuje płatności punktami lojalnościowymi oraz kartami z promocjami.

## Wymagania

- Java 21
- Maven 4.0.0+

## Budowanie i uruchamianie

### 1. Klonowanie repozytorium

```bash
git clone https://github.com/yourusername/OcadoTest.git
cd OcadoTest
```

### 2. Budowanie JARa

```bash
mvn clean package
```

Gotowy plik znajdziesz w `target/app.jar`.

### 3. Uruchamianie

```bash
java -jar target/app.jar <ścieżka/do/pliku/z/zamówieniami> <ścieżka/do/pliku/z/metodami/płatności>
```

## 📁 Struktura katalogów

```
src/
└── main/
    └── java/
        └── app/
            ├── PaymentOptimizerApp.java   # punkt wejścia aplikacji
            ├── PaymentOptimizer.java      # logika wyboru najlepszej opcji
            ├── PaymentOption.java         # struktura reprezentująca wybór
            ├── PaymentMethod.java         # metoda płatności (punkty, karta)
            └── Order.java                 # zamówienie klienta
└── test/
    └── java/
        └── app/
            ├── PaymentOptimizerTest.java
            ├── PaymentOptionTest.java
            ├── PaymentMethodTest.java
            └── OrderTest.java
```

## Dane wejściowe

### Przykładowy `orders.json`

```json
[
  {
    "id": "ORDER1",
    "value": "250.00",
    "promotions": ["mZysk"]
  }
]
```

### Przykładowy `payments.json`

```json
[
  {
    "id": "PUNKTY",
    "discount": "15",
    "limit": "100.00"
  },
  {
    "id": "mZysk",
    "discount": "10",
    "limit": "180.00"
  }
]
```

## Zasady działania

- Punkty mogą pokryć do 100% wartości zamówienia.
- Jeśli punkty nie pokryją całości, można łączyć je z inną metodą płatności.
- Jeżeli punkty pokryją 100% wartości zamówienia, naliczany jest ich rabat
- Jeżeli zamówienie zostanie pokryte w co najmniej 10% punktami, ale nie w całości, naliczany jest rabat 10%
- Jeżeli zamówienie zostanie w całości pokryte kartą, której przysługuje rabat, ten rabat jest naliczany
- Najpierw wybierane są opcje z najwyższym rabatem, następnie te z najmniejszą płatnością kartą.

## 🧪 Testowanie

Testy jednostkowe uruchomisz komendą:

```bash
mvn test
```

## 📦 Zależności

- [Jackson](https://github.com/FasterXML/jackson) — parsowanie JSON
- [JUnit 5](https://junit.org/junit5/) — testy jednostkowe
- [Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/) — tworzenie pojedynczego JARa z zależnościami
