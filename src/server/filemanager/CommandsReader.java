package server.filemanager;

import server.data.ClassesManager;
import server.data.DataCommands;
import common.Response;
import common.dataclasses.CommandType;
import common.dataclasses.MusicBand;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Чтение и выполнение команд из файла скрипта с проверкой рекурсии execute_script.
 */
public class CommandsReader {

    private static CommandsReader instance;
    private static final ArrayList<String> filesNames = new ArrayList<>();

    private CommandsReader() {
    }

    /**
     * Инициализирует читатель команд (выполняется один раз).
     *
     * @throws IllegalStateException при повторной инициализации
     */
    public static void initialize() {
        if (instance == null) {
            instance = new CommandsReader();
        } else {
            throw new IllegalStateException("CommandsReader has already been initialized");
        }
    }

    /**
     * Возвращает единственный экземпляр читателя команд.
     *
     * @return инициализированный {@link CommandsReader}
     * @throws IllegalStateException если читатель не инициализирован
     */
    public static CommandsReader getInstance() {
        if (instance == null) {
            throw new IllegalStateException("CommandsReader has not been initialized");
        }
        return instance;
    }

    /**
     * Читает и выполняет команды из файла для клиента с идентификатором 1.
     *
     * @param fileName путь к файлу скрипта
     * @return журнал выполнения команд
     */
    public StringBuilder readCommands(String fileName) {
        return readCommands(fileName, 1);
    }

    /**
     * Читает и выполняет команды из файла от имени указанного клиента.
     *
     * @param fileName путь к файлу скрипта
     * @param clientId идентификатор клиента
     * @return журнал выполнения команд
     */
    public StringBuilder readCommands(String fileName, int clientId) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean firstCommandBlock = true;

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName.trim()), StandardCharsets.UTF_8))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = normalizeLine(line);
                if (isCommentOrEmpty(line)) {
                    continue;
                }

                String[] commands = splitCommand(line);
                if (commands.length == 0) {
                    continue;
                }

                checkRecursion(line);

                try {
                    MusicBand musicBand = checkIfBandsInput(bufferedReader, commands);
                    CommandType type = CommandType.fromName(commands[0]);
                    if (type == null) {
                        if (!firstCommandBlock) {
                            stringBuilder.append('\n');
                        }
                        firstCommandBlock = false;
                        stringBuilder.append("Unknown command: ").append(line).append('\n');
                        continue;
                    }

                    Response response = runCommand(type, commands, musicBand, clientId);
                    if (!firstCommandBlock) {
                        stringBuilder.append('\n');
                    }
                    firstCommandBlock = false;
                    appendResponse(stringBuilder, response);
                    stringBuilder.append("Command ").append(line).append(" processed\n");

                } catch (Exception e) {
                    String detail = e.getMessage();
                    if (detail == null || detail.isBlank()) {
                        detail = e.getClass().getSimpleName();
                    }
                    if (!firstCommandBlock) {
                        stringBuilder.append('\n');
                    }
                    firstCommandBlock = false;
                    stringBuilder.append("Error at line \"").append(line).append("\": ")
                            .append(detail).append('\n');
                }
            }
            return stringBuilder;

        } catch (IOException e) {
            throw new RuntimeException("There is no file with this name: " + fileName.trim());
        }
    }

    private static String normalizeLine(String line) {
        if (line == null) {
            return "";
        }
        if (!line.isEmpty() && line.charAt(0) == '\ufeff') {
            line = line.substring(1);
        }
        return line.trim();
    }

    private static boolean isCommentOrEmpty(String line) {
        return line.isEmpty() || line.startsWith("#");
    }

    private static String[] splitCommand(String line) {
        return Arrays.stream(line.split("\\s+"))
                .filter(part -> !part.isEmpty())
                .toArray(String[]::new);
    }

    private static void appendResponse(StringBuilder out, Response response) {
        if (response == null) {
            return;
        }
        if (response.getMessage() != null && !response.getMessage().isBlank()) {
            out.append(response.getMessage()).append('\n');
        }
        if (response.getData() != null && response.getData().length() > 0) {
            out.append(response.getData());
            if (response.getData().charAt(response.getData().length() - 1) != '\n') {
                out.append('\n');
            }
        }
    }

    private static Response runCommand(CommandType type, String[] parts, MusicBand musicBand, int clientId) {
        String argument = parts.length > 1 ? parts[1] : null;

        if (type.requiresArgument() && type.requiresMusicBand()) {
            if (musicBand == null) {
                throw new IllegalArgumentException("MusicBand fields must follow this command in the script file");
            }
            return DataCommands.getInstance().createCommand(type, argument, musicBand, clientId);
        }
        if (type.requiresMusicBand()) {
            if (musicBand == null) {
                throw new IllegalArgumentException("MusicBand fields must follow this command in the script file");
            }
            return DataCommands.getInstance().createCommand(type, null, musicBand, clientId);
        }
        if (type.requiresArgument()) {
            return DataCommands.getInstance().createCommand(type, argument, null, clientId);
        }
        return DataCommands.getInstance().createCommand(type, null, null, clientId);
    }

    private void checkRecursion(String line) {
        String[] command = line.toLowerCase().split("\\s+");
        if (command.length == 0) {
            return;
        }

        if (Objects.equals(command[0], "execute_script")) {
            if (command.length < 2) {
                throw new IllegalArgumentException("execute_script requires a file path");
            }
            if (filesNames.contains(command[1])) {
                throw new RuntimeException("A recursion was detected. Executing the file " + command[1]
                        + " will cause the program to loop.");
            }
            filesNames.add(command[1]);
        }
    }

    /**
     * Сбрасывает список уже обрабатываемых файлов скриптов (после execute_script).
     */
    public void resetCommand() {
        filesNames.clear();
    }

    private MusicBand checkIfBandsInput(BufferedReader reader, String[] command) {
        if (command.length == 0) {
            return null;
        }
        String name = command[0].toLowerCase();

        if (Objects.equals(name, "remove_lower") && command.length == 1) {
            return BandsFileReader.getInstance().inputBand(reader);
        }
        if (Objects.equals(name, "update") && command.length == 2) {
            return BandsFileReader.getInstance().inputBand(Integer.parseInt(command[1]), reader);
        }
        if ((Objects.equals(name, "insert")
                || Objects.equals(name, "replace_if_greater")
                || Objects.equals(name, "replace_if_lower"))
                && command.length == 2) {
            return BandsFileReader.getInstance().inputBand(reader);
        }
        return null;
    }
}
