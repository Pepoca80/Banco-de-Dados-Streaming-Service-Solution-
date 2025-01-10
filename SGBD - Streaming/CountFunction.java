import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountFunction {
    // Método genérico para contar registros em uma tabela
    public static int countRecords(String tableName) {
        int total = 0;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT COUNT(*) as TOTAL FROM " + tableName;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                total = resultSet.getInt("TOTAL");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar registros na tabela " + tableName + ": " + e.getMessage());
        }
        return total;
    }
}
