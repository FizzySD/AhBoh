import java.sql.*;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class DatabaseHandler 
{
    public static DatabaseHandler instance = null; 
    //#region Database Settings
    static final String DB_URL = "jdbc:mysql://localhost";
    static final String USER = "root";
    static final String PASS = "";
    private boolean DebugMode = true;
    //#endregion
    //#region Database Objects
    public Statement stmt;
    private Connection conn;
    public DatabaseMetaData metaData;
    //#endregion
    String currentDB;
    String currentRow;
    HashMap<Integer, String> databaseList = new HashMap<Integer, String>();
    HashMap<Integer, String> rowList = new HashMap<Integer, String>();

    public DatabaseHandler()
    {
        instance = this;
        GenericConnection();
    }

    public String getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(String currentRow) {
        this.currentRow = currentRow;
    }
    public void GenericConnection() 
    {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            metaData = conn.getMetaData();
            if (DebugMode) {
                System.out.println("DB connesso");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("DB non connesso");
        }
    }
    public Connection getConn() {
        return conn;
    }
    public void ConnectDB(int Scelta) {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        String dbName = databaseList.get(Scelta);
        if (dbName != null) {
            setCurrentDatabase(dbName); // Set the current database
    
            String dbUrl = DB_URL + "/" + dbName;
            try {
                this.conn = DriverManager.getConnection(dbUrl, USER, PASS);
                this.stmt = this.conn.createStatement();
                this.metaData = this.conn.getMetaData();
                if (DebugMode) {
                    System.out.println("Connesso a " + dbName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Errore nella connessione al database " + dbName);
            }
        } else {
            System.out.println("Database con indice " + Scelta + " non trovato nell'elenco.");
        }
    }
    
    
    public void setCurrentDatabase(String dbName) {
        currentDB = dbName;
    }
    

    public void ExecuteQuery(QueryTypes Type, PreparedStatement Statement) {
        if(Type == QueryTypes.Select)
        {
            //TODO Implement in a better way
        }
        else if (Type == QueryTypes.Insert) {
            try {
                Statement.executeUpdate();
                Statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

    public void SelectRow(int Scelta) 
    {
        //OutputHadler.instance.ClearConsole();
        currentRow = rowList.get(Scelta);
        //OutputHadler.instance.UpdateOutput(currentRow);
    }

    public String getCurrentDB() {
        return currentDB;
    }
    public DatabaseMetaData getMetaData() {
        return metaData;
    }
    public void showTables() {
        if (currentDB != null) {
            try {
                ResultSet tables = metaData.getTables(currentDB, null, null, new String[] { "TABLE" });
        
                System.out.println("Elenco delle tabelle nel database " + currentDB + ":");
                int i = 0;
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    i++;
                    System.out.println("[" + i + "] " + tableName);
                    rowList.put(i, tableName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nessun database selezionato. Esegui ConnectDB() per selezionare un database.");
        }
    }

    
    
    
    

    public void showDBList() {
        int i = 0;
        try {
            ResultSet resultSet = metaData.getCatalogs();
    
            System.out.println("Elenco delle tabelle nel database:");
            while (resultSet.next()) {
                i++;
                String tableName = resultSet.getString("TABLE_CAT"); // 3 rappresenta la colonna del nome della tabella
                System.out.println("[" + i + "]" + " " + tableName);
                databaseList.put(i, tableName);
            }
    
            // Creare e visualizzare la finestra DBListFrame
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JFrame dbListFrame = new DBListFrame(databaseList);
                    dbListFrame.setVisible(true);
                }
            });
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}