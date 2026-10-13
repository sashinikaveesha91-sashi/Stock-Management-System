/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oop.ica.e2;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Shiva Krishna
 */
public class TableModelASC extends AbstractTableModel {
    //fields
    //empty string array for column names

    private String[] columnNames;

    //empty two dimensional object array for data
    private Object[][] data;

    //provide a custom constructor
    public TableModelASC(final String[] colNames, final ArrayList<StockascItem> dataList) {
        //get length of array parameter
        int columnNamesLength = colNames.length;

        //copy parameter array into column names
        columnNames = Arrays.copyOf(colNames, columnNamesLength);
        //get size of arraylist
        int rowLength = dataList.size();

        //set size of data array
        data = new Object[rowLength][columnNamesLength];

        //set index variables for data row
        int row = 0;

        //loop through array list
        for (StockascItem asc : dataList) {
            //get fields
            String stockCode = asc.getPC();
            String title = asc.getpT();
            String description = asc.getProdDescription();
            double cost = asc.getPricePound() + asc.getPp() / 100.0;
            String price = '£' + Double.toString(cost);
            Integer quantity = asc.getStockQty();

            //use fields to create object array
            Object[] dataRow = new Object[]{stockCode, title, description, asc.getPricePound(),asc.getPp(), quantity};

            //copy row data array into current data row
            data[row] = Arrays.copyOf(dataRow, columnNamesLength);

            //increase row index
            row++;
        }
    }

    //overridden method
    @Override
    public int getRowCount() {
        //give length of first dimension of data
        return data.length;
    }

    @Override
    public int getColumnCount() {
        //give length of scolumn names
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        //get object at insection of row and colun in data
        return data[row][column];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
