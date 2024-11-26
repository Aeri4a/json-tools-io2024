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
w pom.xml oraz wykonuje pusha z są zmianą. Po zmergowaniu brancha (z poziomu Githuba) do maina automatycznie wykona się akcja GH Action — auto tagger,
który automatycznie dodaje tag z odpowiednią wersją. **Uwaga - nie wykonywać tego skryptu na branchu main - nie pozwoli
wam.**

Do przeprowadzania większych zmian, które zasługują na nowe major version, przeznaczony jest skrypt _major-release.sh_.
Skrypt ten może być wykonany tylko na mainie i tworzy nowego commita podbijającego major version i zerującego minor
version. Tak jak wyżej, wykonanie tego skryptu powoduje wykonanie się akcji dodającej i pushującej taga.

## Narzędzia
- Conventional commits
- Intelij/VSCode
- Maven

## Clean code
- Commity po polsku

## Dokumentacja
