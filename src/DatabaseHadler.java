import java.sql.*;

public class DatabaseHadler 
{
    public static DatabaseHadler instance = null; 
    static final String DB_URL =g "jdbc:mysql://localhost/aottg_maps";
    static final String USER = "root";
    static final String PASS = "";
    public Statement stmt;
    //static final String QUERY = "SELECT id, first, last, age FROM Employees";
    
    public DatabaseHadler()
    {
        instance = this;
        DBConnect();
    }

    public void DBConnect()
    {
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn.createStatement();)
        {
        this.stmt = stmt;
        System.out.println("DB connesso");
        ExecuteQuery("SELECT MapName FROM maps WHERE ID = 2");
        }catch (SQLException e)
        {
        e.printStackTrace();
        System.out.println("DB non connesso");
        } 
    }

    public void ExecuteQuery(String Query)
    {
        try
        {
            ResultSet rs = stmt.executeQuery(Query);
            while(rs.next())
            {
                System.out.println("Mappa Scelta: " + rs.getString("MapName"));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}