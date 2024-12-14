# Zasady kontrybucji

## Nazwy branch'y
Dla każdego zadania mamy jakiś numerek np **[T01]** czyli numerek **01**.

Jeżeli jest wymagane stworzenie brancha (większość kategorii feature i maintance) to tworzymy branch z nazwą:
- feat/01 dla zadania (issue) **[T01]** i labelki **feature**
- maint/01 dla zadania (issue) **[T01]** i labelki **maintance**

**Jak zadanie wymaga zmergowania do maina to tworzymy Pull Request z taką samą nazwą jak issue (czyli to [T01] itd)**

## Wersjonowanie
Wersja produktu składa się z dwóch liczb przedzielonych kropką (przykładowo 0.1). Mniej znacząca liczba to tzw. minor
release. Przyjęta konwencja jest taka, że każdy nowy merge z feature brancha jest równoznaczny nowemu minor release.
Przed każdym pull requestem na feature branchu należy wykonać skrypt _feat-version-bump.sh_, który podbija minor version
w pom.xml oraz wykonuje pusha z tą zmianą. Po zmergowaniu brancha (z poziomu Githuba) do maina automatycznie wykona się akcja GH Action — auto tagger,
który automatycznie dodaje tag z odpowiednią wersją. **Uwaga - nie wykonywać tego skryptu na branchu main - nie pozwoli
wam.**

Do przeprowadzania większych zmian, które zasługują na nowe major version, przeznaczony jest skrypt _major-release.sh_.
Skrypt ten może być wykonany tylko na mainie i tworzy nowego commita podbijającego major version i zerującego minor
version. Tak jak wyżej, wykonanie tego skryptu powoduje wykonanie się akcji dodającej i pushującej taga.

## Logowanie
W projekcie korzystamy z loggera slf4j. Obsługujemy dwa poziomy logowania — INFO oraz DEBUG. Logi INFO powinny być
tworzone do informacji o działaniu programu — przyjęcie żądania na endpoincie itp. Logi DEBUG powinny zawierać informacje
przydatne podczas identyfikacji problemów z działaniem aplikacji — przykładowo request body w kontrolerze, czy "przewidziane"
błędy użytkownika (np. niepoprawna struktura JSON-a). Przed decyzją czy coś logować samemu warto też sprawdzić, czy SpringBoot
sam już tego nie loguje — nie ma sensu się powtarzać.

## Narzędzia
- Conventional commits
- Intelij/VSCode
- Maven

## Clean code
- Commity po polsku

## Dokumentacja
Dokumentacja pisana po polsku, w trzeciej osobie i generowana jako JavaDoc.

Pierwsze zdanie będące zwięzłym opisem zaczyna się od czasownika.
#### Przykładowy doc comment dla metody:
```java
/**
* Oblicza iloraz dwóch liczb.
*
* @param a dzielna 
* @param b dzielnik
* @return wynik dzielenia liczby a przez liczbę b
*/
```
#### Kolejność tagów:
- `@param` (dla metod i konstruktorów)
- `@return` (dla metod)
- `@exception` (dla metod)
#### Zalecane jest używanie  `<code>...</code>` dla kluczowych słów/nazw takich jak:
- Java keywords
- package names
- class names
- method names
- interface names
- field names
- argument names
- code examples