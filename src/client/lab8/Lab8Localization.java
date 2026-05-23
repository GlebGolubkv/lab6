package client.lab8;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Lab 8: localized UI strings stored in this class (four locales).
 */
public final class Lab8Localization {

    public static final Locale LOCALE_RU = Locale.forLanguageTag("ru");
    public static final Locale LOCALE_MK = Locale.forLanguageTag("mk");
    public static final Locale LOCALE_HU = Locale.forLanguageTag("hu");
    public static final Locale LOCALE_EN_AU = Locale.forLanguageTag("en-AU");

    public static final List<Locale> SUPPORTED = List.of(LOCALE_RU, LOCALE_MK, LOCALE_HU, LOCALE_EN_AU);

    private static final Map<Locale, Map<String, String>> MESSAGES = new HashMap<>();

    static {
        put(LOCALE_RU,
                "app.title", "MusicBand — клиент",
                "auth.title", "Вход / регистрация",
                "auth.host", "Хост",
                "auth.port", "Порт",
                "auth.username", "Имя пользователя",
                "auth.password", "Пароль",
                "auth.login", "Войти",
                "auth.register", "Регистрация",
                "auth.connect", "Подключиться",
                "main.title", "Коллекция MusicBand",
                "main.user", "Пользователь",
                "main.language", "Язык",
                "main.filter", "Фильтр",
                "main.sort.column", "Сортировка",
                "main.sort.asc", "По возрастанию",
                "main.refresh", "Обновить",
                "main.delete", "Удалить",
                "main.edit", "Редактировать",
                "main.editField", "Поле…",
                "main.add", "Добавить",
                "main.canvas", "Визуализация",
                "main.log", "Журнал",
                "col.key", "Ключ",
                "col.id", "ID",
                "col.name", "Имя",
                "col.x", "X",
                "col.y", "Y",
                "col.creation", "Дата создания",
                "col.participants", "Участники",
                "col.albums", "Альбомы",
                "col.genre", "Жанр",
                "col.label", "Label",
                "col.owner", "Владелец",
                "cmd.help", "Справка",
                "cmd.info", "Инфо",
                "cmd.show", "Показать",
                "cmd.clear", "Очистить",
                "cmd.script", "Скрипт",
                "cmd.count", "Счёт по участникам",
                "cmd.filter_label", "Фильтр label",
                "cmd.print_labels", "Label ↓",
                "cmd.remove_lower", "Remove lower",
                "cmd.replace_greater", "Replace if greater",
                "cmd.replace_lower", "Replace if lower",
                "band.dialog.add", "Новая группа",
                "band.dialog.edit", "Редактирование",
                "band.name", "Имя",
                "band.key", "Ключ (пусто — авто)",
                "band.save", "Сохранить",
                "band.cancel", "Отмена",
                "error.title", "Ошибка",
                "error.network", "Сервер недоступен",
                "error.empty", "Поле не может быть пустым",
                "info.selected", "Выбран объект",
                "exit", "Выход"
        );
        put(LOCALE_MK,
                "app.title", "MusicBand — клиент",
                "auth.title", "Најава / регистрација",
                "auth.host", "Домаќин",
                "auth.port", "Порта",
                "auth.username", "Корисничко име",
                "auth.password", "Лозинка",
                "auth.login", "Најави се",
                "auth.register", "Регистрација",
                "auth.connect", "Поврзи се",
                "main.title", "Колекција MusicBand",
                "main.user", "Корисник",
                "main.language", "Јазик",
                "main.filter", "Филтер",
                "main.sort.column", "Подредување",
                "main.sort.asc", "Растечки",
                "main.refresh", "Освежи",
                "main.delete", "Избриши",
                "main.edit", "Уреди",
                "main.editField", "Поле…",
                "main.add", "Додади",
                "main.canvas", "Визуелизација",
                "main.log", "Дневник",
                "col.key", "Клуч",
                "col.id", "ID",
                "col.name", "Име",
                "col.x", "X",
                "col.y", "Y",
                "col.creation", "Датум",
                "col.participants", "Учесници",
                "col.albums", "Албуми",
                "col.genre", "Жанр",
                "col.label", "Label",
                "col.owner", "Сопственик",
                "cmd.help", "Помош",
                "cmd.info", "Инфо",
                "cmd.show", "Прикажи",
                "cmd.clear", "Исчисти",
                "cmd.script", "Скрипта",
                "cmd.count", "Број по учесници",
                "cmd.filter_label", "Филтер label",
                "cmd.print_labels", "Label ↓",
                "cmd.remove_lower", "Remove lower",
                "cmd.replace_greater", "Replace if greater",
                "cmd.replace_lower", "Replace if lower",
                "band.dialog.add", "Нова група",
                "band.dialog.edit", "Уредување",
                "band.name", "Име",
                "band.key", "Клуч (празно — авто)",
                "band.save", "Зачувај",
                "band.cancel", "Откажи",
                "error.title", "Грешка",
                "error.network", "Серверот е недостапен",
                "error.empty", "Полето не смее да е празно",
                "info.selected", "Избран објект",
                "exit", "Излез"
        );
        put(LOCALE_HU,
                "app.title", "MusicBand — kliens",
                "auth.title", "Bejelentkezés / regisztráció",
                "auth.host", "Gazdagép",
                "auth.port", "Port",
                "auth.username", "Felhasználónév",
                "auth.password", "Jelszó",
                "auth.login", "Bejelentkezés",
                "auth.register", "Regisztráció",
                "auth.connect", "Csatlakozás",
                "main.title", "MusicBand gyűjtemény",
                "main.user", "Felhasználó",
                "main.language", "Nyelv",
                "main.filter", "Szűrő",
                "main.sort.column", "Rendezés",
                "main.sort.asc", "Növekvő",
                "main.refresh", "Frissítés",
                "main.delete", "Törlés",
                "main.edit", "Szerkesztés",
                "main.editField", "Mező…",
                "main.add", "Hozzáadás",
                "main.canvas", "Vizualizáció",
                "main.log", "Napló",
                "col.key", "Kulcs",
                "col.id", "ID",
                "col.name", "Név",
                "col.x", "X",
                "col.y", "Y",
                "col.creation", "Létrehozva",
                "col.participants", "Résztvevők",
                "col.albums", "Albumok",
                "col.genre", "Műfaj",
                "col.label", "Label",
                "col.owner", "Tulajdonos",
                "cmd.help", "Súgó",
                "cmd.info", "Infó",
                "cmd.show", "Mutat",
                "cmd.clear", "Törlés mind",
                "cmd.script", "Szkript",
                "cmd.count", "Számlálás",
                "cmd.filter_label", "Label szűrő",
                "cmd.print_labels", "Label ↓",
                "cmd.remove_lower", "Remove lower",
                "cmd.replace_greater", "Replace if greater",
                "cmd.replace_lower", "Replace if lower",
                "band.dialog.add", "Új zenekar",
                "band.dialog.edit", "Szerkesztés",
                "band.name", "Név",
                "band.key", "Kulcs (üres — auto)",
                "band.save", "Mentés",
                "band.cancel", "Mégse",
                "error.title", "Hiba",
                "error.network", "A szerver nem elérhető",
                "error.empty", "A mező nem lehet üres",
                "info.selected", "Kijelölt elem",
                "exit", "Kilépés"
        );
        put(LOCALE_EN_AU,
                "app.title", "MusicBand — client",
                "auth.title", "Sign in / register",
                "auth.host", "Host",
                "auth.port", "Port",
                "auth.username", "Username",
                "auth.password", "Password",
                "auth.login", "Sign in",
                "auth.register", "Register",
                "auth.connect", "Connect",
                "main.title", "MusicBand collection",
                "main.user", "User",
                "main.language", "Language",
                "main.filter", "Filter",
                "main.sort.column", "Sort by",
                "main.sort.asc", "Ascending",
                "main.refresh", "Refresh",
                "main.delete", "Delete",
                "main.edit", "Edit",
                "main.editField", "Field…",
                "main.add", "Add",
                "main.canvas", "Visualisation",
                "main.log", "Log",
                "col.key", "Key",
                "col.id", "ID",
                "col.name", "Name",
                "col.x", "X",
                "col.y", "Y",
                "col.creation", "Created",
                "col.participants", "Participants",
                "col.albums", "Albums",
                "col.genre", "Genre",
                "col.label", "Label",
                "col.owner", "Owner",
                "cmd.help", "Help",
                "cmd.info", "Info",
                "cmd.show", "Show",
                "cmd.clear", "Clear",
                "cmd.script", "Script",
                "cmd.count", "Count participants",
                "cmd.filter_label", "Filter label",
                "cmd.print_labels", "Labels desc",
                "cmd.remove_lower", "Remove lower",
                "cmd.replace_greater", "Replace if greater",
                "cmd.replace_lower", "Replace if lower",
                "band.dialog.add", "New band",
                "band.dialog.edit", "Edit band",
                "band.name", "Name",
                "band.key", "Key (empty — auto)",
                "band.save", "Save",
                "band.cancel", "Cancel",
                "error.title", "Error",
                "error.network", "Server unavailable",
                "error.empty", "Field cannot be empty",
                "info.selected", "Selected item",
                "exit", "Exit"
        );
    }

    private Locale current = LOCALE_RU;

    private Lab8Localization() {
    }

    private static final Lab8Localization INSTANCE = new Lab8Localization();

    public static Lab8Localization getInstance() {
        return INSTANCE;
    }

    public Locale getLocale() {
        return current;
    }

    public void setLocale(Locale locale) {
        if (MESSAGES.containsKey(locale)) {
            current = locale;
        }
    }

    public String get(String key) {
        return MESSAGES.getOrDefault(current, MESSAGES.get(LOCALE_RU))
                .getOrDefault(key, key);
    }

    public String displayName(Locale locale) {
        return switch (locale.toLanguageTag()) {
            case "ru" -> "Русский";
            case "mk" -> "Македонски";
            case "hu" -> "Magyar";
            case "en-AU" -> "English (AU)";
            default -> locale.getDisplayName(current);
        };
    }

    private static void put(Locale locale, String... pairs) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put(pairs[i], pairs[i + 1]);
        }
        MESSAGES.put(locale, map);
    }
}
