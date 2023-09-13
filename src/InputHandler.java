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

    //#region Metodi

    //Metodo generico per ottenere gli input

    
    public <T> T GetInput(InputType inputType) {
        switch (inputType) {
            case Number:
                    return (T) Integer.valueOf(inputScanner.nextInt());
            case Object:
                    return (T) inputScanner.nextLine();     
            default:
                return null;
        }
    }
    //#endregion
}
