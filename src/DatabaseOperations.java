import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseOperations extends DatabaseHandler {
    public DatabaseOperations() {
        super();
    }

    public ResultSet SelectFromTable() {
        if (super.instance.currentDB != null) {
            try {
                String query = "SELECT * FROM " + super.instance.currentRow;
                Statement statement = super.stmt;
                return statement.executeQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    

    public void InsertOnTable(PreparedStatement Query) {
        if (super.instance.currentDB != null) {
            super.ExecuteQuery(QueryTypes.Insert,Query);
        }
    }

    public void RemoveFromTable() {
        // Implementa la rimozione dei dati dalla tabella
    }
}
