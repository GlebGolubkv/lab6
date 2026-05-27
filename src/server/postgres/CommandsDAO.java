package server.postgres;

import common.Response;
import common.dataclasses.*;
import server.data.DataCommands;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Доступ к данным PostgreSQL: музыкальные группы, пользователи и связанные операции.
 */
public class CommandsDAO {

    /**
     * Вставляет музыкальную группу в БД и присваивает ей сгенерированный {@code id}.
     *
     * @param bandKey   ключ элемента в коллекции
     * @param musicBand объект группы
     * @param ownerId   идентификатор владельца
     * @return тот же объект с заполненным {@code id}
     */
    public static MusicBand insertMusicBand(int bandKey, MusicBand musicBand, int ownerId) {

        String sql = """
                INSERT INTO musicbands (
                    band_key, owner_id, name, coordinates_x, coordinates_y,
                    creation_date, number_of_participants, albums_count, genre, label_bands
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, bandKey);
            statement.setInt(2, ownerId);
            statement.setString(3, musicBand.getName());
            statement.setInt(4, musicBand.getCoordinates().getX());
            statement.setDouble(5, musicBand.getCoordinates().getY());
            statement.setObject(6, musicBand.getCreationDate().toOffsetDateTime());
            statement.setLong(7, musicBand.getNumberOfParticipants());
            statement.setLong(8, musicBand.getAlbumsCount());
            if (musicBand.getGenre() != null) {
                statement.setString(9, musicBand.getGenre().toString());
            } else {
                statement.setString(9, null);
            }

            statement.setInt(10, musicBand.getLabel().getBands());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    musicBand.setId(resultSet.getInt("id"));
                    return musicBand;
                } else {
                    throw new SQLException("Insert failed");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Загружает все музыкальные группы из таблицы {@code musicbands}.
     *
     * @return карта «ключ коллекции → группа»
     */
    public static Map<Integer, MusicBand> readFromPostgres() {
        Map<Integer, MusicBand> map = new HashMap<>();
        String sql = "SELECT * FROM musicbands";

        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int bandKey = resultSet.getInt("band_key");
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Coordinates coordinates = new Coordinates(resultSet.getInt("coordinates_x"), resultSet.getDouble("coordinates_y"));
                OffsetDateTime odt = resultSet.getObject("creation_date", OffsetDateTime.class);
                ZonedDateTime creationDate = odt.atZoneSameInstant(ZoneId.systemDefault());
                long numberOfParticipants = resultSet.getLong("number_of_participants");
                int albumsCount = resultSet.getInt("albums_count");

                MusicGenre genre = (resultSet.getString("genre") != null) ? MusicGenre.valueOf(resultSet.getString("genre")) : null;

                Label label = new Label(resultSet.getInt("label_bands"));
                MusicBand musicBand = new MusicBand(id, name, coordinates, creationDate, numberOfParticipants, albumsCount, genre, label);
                map.put(bandKey, musicBand);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * Обновляет группу в БД по внутреннему {@code id}, если она принадлежит владельцу.
     *
     * @param id        идентификатор записи в БД
     * @param musicBand новые данные группы
     * @param owner_id  идентификатор владельца
     * @return {@code true}, если обновлена хотя бы одна строка
     */
    public static boolean updateMusicBandById(int id, MusicBand musicBand, int owner_id) {

        if (findOwnerIdFromBandID(id) != owner_id) {
            throw new IllegalArgumentException("The given id doesn't belong to this owner.");
        }

        String sql = """
                        UPDATE musicbands
                                SET
                                    name = ?,
                                    coordinates_x = ?,
                                    coordinates_y = ?,
                                    creation_date = ?,
                                    number_of_participants = ?,
                                    albums_count = ?,
                                    genre = ?,
                                    label_bands = ?
                                WHERE id = ?;
                """;
        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, musicBand.getName());
            statement.setInt(2, musicBand.getCoordinates().getX());
            statement.setDouble(3, musicBand.getCoordinates().getY());
            statement.setObject(4, musicBand.getCreationDate().toOffsetDateTime());
            statement.setLong(5, musicBand.getNumberOfParticipants());
            statement.setLong(6, musicBand.getAlbumsCount());
            if (musicBand.getGenre() != null) {
                statement.setString(7, musicBand.getGenre().toString());
            } else {
                statement.setString(7, null);
            }
            statement.setInt(8, musicBand.getLabel().getBands());
            statement.setInt(9, id);
            int res = statement.executeUpdate();
            if (res > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Удаляет группу из БД по ключу коллекции и идентификатору владельца.
     *
     * @param key      ключ элемента
     * @param owner_id идентификатор владельца
     * @return {@code true}, если удалена хотя бы одна строка
     */
    public static boolean removeMusicBand(int key, int owner_id) {
        String sql = """
                        DELETE FROM musicbands WHERE band_key = ? AND owner_id = ?;
                """;

        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, key);
            statement.setInt(2, owner_id);

            int res = statement.executeUpdate();
            if (res > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаляет все группы владельца, вызывая команду {@code remove_key} для каждого ключа.
     *
     * @param owner_id идентификатор владельца
     * @return журнал результатов удаления
     */
    public static StringBuilder clearMusicBands(int owner_id) {

        StringBuilder string = new StringBuilder().append("Cleared music bands: \n");
        String sql = """
                 SELECT band_key FROM musicbands WHERE owner_id = ?;
                """;

        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, owner_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Response response = DataCommands.getInstance().createCommand(CommandType.REMOVE_KEY, resultSet.getString("band_key"), null, owner_id);
                string.append(response.getMessage()).append('\n');

            }
            return string;
        } catch (SQLException e) {
            return string.append(e.getMessage());
        }
    }

    /**
     * Обновляет группу в БД по ключу коллекции, если она принадлежит владельцу.
     *
     * @param key       ключ элемента
     * @param musicBand новые данные группы
     * @param owner_id  идентификатор владельца
     * @return {@code true}, если обновлена хотя бы одна строка
     */
    public static boolean updateMusicBandByKey(int key, MusicBand musicBand, int owner_id) {

        if (findOwnerIdFromBandKey(key) != owner_id) {
            throw new IllegalArgumentException("The given id doesn't belong to this owner.");
        }
        String sql = """
                        UPDATE musicbands
                                SET
                                    owner_id = ?,
                                    name = ?,
                                    coordinates_x = ?,
                                    coordinates_y = ?,
                                    creation_date = ?,
                                    number_of_participants = ?,
                                    albums_count = ?,
                                    genre = ?,
                                    label_bands = ?
                                WHERE band_key = ?;
                """;
        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, owner_id);
            statement.setString(2, musicBand.getName());
            statement.setInt(3, musicBand.getCoordinates().getX());
            statement.setDouble(4, musicBand.getCoordinates().getY());
            statement.setObject(5, musicBand.getCreationDate().toOffsetDateTime());
            statement.setLong(6, musicBand.getNumberOfParticipants());
            statement.setLong(7, musicBand.getAlbumsCount());
            if (musicBand.getGenre() != null) {
                statement.setString(8, musicBand.getGenre().toString());
            } else {
                statement.setString(8, null);
            }
            statement.setInt(9, musicBand.getLabel().getBands());
            statement.setInt(10, key);
            int res = statement.executeUpdate();
            if (res > 0) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Проверяет наличие группы с заданным ключом в БД.
     *
     * @param bandKey ключ коллекции
     * @return {@code true}, если запись существует
     */
    public static boolean bandKeyExists(int bandKey) {
        String sql = "SELECT 1 FROM musicbands WHERE band_key = ? LIMIT 1";
        try (Connection connection = ConnectionInitializer.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bandKey);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Загружает соответствие «ключ коллекции → владелец» одним запросом (для {@code get_collection}).
     *
     * @return карта {@code band_key} → {@code owner_id}
     */
    public static Map<Integer, Integer> readBandKeyOwnerMap() {
        Map<Integer, Integer> owners = new HashMap<>();
        String sql = "SELECT band_key, owner_id FROM musicbands";
        try (Connection connection = ConnectionInitializer.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                owners.put(resultSet.getInt("band_key"), resultSet.getInt("owner_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return owners;
    }

    /**
     * Возвращает идентификатор владельца по ключу элемента коллекции.
     *
     * @param key ключ коллекции
     * @return {@code owner_id} из БД
     */
    public static int findOwnerIdFromBandKey(int key) {
        String sql = """
                                 SELECT owner_id FROM musicbands WHERE band_key = ?;
                """;

        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, key);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("owner_id");
            } else throw new SQLException("Error finding owner_id from band.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает идентификатор владельца по внутреннему {@code id} записи в БД.
     *
     * @param id идентификатор музыкальной группы
     * @return {@code owner_id} из БД
     */
    public static int findOwnerIdFromBandID(int id) {
        String sql = """
                                 SELECT owner_id FROM musicbands WHERE id = ?;
                """;

        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("owner_id");
            } else throw new SQLException("Error finding owner_id from band.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверяет, зарегистрировано ли имя пользователя в таблице {@code users}.
     *
     * @param name имя пользователя
     * @return ответ с признаком существования ({@link Response#isSuccess()})
     */
    public static Response isUserExists(String name) {
        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            String sql = "SELECT EXISTS (SELECT 1 FROM users WHERE name = ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    boolean exists = resultSet.getBoolean(1);
                    return new Response(exists, "checked for availability", true);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalArgumentException("Error checking for availability");
    }

    /**
     * Регистрирует нового пользователя с паролем (хеш MD5 в БД).
     *
     * @param name     имя пользователя
     * @param password пароль в открытом виде
     * @return ответ с {@code userId} при успехе
     */
    public static Response insertUser(String name, String password) {

        String sql = "INSERT INTO users (name, password) VALUES (?, MD5(?)) RETURNING id";

        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                return new Response(true, "User inserted successfully", true, userId);
            } else return new Response(false, "User insertion failed");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверяет учётные данные и возвращает идентификатор пользователя при успешном входе.
     *
     * @param username имя пользователя
     * @param password пароль
     * @return ответ с {@code userId} при успехе
     */
    public static Response logInUser(String username, String password) {

        String sql = """
                SELECT id FROM users WHERE name = ? AND password = MD5(?)
                """;
        try (Connection connection = ConnectionInitializer.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");

                return new Response(true, "Logged in as " + username, true, id);
            } else {
                return new Response(false, "Password is incorrect");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
