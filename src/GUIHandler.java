import javax.swing.*;  
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;

public class GUIHandler 
{
    public static GUIHandler instance;
    public static JFrame f =new JFrame();

    public GUIHandler()
    {
        instance = this;
    }

    public void Init()
    {
    Dimension dimensioniSchermo = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (dimensioniSchermo.width - 800) / 2;
    int y = (dimensioniSchermo.height - 500) / 2;
    
    ImageIcon background=new ImageIcon("C:\\Users\\marzi\\Documents\\JavaEHT\\AhBoh\\DBMS.png");
    Image img=background.getImage();
    Image temp=img.getScaledInstance(800,500,Image.SCALE_SMOOTH);
    background=new ImageIcon(temp);
    JLabel BackGround=new JLabel(background);



    JButton button1 = new JButton("Select DB");
    JButton button2 = new JButton("Delete DB");
    JButton button3 = new JButton("Export DB");
    JButton button4 = new JButton("Close Connection");

    button1.setBounds(100, 50, 150, 60);
    button2.setBounds(500, 50, 150, 60);
    button3.setBounds(100, 350, 150, 60);
    button4.setBounds(500, 350, 150, 60);

    button1.addActionListener(new ActionListener() 
    {
        public void actionPerformed(ActionEvent e) 
        {
            DatabaseHandler.instance.showDBList();
        }
    });

    button2.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // Code to execute when Button 2 is clicked
            // You can add your logic here
        }
    });

    button3.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // Code to execute when Button 3 is clicked
            // You can add your logic here
        }
    });

    button4.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });

    f.add(button1);
    f.add(button2);
    f.add(button3);
    f.add(button4);
    BackGround.setLayout(null);
    f.add(BackGround);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    BackGround.setBounds(0,0,800,500);
    f.setSize(800,500);//400 width and 500 height  
    f.setLocation(x, y);
    f.setResizable(false);
    f.setLayout(null);//using no layout managers  
    f.setVisible(true);//making the frame visible  
    }
}
