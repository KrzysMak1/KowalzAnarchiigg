# Dream-Kowal

Plugin do systemu ulepszania uzbrojenia dla serwerów Paper.

## Wymagania

- Java 21
- Paper 1.21.10
- (Opcjonalnie) Vault + ekonomia do kosztów ulepszeń

## Instalacja

1. Zbuduj plugin:
   ```bash
   mvn package
   ```
2. Skopiuj wygenerowany plik JAR z `target/` do folderu `plugins/` na serwerze.
3. Uruchom serwer, aby wygenerować pliki `config.yml` i `message.yml`.

## Konfiguracja

Po pierwszym uruchomieniu plugin wygeneruje:

- `config.yml` – ustawienia GUI, poziomów ulepszeń, efektów itd.
- `message.yml` – wiadomości wyświetlane graczom.

Zachowuj klucze konfiguracyjne (nazwy pól) bez zmian, aby zachować kompatybilność.

## Komendy

- `/kowal` – główna komenda GUI.
- `/kowal give <gracz|all> <ilość>` – nadaje kamień kowalski.
- `/kowal set` – ustawia przedmiot kamienia kowalskiego z ręki.
- `/kowal ulepsz <poziom>` – ręczne ulepszanie w ręku.
- `/kowal reload` – przeładowanie konfiguracji.

## Uprawnienia

- `dream-kowal.give`
- `dream-kowal.set`
- `dream-kowal.upgrade`
- `dream-kowal.reload`
