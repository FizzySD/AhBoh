import java.security.PublicKey;
import java.sql.*;


public class App 
{
    public static DatabaseHadler db = new DatabaseHadler();
    public static InputHandler Input = new InputHandler();
    static int test;
    public static void main(String[] args) throws Exception 
    {
        System.out.println("Scrivi qualcosa");
        test = Input.GetInput(InputType.Number);
        System.out.println("Hai scritto " + test);
    }
}
