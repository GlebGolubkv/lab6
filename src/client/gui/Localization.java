package client.gui;

import common.dataclasses.CommandType;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Локализация строк интерфейса (русский, македонский, венгерский, английский AU).
 */
public final class Localization {

    /** Локаль: русский. */
    public static final Locale LOCALE_RU = Locale.forLanguageTag("ru");
    /** Локаль: македонский. */
    public static final Locale LOCALE_MK = Locale.forLanguageTag("mk");
    /** Локаль: венгерский. */
    public static final Locale LOCALE_HU = Locale.forLanguageTag("hu");
    /** Локаль: английский (Австралия). */
    public static final Locale LOCALE_EN_AU = Locale.forLanguageTag("en-AU");

    /** Список локалей, доступных в селекторе языка. */
    public static final List<Locale> SUPPORTED = List.of(LOCALE_RU, LOCALE_MK, LOCALE_HU, LOCALE_EN_AU);

    private static final Map<Locale, Map<String, String>> MESSAGES = new HashMap<>();

    static {
        put(LOCALE_RU,
                "app.title", "жаба 8",
                "auth.title", "жаба 8",
                "auth.host", "Хост",
                "auth.port", "Порт",
                "auth.username", "Имя пользователя",
                "auth.password", "Пароль",
                "auth.login", "Войти",
                "auth.register", "Регистрация",
                "auth.connect", "Подключиться",
                "main.title", "жаба 8",
                "main.user", "Пользователь",
                "main.language", "Язык",
                "main.filter", "Фильтр",
                "main.filter.column", "Столбец",
                "main.sort.column", "Сортировка",
                "main.sort.asc", "По возрастанию",
                "main.sort.reset", "Сбросить сортировку",
                "main.sort.by", "Сортировка:",
                "main.sort.then", "затем по",
                "main.sort.none", "Сортировка: не задана",
                "main.refresh", "Обновить",
                "main.delete", "Удалить",
                "main.edit", "Редактировать",
                "main.add", "Добавить",
                "main.canvas", "Визуализация",
                "main.log", "Журнал",
                "prompt.insert.key", "Ключ",
                "prompt.update.id", "ID группы для обновления",
                "prompt.remove_key", "Ключ для удаления",
                "prompt.replace.key", "Ключ элемента",
                "prompt.count.participants", "Участники",
                "prompt.filter.label", "label",
                "prompt.clear.confirm", "Удалить все ваши элементы из коллекции?",
                "dialog.run", "Выполнить",
                "dialog.wait", "Ожидание ответа сервера…",
                "error.not_owner", "Доступно только владельцу объекта",
                "error.invalid_id", "Некорректный ID",
                "error.invalid_key", "Некорректный ключ",
                "col.row", "№",
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
                "cmd.help", "справка",
                "cmd.info", "информация",
                "cmd.show", "показать",
                "cmd.insert", "вставить",
                "cmd.update", "обновить",
                "cmd.remove_key", "удалить_ключ",
                "cmd.clear", "очистить",
                "cmd.execute_script", "выполнить_скрипт",
                "cmd.remove_lower", "удалить_ниже",
                "cmd.replace_if_greater", "заменить_если_больше",
                "cmd.replace_if_lower", "заменить_если_меньше",
                "cmd.count_by_number_of_participants", "количество_по_участникам",
                "cmd.filter_less_then_label", "фильтр_меньше_label",
                "cmd.print_field_descending_label", "печать_label_по_убыванию",
                "band.dialog.add", "Новая группа",
                "band.dialog.edit", "Редактирование",
                "band.name", "Имя",
                "band.key", "Ключ",
                "band.save", "Сохранить",
                "band.cancel", "Отмена",
                "error.title", "Ошибка",
                "error.network", "Сервер недоступен",
                "error.empty", "Поле не может быть пустым",
                "error.fix_fields", "Исправьте выделенные поля",
                "error.coord.empty", "Координата не может быть пустой",
                "error.coord.integer", "Координата должна быть целым числом",
                "error.coord.number", "Координата должна быть числом",
                "error.x.max", "X должно быть не больше 254",
                "error.y.max", "Y должно быть не больше 93",
                "error.number.empty", "Число не может быть пустым",
                "error.number.positive", "Число должно быть больше 0",
                "error.number.format", "Введите целое число",
                "error.invalid_label", "Некорректное значение label",
                "error.invalid_genre", "Неверный жанр",
                "exit", "Выход"
        );
        put(LOCALE_MK,
                "app.title", "жаба 8",
                "auth.title", "жаба 8",
                "auth.host", "Домаќин",
                "auth.port", "Порта",
                "auth.username", "Корисничко име",
                "auth.password", "Лозинка",
                "auth.login", "Најави се",
                "auth.register", "Регистрација",
                "auth.connect", "Поврзи се",
                "main.title", "жаба 8",
                "main.user", "Корисник",
                "main.language", "Јазик",
                "main.filter", "Филтер",
                "main.filter.column", "Колона",
                "main.sort.column", "Подредување",
                "main.sort.asc", "Растечки",
                "main.sort.reset", "Ресетирај подредување",
                "main.sort.by", "Подредување:",
                "main.sort.then", "потоа по",
                "main.sort.none", "Подредување: не е зададена",
                "main.refresh", "Освежи",
                "main.delete", "Избриши",
                "main.edit", "Уреди",
                "main.add", "Додади",
                "main.canvas", "Визуелизација",
                "main.log", "Дневник",
                "prompt.insert.key", "Клуч",
                "prompt.update.id", "ID на групата",
                "prompt.remove_key", "Клуч за бришење",
                "prompt.replace.key", "Клуч на елементот",
                "prompt.count.participants", "Број на учесници",
                "prompt.filter.label", "label",
                "prompt.clear.confirm", "Да се избришат сите ваши елементи?",
                "dialog.run", "Изврши",
                "dialog.wait", "Се чека одговор од серверот…",
                "error.not_owner", "Само за сопственикот",
                "error.invalid_id", "Невалиден ID",
                "error.invalid_key", "Невалиден клуч",
                "col.row", "№",
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
                "cmd.help", "помош",
                "cmd.info", "инфо",
                "cmd.show", "прикажи",
                "cmd.insert", "вметни",
                "cmd.update", "ажурирај",
                "cmd.remove_key", "избриши_клуч",
                "cmd.clear", "исчисти",
                "cmd.execute_script", "изврши_скрипта",
                "cmd.remove_lower", "отстрани_помали",
                "cmd.replace_if_greater", "замени_ако_поголемо",
                "cmd.replace_if_lower", "замени_ако_помало",
                "cmd.count_by_number_of_participants", "број_по_учесници",
                "cmd.filter_less_then_label", "филтер_помало_label",
                "cmd.print_field_descending_label", "печати_label_опаѓачки",
                "band.dialog.add", "Нова група",
                "band.dialog.edit", "Уредување",
                "band.name", "Име",
                "band.key", "Клуч",
                "band.save", "Зачувај",
                "band.cancel", "Откажи",
                "error.title", "Грешка",
                "error.network", "Серверот е недостапен",
                "error.empty", "Полето не смее да е празно",
                "error.fix_fields", "Поправете ги означените полиња",
                "error.coord.empty", "Координатата не смее да е празна",
                "error.coord.integer", "Координатата мора да е цел број",
                "error.coord.number", "Координатата мора да е број",
                "error.x.max", "X не смее да е поголемо од 254",
                "error.y.max", "Y не смее да е поголемо од 93",
                "error.number.empty", "Бројот не смее да е празен",
                "error.number.positive", "Бројот мора да е поголем од 0",
                "error.number.format", "Внесете цел број",
                "error.invalid_label", "Невалидна вредност за label",
                "error.invalid_genre", "Невалиден жанр",
                "exit", "Излез"
        );
        put(LOCALE_HU,
                "app.title", "жаба 8",
                "auth.title", "жаба 8",
                "auth.host", "Gazdagép",
                "auth.port", "Port",
                "auth.username", "Felhasználónév",
                "auth.password", "Jelszó",
                "auth.login", "Bejelentkezés",
                "auth.register", "Regisztráció",
                "auth.connect", "Csatlakozás",
                "main.title", "жаба 8",
                "main.user", "Felhasználó",
                "main.language", "Nyelv",
                "main.filter", "Szűrő",
                "main.filter.column", "Oszlop",
                "main.sort.column", "Rendezés",
                "main.sort.asc", "Növekvő",
                "main.sort.reset", "Rendezés törlése",
                "main.sort.by", "Rendezés:",
                "main.sort.then", "majd",
                "main.sort.none", "Rendezés: nincs",
                "main.refresh", "Frissítés",
                "main.delete", "Törlés",
                "main.edit", "Szerkesztés",
                "main.add", "Hozzáadás",
                "main.canvas", "Vizualizáció",
                "main.log", "Napló",
                "prompt.insert.key", "Kulcs",
                "prompt.update.id", "Zenekar ID",
                "prompt.remove_key", "Törlendő kulcs",
                "prompt.replace.key", "Elem kulcsa",
                "prompt.count.participants", "Résztvevők száma",
                "prompt.filter.label", "label",
                "prompt.clear.confirm", "Törölje az összes saját elemet?",
                "dialog.run", "Futtatás",
                "dialog.wait", "Várakozás a szerverre…",
                "error.not_owner", "Csak a tulajdonosnak",
                "error.invalid_id", "Érvénytelen ID",
                "error.invalid_key", "Érvénytelen kulcs",
                "col.row", "№",
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
                "cmd.help", "súgó",
                "cmd.info", "infó",
                "cmd.show", "mutat",
                "cmd.insert", "beszúr",
                "cmd.update", "frissít",
                "cmd.remove_key", "kulcs_törlése",
                "cmd.clear", "kiürít",
                "cmd.execute_script", "szkript_futtatása",
                "cmd.remove_lower", "kisebbek_törlése",
                "cmd.replace_if_greater", "csere_ha_nagyobb",
                "cmd.replace_if_lower", "csere_ha_kisebb",
                "cmd.count_by_number_of_participants", "számlálás_résztvevők_szerint",
                "cmd.filter_less_then_label", "szűrő_kisebb_label",
                "cmd.print_field_descending_label", "label_csökkenő",
                "band.dialog.add", "Új zenekar",
                "band.dialog.edit", "Szerkesztés",
                "band.name", "Név",
                "band.key", "Kulcs",
                "band.save", "Mentés",
                "band.cancel", "Mégse",
                "error.title", "Hiba",
                "error.network", "A szerver nem elérhető",
                "error.empty", "A mező nem lehet üres",
                "error.fix_fields", "Javítsa a kiemelt mezőket",
                "error.coord.empty", "A koordináta nem lehet üres",
                "error.coord.integer", "A koordinátának egész számnak kell lennie",
                "error.coord.number", "A koordinátának számnak kell lennie",
                "error.x.max", "Az X legfeljebb 254 lehet",
                "error.y.max", "Az Y legfeljebb 93 lehet",
                "error.number.empty", "A szám nem lehet üres",
                "error.number.positive", "A számnak 0-nál nagyobbnak kell lennie",
                "error.number.format", "Adjon meg egész számot",
                "error.invalid_label", "Érvénytelen label érték",
                "error.invalid_genre", "Érvénytelen műfaj",
                "exit", "Kilépés"
        );
        put(LOCALE_EN_AU,
                "app.title", "жаба 8",
                "auth.title", "жаба 8",
                "auth.host", "Host",
                "auth.port", "Port",
                "auth.username", "Username",
                "auth.password", "Password",
                "auth.login", "Sign in",
                "auth.register", "Register",
                "auth.connect", "Connect",
                "main.title", "жаба 8",
                "main.user", "User",
                "main.language", "Language",
                "main.filter", "Filter",
                "main.filter.column", "Column",
                "main.sort.column", "Sort by",
                "main.sort.asc", "Ascending",
                "main.sort.reset", "Reset sort",
                "main.sort.by", "Sort:",
                "main.sort.then", "then by",
                "main.sort.none", "Sort: none",
                "main.refresh", "Refresh",
                "main.delete", "Delete",
                "main.edit", "Edit",
                "main.add", "Add",
                "main.canvas", "Visualisation",
                "main.log", "Log",
                "prompt.insert.key", "Key",
                "prompt.update.id", "Band ID to update",
                "prompt.remove_key", "Key to remove",
                "prompt.replace.key", "Element key",
                "prompt.count.participants", "Number of participants",
                "prompt.filter.label", "label",
                "prompt.clear.confirm", "Remove all your items from the collection?",
                "dialog.run", "Run",
                "dialog.wait", "Waiting for server…",
                "error.not_owner", "Owner only",
                "error.invalid_id", "Invalid ID",
                "error.invalid_key", "Invalid key",
                "col.row", "№",
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
                "cmd.help", "help",
                "cmd.info", "info",
                "cmd.show", "show",
                "cmd.insert", "insert",
                "cmd.update", "update",
                "cmd.remove_key", "remove_key",
                "cmd.clear", "clear",
                "cmd.execute_script", "execute_script",
                "cmd.remove_lower", "remove_lower",
                "cmd.replace_if_greater", "replace_if_greater",
                "cmd.replace_if_lower", "replace_if_lower",
                "cmd.count_by_number_of_participants", "count_by_number_of_participants",
                "cmd.filter_less_then_label", "filter_less_then_label",
                "cmd.print_field_descending_label", "print_field_descending_label",
                "band.dialog.add", "New band",
                "band.dialog.edit", "Edit band",
                "band.name", "Name",
                "band.key", "Key",
                "band.save", "Save",
                "band.cancel", "Cancel",
                "error.title", "Error",
                "error.network", "Server unavailable",
                "error.empty", "Field cannot be empty",
                "error.fix_fields", "Fix the highlighted fields",
                "error.coord.empty", "Coordinate cannot be empty",
                "error.coord.integer", "Coordinate must be an integer",
                "error.coord.number", "Coordinate must be a number",
                "error.x.max", "X must be at most 254",
                "error.y.max", "Y must be at most 93",
                "error.number.empty", "Number cannot be empty",
                "error.number.positive", "Number must be greater than 0",
                "error.number.format", "Enter a whole number",
                "error.invalid_label", "Invalid label value",
                "error.invalid_genre", "Invalid genre",
                "exit", "Exit"
        );
    }

