package rosAtom.jdbc.jdbc.dao;

import rosAtom.jdbc.jdbc.model.User;
import rosAtom.jdbc.jdbc.model.UserPage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao implements Dao<User>{

    private static final String URL = "jdbc:postgresql://localhost:5432/jdbc";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection;

    static {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void create (User user) {
        String query = "INSERT INTO users (name, age, cash, is_woman) VALUES (?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setDouble(3, user.getCash());
            preparedStatement.setBoolean(4, user.isWoman());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Long id, User user) {
        String query = "UPDATE users SET name=?, age=?, cash=?, is_woman=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setDouble(3, user.getCash());
            preparedStatement.setBoolean(4, user.isWoman());
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(Long id) {
        String query = "DELETE FROM users where id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public User findById(Long id) {
        String query = "SELECT * FROM users WHERE id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return toUser(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<User> findAllPaginationFilter(UserPage userPage){
        List<User> filteredUser = new ArrayList<>();
        String query = getFindAllFilteredPaginated(userPage, userPage.getCriteria());
        List<Object> values = userPage.getParams();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 1;
            for (Object value : values) {
                statement.setObject(i++, value);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = User.builder()
                            .name(resultSet.getString("name"))
                            .age(resultSet.getInt("age"))
                            .cash(resultSet.getDouble("cash"))
                            .isWoman(resultSet.getBoolean("is_woman"))
                            .build();
                    user.setId(resultSet.getLong("id"));
                    filteredUser.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return filteredUser;
    }

    private String getFindAllFilteredPaginated(UserPage personRequest, Map<String, Object> criteria) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        if (personRequest.isPageable()) {
            sb.append("(SELECT *, ROW_NUMBER() OVER (ORDER BY id) FROM person) x");
        }
        if (personRequest.isFilter() || personRequest.isPageable()) {
            sb.append(" WHERE ");
        }
        if (personRequest.isFilter()) {
            for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                if (sb.length() > 30) {
                    sb.append(" AND ");
                }
                sb.append(entry.getKey()).append(" = ?");
            }
        }
        if (personRequest.isPageable()) {
            sb.append("ROW_NUMBER BETWEEN ? AND ?");
        }
        return sb.toString();
    }

    private User toUser(ResultSet res) throws SQLException {
        return new User(
                res.getLong("id"),
                res.getString("name"),
                res.getInt("age"),
                res.getDouble("cash"),
                res.getBoolean("is_woman")
        );
    }
}
