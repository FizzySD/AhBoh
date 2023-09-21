import java.security.PublicKey;
import java.sql.*;


public class App 
{
    public static App Instance = null;
    public static DatabaseHandler db = new DatabaseHandler();
    public static DatabaseOperations Operazioni = new DatabaseOperations();
    public static OutputHadler OutputHadler = new OutputHadler();
    public static InputHandler Input = new InputHandler();
    static int Scelta;

    public App()
    {
        Instance = this;
    }
    public static void main(String[] args) throws Exception 
    {
        OutputHadler.ShowOutput();
    }
}
