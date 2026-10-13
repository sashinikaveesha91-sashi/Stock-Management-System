package oop.ica.e2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class StockmsmItem {
    private final int departmentId;
    private final String code;
    private final String nameAndDescription;
    private final int unitPrice;
    private int quantityInStock;
    
    public StockmsmItem(int departmentId, String code, String titleAndDescription, int unitPrice, int quantityInStock) {
        this.departmentId = departmentId;
        this.code = code;
        this.nameAndDescription = titleAndDescription;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
    }

        public int getDepartmentId() {
        return departmentId;
    }

      public String getCode() {
        return code;
    }
      
      public String getnameAndDescription()
      {
            return nameAndDescription;
      }
   
      
   public String getName() {
       
   String[] splitname  = nameAndDescription.split("(?<=\\u00a0\\u00a0\\u00a0)(?!\\u00a0)");
             if(splitname.length > 1)
             {
                String name = splitname[0];
                return name;
             }
    return nameAndDescription;
}

   public String getDescription() {
         String[] splitdescription = nameAndDescription.split("(?<=\\u00a0\\u00a0\\u00a0)(?!\\u00a0)");
         if(splitdescription.length > 1)
             {
                String description = splitdescription[1];
                return description;
             }
    return nameAndDescription;
}

    public int getUnitPrice() {
        return unitPrice;
    }

      public int getQuantityInStock() {
        return quantityInStock;
    }
      
      public int getpounds() {
           int pounds = getUnitPrice() / 100;
        return  pounds ;
    }
      
       public int getpence() {
            int pence = getUnitPrice() % 100;
        return pence;
    }
    
    public String getHumanFriendlyUnitPrice() {
        
        final int pounds = getUnitPrice() / 100;
        final int pence = getUnitPrice() % 100;
        return String.format("%d.%02d", pounds, pence);
    }
    
    public void setQuanity(int newQuantity) {
        if(newQuantity >= 0) {
            quantityInStock = newQuantity;
        }
    }
    
    @Override
    public String toString() {
        return String.format("%d-%s - %s - %s - UNIT PRICE: £%s - QTY: %d",
                getDepartmentId(),
                getCode(),
                getName(),
                getDescription(),
                getHumanFriendlyUnitPrice(),
                getQuantityInStock());
    }

    public static List<StockmsmItem> loadStock() {
        final List<StockmsmItem> loadedStock = new ArrayList<>();
      
        String line;
        String fileName = "MengdasSportyMart.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] data = line.split(",", 5);
                if (data.length < 5) continue;
                try {
                    StockmsmItem item = new StockmsmItem(Integer.parseInt(data[0].replaceAll("\\p{C}", "").trim()),
                            data[1].trim(), data[2].trim(), Integer.parseInt(data[3].trim()), Integer.parseInt(data[4].trim()));
                    loadedStock.add(item);
                } catch (NumberFormatException ignored) {
                    // Skip CSV header or malformed row.
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: " + fileName + " does not exist.", "No File Found", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + fileName + " has no data.", "No File Data Found", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            System.out.println("Invalid file format: " + fileName + e.getMessage());
        }
        return loadedStock;
    }


    
}
