package oop.ica.e2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;

public class SportsShopGUI extends JFrame {

    public final ArrayList<StockascItem> dataList = new ArrayList<>();
    private final String[] columnNames = {"Stock Code", "Title", "Description", "Pounds", "Pence", "Quantity"};
    private final JTable sportsTable = new JTable();
    private TableModelASC ascModel;

    private final JLabel totalProductsValue = new JLabel("0", SwingConstants.CENTER);
    private final JLabel totalUnitsValue = new JLabel("0", SwingConstants.CENTER);
    private final JLabel lowStockValue = new JLabel("0", SwingConstants.CENTER);
    private final String loggedInUser;

    public SportsShopGUI() {
        this("admin");
    }

    public SportsShopGUI(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        setTitle("Sports Shop Stock Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        loadData();
        buildUI();
        refreshTable();
        updateDashboard();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Sports Shop Inventory Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        top.add(title, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Logged in as: " + loggedInUser);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        top.add(userLabel, BorderLayout.EAST);
        root.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 0));
        cards.add(createCard("Total Products", totalProductsValue));
        cards.add(createCard("Total Stock Units", totalUnitsValue));
        cards.add(createCard("Low Stock Items (<5)", lowStockValue));
        center.add(cards, BorderLayout.NORTH);

        sportsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sportsTable.setRowHeight(24);
        sportsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        sportsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        center.add(new JScrollPane(sportsTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton addItem = new JButton("Add Item");
        JButton updateItem = new JButton("Update Item");
        JButton deleteItem = new JButton("Delete Item");
        JButton sellOne = new JButton("Sell 1");
        JButton restockOne = new JButton("Restock +1");
        JButton save = new JButton("Save");
        JButton logout = new JButton("Logout");

        addItem.addActionListener(e -> addNewItem());
        updateItem.addActionListener(e -> updateSelectedItem());
        deleteItem.addActionListener(e -> deleteSelectedItem());
        sellOne.addActionListener(e -> sellSelectedItem());
        restockOne.addActionListener(e -> restockSelectedItem());
        save.addActionListener(e -> saveData(true));
        logout.addActionListener(e -> logout());

        buttons.add(addItem);
        buttons.add(updateItem);
        buttons.add(deleteItem);
        buttons.add(sellOne);
        buttons.add(restockOne);
        buttons.add(save);
        buttons.add(logout);
        center.add(buttons, BorderLayout.SOUTH);

        root.add(center, BorderLayout.CENTER);
        setContentPane(root);
        pack();
    }

    private JPanel createCard(String label, JLabel value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        JLabel title = new JLabel(label, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        value.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(title, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    private void refreshTable() {
        ascModel = new TableModelASC(columnNames, dataList);
        sportsTable.setModel((TableModel) ascModel);
    }

    private void updateDashboard() {
        int totalUnits = 0;
        int lowStock = 0;
        for (StockascItem item : dataList) {
            totalUnits += item.getStockQty();
            if (item.getStockQty() < 5) lowStock++;
        }
        totalProductsValue.setText(String.valueOf(dataList.size()));
        totalUnitsValue.setText(String.valueOf(totalUnits));
        lowStockValue.setText(String.valueOf(lowStock));
    }

    private int selectedIndex() {
        int row = sportsTable.getSelectedRow();
        if (row < 0 || row >= dataList.size()) {
            JOptionPane.showMessageDialog(this, "Please select an item from the table.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        return row;
    }

    // CREATE
    private void addNewItem() {
        JTextField code = new JTextField();
        JTextField title = new JTextField();
        JTextField description = new JTextField();
        JTextField pounds = new JTextField();
        JTextField pence = new JTextField();
        JTextField quantity = new JTextField();

        Object[] fields = {
            "Stock Code:", code,
            "Title:", title,
            "Description:", description,
            "Price - Pounds:", pounds,
            "Price - Pence:", pence,
            "Quantity:", quantity
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            if (code.getText().trim().isEmpty() || title.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Stock code and title are required.");
            }
            int poundValue = Integer.parseInt(pounds.getText().trim());
            int penceValue = Integer.parseInt(pence.getText().trim());
            int qtyValue = Integer.parseInt(quantity.getText().trim());
            if (poundValue < 0 || penceValue < 0 || penceValue > 99 || qtyValue < 0) {
                throw new IllegalArgumentException("Please enter valid non-negative price and quantity values.");
            }
            dataList.add(new StockascItem(code.getText().trim(), title.getText().trim(),
                    description.getText().trim(), poundValue, penceValue, qtyValue, "InventoryData.csv"));
            refreshAndSave();
            JOptionPane.showMessageDialog(this, "Item added successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pounds, pence and quantity must be numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE
    private void updateSelectedItem() {
        int index = selectedIndex();
        if (index < 0) return;
        StockascItem item = dataList.get(index);

        JTextField title = new JTextField(item.getpT());
        JTextField description = new JTextField(item.getProdDescription());
        JTextField pounds = new JTextField(String.valueOf(item.getPricePound()));
        JTextField pence = new JTextField(String.valueOf(item.getPp()));
        JTextField quantity = new JTextField(String.valueOf(item.getStockQty()));

        Object[] fields = {
            "Stock Code: " + item.getPC(),
            "Title:", title,
            "Description:", description,
            "Price - Pounds:", pounds,
            "Price - Pence:", pence,
            "Quantity:", quantity
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Update Item", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            int poundValue = Integer.parseInt(pounds.getText().trim());
            int penceValue = Integer.parseInt(pence.getText().trim());
            int qtyValue = Integer.parseInt(quantity.getText().trim());
            if (poundValue < 0 || penceValue < 0 || penceValue > 99 || qtyValue < 0) {
                throw new IllegalArgumentException("Please enter valid non-negative price and quantity values.");
            }
            // Replace object to keep the model simple and preserve encapsulation through constructor values.
            dataList.set(index, new StockascItem(item.getPC(), title.getText().trim(), description.getText().trim(),
                    poundValue, penceValue, qtyValue, "InventoryData.csv"));
            refreshAndSave();
            JOptionPane.showMessageDialog(this, "Item updated successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pounds, pence and quantity must be numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE
    private void deleteSelectedItem() {
        int index = selectedIndex();
        if (index < 0) return;
        StockascItem item = dataList.get(index);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete " + item.getpT() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dataList.remove(index);
            refreshAndSave();
            JOptionPane.showMessageDialog(this, "Item deleted successfully.");
        }
    }

    private void sellSelectedItem() {
        int index = selectedIndex();
        if (index < 0) return;
        StockascItem item = dataList.get(index);
        if (item.getStockQty() <= 0) {
            JOptionPane.showMessageDialog(this, "This item is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }
        item.setStockQty(item.getStockQty() - 1);
        refreshAndSave();
        JOptionPane.showMessageDialog(this, "1 item sold. Remaining quantity: " + item.getStockQty());
    }

    private void restockSelectedItem() {
        int index = selectedIndex();
        if (index < 0) return;
        StockascItem item = dataList.get(index);
        item.setStockQty(item.getStockQty() + 1);
        refreshAndSave();
        JOptionPane.showMessageDialog(this, "Stock increased by 1. New quantity: " + item.getStockQty());
    }

    private void refreshAndSave() {
        refreshTable();
        updateDashboard();
        saveData(false);
    }

    private void logout() {
        saveData(false);
        new LoginGUI().setVisible(true);
        dispose();
    }

    private void loadData() {
        File saved = new File("InventoryData.csv");
        if (saved.exists()) {
            loadCanonicalFile(saved);
            return;
        }
        loadASCFile(new File("AshersSportsCollective.csv"));
        for (StockmsmItem msm : StockmsmItem.loadStock()) {
            dataList.add(new AdaptorOfmsmtoasc(msm.getDepartmentId(), msm.getCode(), msm.getnameAndDescription(),
                    msm.getUnitPrice(), msm.getQuantityInStock()));
        }
        if (!dataList.isEmpty()) saveData(false);
    }

    private void loadCanonicalFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                if (first && line.toLowerCase().contains("stockcode")) {
                    first = false;
                    continue;
                }
                first = false;
                String[] p = splitCsv(line, 6);
                if (p.length < 6) continue;
                dataList.add(new StockascItem(p[0], p[1], p[2], Integer.parseInt(p[3]),
                        Integer.parseInt(p[4]), Integer.parseInt(p[5]), "InventoryData.csv"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not read InventoryData.csv: " + ex.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadASCFile(File file) {
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = splitCsv(line, 6);
                if (p.length < 6) continue;
                try {
                    int pounds = Integer.parseInt(p[3].trim());
                    int pence = Integer.parseInt(p[4].trim());
                    int quantity = Integer.parseInt(p[5].trim());
                    dataList.add(new StockascItem(p[0].trim(), p[1].trim(), p[2].trim(), pounds, pence,
                            quantity, "InventoryData.csv"));
                } catch (NumberFormatException ignored) {
                    // Header or invalid row: safely skip it.
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not read " + file.getName(), "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] splitCsv(String line, int expectedColumns) {
        // The supplied coursework CSV files do not contain quoted commas, so this is sufficient and dependency-free.
        return line.split(",", expectedColumns);
    }

    private void saveData(boolean showMessage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("InventoryData.csv"))) {
            writer.write("StockCode,Title,Description,Pounds,Pence,Quantity");
            writer.newLine();
            for (StockascItem item : dataList) {
                writer.write(clean(item.getPC()) + "," + clean(item.getpT()) + "," + clean(item.getProdDescription()) + ","
                        + item.getPricePound() + "," + item.getPp() + "," + item.getStockQty());
                writer.newLine();
            }
            if (showMessage) JOptionPane.showMessageDialog(this, "Inventory saved successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save inventory: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }

    public JTable getAsportsTable() {
        return sportsTable;
    }

    public static void main(String[] args) {
        LoginGUI.main(args);
    }
}
