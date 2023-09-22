import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBListFrame extends JFrame {
    private List<JTextField> textFields = new ArrayList<>();
    private List<JComboBox<String>> foreignKeyComboBoxes = new ArrayList<>(); // Lista per le JComboBox delle foreign key

    private JPanel databasePanel;
    private JPanel tablePanel;
    private HashMap<Integer, String> databaseList;
    private JTextArea resultTextArea;

    public DBListFrame(HashMap<Integer, String> databaseList) {
        this.databaseList = databaseList;
        setTitle("Elenco Database");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        databasePanel = new JPanel();
        databasePanel.setLayout(new GridLayout(3, 1, 45, 10));
        tablePanel = new JPanel(new GridLayout(7,2,100,10));

        for (int i = 1; i <= databaseList.size(); i++) {
            String dbName = databaseList.get(i);
            JButton dbButton = new JButton(dbName);
            dbButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            dbButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleDatabaseButtonClick(dbName);
                }
            });
            databasePanel.add(dbButton);
        }

        add(databasePanel, BorderLayout.WEST);
        add(new JScrollPane(tablePanel), BorderLayout.CENTER);
    }

    private void handleDatabaseButtonClick(String dbName) {
        Integer dbKey = getKeyByValue(databaseList, dbName);
        System.out.println("Hai selezionato il database: " + dbName);
        DatabaseHandler.instance.ConnectDB(dbKey);
        createTableButtons();
    }

    private void createTableButtons() {
        if (DatabaseHandler.instance.getCurrentDB() != null) {
            try {
                ResultSet tables = DatabaseHandler.instance.getMetaData().getTables(DatabaseHandler.instance.getCurrentDB(), null, null, new String[]{"TABLE"});

                System.out.println("Elenco delle tabelle nel database " + DatabaseHandler.instance.getCurrentDB() + ":");

                databasePanel.removeAll();
                databasePanel.revalidate();
                databasePanel.repaint();

                tablePanel.removeAll();
                int i = 0;
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    i++;
                    System.out.println("[" + i + "] " + tableName);
                    JButton tableButton = new JButton("[" + i + "] " + tableName);
                    tableButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            DatabaseHandler.instance.setCurrentRow(tableName);
                            setupTableActions();
                        }
                    });
                    tablePanel.add(tableButton);
                }

                tablePanel.revalidate();
                tablePanel.repaint();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nessun database selezionato. Esegui ConnectDB() per selezionare un database.");
        }
    }


    private void setupTableActions() {
        tablePanel.removeAll();
        //TODO Select Button
        JButton selectButton = new JButton("Select * from " + DatabaseHandler.instance.getCurrentRow());
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResultSet resultSet = App.Instance.Operazioni.SelectFromTable();
                displayQueryResults(resultSet);
            }
        });
        //TODO Insert Button
        JButton insertButton = new JButton("Insert on " + DatabaseHandler.instance.getCurrentRow());
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayInsertForm();
            }
        });

        JButton removeButton = new JButton("Remove from " + DatabaseHandler.instance.getCurrentRow());
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Implementazione Remove Button
            }
        });

        tablePanel.add(selectButton);
        tablePanel.add(insertButton);
        tablePanel.add(removeButton);
        Repaint();
    }

    public void Repaint()
    {
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private void displayQueryResults(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                DefaultTableModel tableModel = new DefaultTableModel();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    tableModel.addColumn(columnName);
                }

                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        String columnValue = resultSet.getString(i);
                        rowData[i - 1] = columnValue;
                    }
                    tableModel.addRow(rowData);
                }

                setResizable(false);
                tablePanel.removeAll();
                Repaint();

                JTable table = new JTable(tableModel);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    table.getColumnModel().getColumn(i).setCellRenderer((TableCellRenderer) new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                            setHorizontalAlignment(SwingConstants.CENTER);
                            TableColumnModel columnModel = table.getColumnModel();
                            int columnWidth = columnModel.getColumn(column).getWidth();
                            Font currentFont = getFont();
                            int fontSize = currentFont.getSize();
                            int maxFontSize = (int) (columnWidth * 0.5);
                            setFont(new Font(currentFont.getName(), currentFont.getStyle(), Math.min(fontSize, maxFontSize)));
                            return cellComponent;
                        }
                    });
                }

                tablePanel.add(new JScrollPane(table));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            tablePanel.removeAll();
            Repaint();
            resultTextArea.setText("Nessun risultato.");
        }
    }
    public boolean isForeignKeyColumn(String columnName) {
        try {
            DatabaseMetaData metaData = DatabaseHandler.instance.getConn().getMetaData();
            ResultSet foreignKeys = metaData.getImportedKeys(null, null, DatabaseHandler.instance.currentRow);
    
            while (foreignKeys.next()) {
                String foreignKeyColumnName = foreignKeys.getString("FKCOLUMN_NAME");
    
                if (columnName.equals(foreignKeyColumnName)) {
                    // La colonna è una chiave esterna
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gestisci eccezione in modo appropriato
        }
        return false;
    }
    public void displayInsertForm() {
        tablePanel.removeAll();
        Repaint();
        DatabaseMetaData dbd = (DatabaseMetaData) DatabaseHandler.instance.getMetaData();
        ResultSet rs;
        try {
            rs = dbd.getColumns(null, null, DatabaseHandler.instance.getCurrentRow(), null);

            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");

                boolean isForeignKey = isForeignKeyColumn(columnName); // Dovresti implementare questa funzione

                if (!isForeignKey) {
                    JLabel label = new JLabel(columnName);
                    JTextField textField = new JTextField(20);
                    textFields.add(textField);
            
                    JPanel panel = new JPanel(new FlowLayout());
                    panel.add(label);
                    panel.add(textField);
            
                    tablePanel.add(panel);
                }
            
            }

            // Aggiungi il codice per cercare e visualizzare le foreign key
            ResultSet foreignKeys = dbd.getImportedKeys(null, null, DatabaseHandler.instance.getCurrentRow());
            while (foreignKeys.next()) {
                String columnName = foreignKeys.getString("FKCOLUMN_NAME");
                String referencedTable = foreignKeys.getString("PKTABLE_NAME");
                String referencedColumnName = foreignKeys.getString("PKCOLUMN_NAME");

                JLabel label = new JLabel(columnName);
                JComboBox<String> foreignKeyComboBox = new JComboBox<>();

                // Popola la JComboBox con i valori della foreign key dalla tabella di riferimento
                String query = "SELECT " + referencedColumnName + " FROM " + referencedTable;
                PreparedStatement preparedStatement = DatabaseHandler.instance.getConn().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    foreignKeyComboBox.addItem(resultSet.getString(referencedColumnName));
                }
                foreignKeyComboBoxes.add(foreignKeyComboBox); // Aggiungi la JComboBox alla lista delle foreign key

                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                panel.add(label);
                panel.add(foreignKeyComboBox);
       

                tablePanel.add(panel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JButton insertButton = new JButton("Inserisci");
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String insertQuery = "INSERT INTO " + DatabaseHandler.instance.getCurrentRow() + " VALUES (";
                    // Aggiungi i valori dei campi di input
                    for (int i = 0; i < textFields.size(); i++) {
                        insertQuery += "?";
                        if (i < textFields.size() - 1) {
                            insertQuery += ",";
                        }
                    }
                    // Aggiungi i valori delle foreign key dalla JComboBox
                    for (int i = 0; i < foreignKeyComboBoxes.size(); i++) {
                        insertQuery += "?";
                        if (i < foreignKeyComboBoxes.size() - 1) {
                            insertQuery += ",";
                        }
                    }
                    insertQuery += ")";

                    PreparedStatement statement = DatabaseHandler.instance.getConn().prepareStatement(insertQuery);

                    // Imposta i valori dei campi di input
                    for (int i = 0; i < textFields.size(); i++) {
                        statement.setString(i + 1, textFields.get(i).getText());
                    }
                    // Imposta i valori delle foreign key dalla JComboBox
                    int startIndex = textFields.size() + 1;
                    for (int i = 0; i < foreignKeyComboBoxes.size(); i++) {
                        statement.setString(startIndex + i, foreignKeyComboBoxes.get(i).getSelectedItem().toString());
                    }

                    App.Instance.Operazioni.InsertOnTable(statement);
                    JOptionPane.showMessageDialog(null, "Dati inseriti con successo!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore!");
                }
            }
        });
        tablePanel.add(insertButton);
        Repaint();
    }

    private Integer getKeyByValue(HashMap<Integer, String> map, String value) {
        for (Integer key : map.keySet()) {
            if (map.get(key).equals(value)) {
                return key;
            }
        }
        return null;
    }
}