    private Locale current = LOCALE_RU;

    /** Запрещает создание экземпляров; используйте {@link #getInstance()}. */
    private Localization() {
    }

    private static final Localization INSTANCE = new Localization();

    /**
     * @return единственный экземпляр службы локализации
     */
    public static Localization getInstance() {
        return INSTANCE;
    }

    /**
     * @return текущая выбранная локаль интерфейса
     */
    public Locale getLocale() {
        return current;
    }

    /**
     * Переключает язык интерфейса, если локаль поддерживается.
     *
     * @param locale новая локаль
     */
    public void setLocale(Locale locale) {
        if (MESSAGES.containsKey(locale)) {
            current = locale;
        }
    }

    /**
     * Возвращает перевод по ключу; при отсутствии — сам ключ.
     *
     * @param key идентификатор строки (например, {@code main.title})
     * @return локализованный текст
     */
    public String get(String key) {
        return MESSAGES.getOrDefault(current, MESSAGES.get(LOCALE_RU))
                .getOrDefault(key, key);
    }

    /**
     * Подпись команды для кнопок и меню.
     *
     * @param type тип команды сервера
     * @return локализованное имя команды
     */
    public String commandLabel(CommandType type) {
        return get("cmd." + type.getCommandName());
    }

    /**
     * Человекочитаемое название локали для выпадающего списка языков.
     *
     * @param locale локаль
     * @return отображаемое имя языка
     */
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
