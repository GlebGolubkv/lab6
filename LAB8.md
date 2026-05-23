# Лабораторная работа 8 — JavaFX клиент

Ветка: **lab8**

## Запуск

1. Запустите сервер (как в лабе 7):
   ```bash
   mvn -Pserver package
   java -jar target/lab7-server-jar-with-dependencies.jar
   ```

2. Запустите GUI-клиент:
   ```bash
   mvn -Plab8-client javafx:run
   ```
   или из IDE: main class `client.lab8.Lab8Application`.

3. В окне входа: хост `localhost`, порт **8887** (порт сервера из `ServerNetworkManager`).

## Новые пакеты (только lab8)

| Путь | Назначение |
|------|------------|
| `src/client/lab8/` | JavaFX UI, i18n, canvas, сетевой фасад |
| `src/common/lab8/` | DTO `MusicBandEntry` |
| `src/server/commands/lab8/` | Команда `GET_COLLECTION` для GUI |

Консольный клиент (`client.Client`) и серверные команды лаб 5–7 **не переписывались**.

## Синхронизация

Каждые 2.5 с клиент запрашивает `get_collection` и обновляет таблицу и canvas у всех подключённых GUI.

## Локали

ru, mk, hu, en-AU — переключение в окне входа и на главном экране без перезапуска.
