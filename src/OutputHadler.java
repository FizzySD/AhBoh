import java.util.LinkedList;

import javax.xml.crypto.Data;

import java.util.*;;
public class OutputHadler 
{
    public static OutputHadler instance;
    LinkedList<String> ElencoInput = new LinkedList<>();
    LinkedList<String> ElencoOperazioni = new LinkedList<>();
    
    private int DBSelection = 2;

    int Scelta;
    public OutputHadler()
    {
        instance = this;
        ElencoInput.add("1 - Select Database \n");
        ElencoInput.add("2 - Delete Database \n");     
        ElencoInput.add("3 - Export Database \n");
        ElencoInput.add("4 - Close Connection \n");

        ElencoOperazioni.add("1 - Select From Table \n");
        ElencoOperazioni.add("1 - Insert On Table \n");
        ElencoOperazioni.add("1 - Remove From Table \n");
    }

    public void UpdateOutput(String RowName)
    {
        ElencoOperazioni.set(0,"1 - Select From " + RowName+"\n");
        ElencoOperazioni.set(1,"2 - Insert On " + RowName +"\n");
        ElencoOperazioni.set(2,"3 - Remove From " + RowName+"\n");
    }

    public static void ClearConsole()
    {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    public void ShowOutput()
    {
        ClearConsole();
        for(int i = 0; i<ElencoInput.size();i++)
        {
            System.out.print(ElencoInput.get(i));
        }
        Scelta = InputHandler.instance.GetInput(InputType.Number);
        if(Scelta == 1)
        {
            DatabaseHandler.instance.showDBList();
            ShowTables();
        }
    }

    public void ShowTables()
    {

        int dbScelto = InputHandler.instance.GetInput(InputType.Number);
        DatabaseHandler.instance.ConnectDB(dbScelto);
        ClearConsole();
        DatabaseHandler.instance.showTables();
        DatabaseHandler.instance.SelectRow(InputHandler.instance.GetInput(InputType.Number));
        ShowOperations();
        
    }

    public void ShowOperations()
    {
        ClearConsole();
        for(int i = 0; i<ElencoOperazioni.size();i++)
        {
            System.out.print(ElencoOperazioni.get(i));
        }
        Scelta = InputHandler.instance.GetInput(InputType.Number);
        switch (Scelta) {
            case 1:
                App.Instance.Operazioni.SelectFromTable();
                //DatabaseHandler.Operazioni.SelectFromTable();
                break;
            case 2:
                System.out.print("Quale campo vuoi inserire\n");
                String Campo = InputHandler.instance.GetInput(InputType.Object);
                ClearConsole();
                System.out.print("Quale sarà il valore\n");
                String Valore = InputHandler.instance.GetInput(InputType.Object);
                App.Instance.Operazioni.InsertOnTable(Campo, Valore);
                break;
            case 3:
                //DatabaseHandler.Operazioni.RemoveFromTable();
                break;
            default:
                break;
        }
    }

}
