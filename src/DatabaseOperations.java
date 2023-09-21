public class DatabaseOperations extends DatabaseHandler
{
    public DatabaseOperations()
    {
        super();
    }
    public void SelectFromTable() {
        // Check if a database is selected
        if (super.instance.currentDB != null) {
            super.ExecuteQuery("SELECT * FROM " + super.instance.currentRow,QueryTypes.Select);
        }
    }
    
    public void InsertOnTable(String Campo, String Valore)
    {
        if (super.instance.currentDB != null) {
            super.ExecuteQuery("INSERT INTO " + super.instance.currentRow + " (" + Campo + ") VALUES ('" + Valore + "')", QueryTypes.Insert);
        }
    }

    public void RemoveFromTable()
    {
        
    }

}
