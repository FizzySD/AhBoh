import java.util.Scanner;

public class InputHandler 
{
    public Scanner inputScanner = new Scanner(System.in);
    public static Boolean scelta = false;
    public static InputHandler instance;

    public InputHandler()
    {
        instance = this;
    }
    
    public int GetInput(InputType.Number)
    {
        return inputScanner.nextInt();
    }

}
