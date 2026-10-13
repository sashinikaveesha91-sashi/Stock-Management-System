
 // Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 // Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 
package oop.ica.e2;

public class StockascItem {
    
    String pc;
    String pT;
    String prodDescription;
    int pp;
    int stockQty;
    int pricePound;
    private String sourceFile;
    
    public StockascItem (String pc, String pT, String prodDescription, int pricePound, int pp, int stockQty,String sourceFile ) {
        this.pc = pc;
        this.pT = pT;
        this.prodDescription = prodDescription;
        this.pricePound = pricePound;
        this.pp = pp;
        this.stockQty = stockQty;
        this.sourceFile = sourceFile;
    }

    //These methods are specified as public, indicating that they are accessible from external classes and methods
    public int getStockQty() {
        return stockQty;
    }

    public int getPricePound() {
        return pricePound;
    }

    public int getPp() {
        return pp;
    }

    public String getPC() {
        return pc;
    }

    public String getpT() {
        return pT;
    }

    public String getProdDescription() {
        return prodDescription;
    }

  
    public void setStockQty(int StockQty) {
        this.stockQty = StockQty;
    }
    
    public double getPrice() {
        return pricePound + pp / 100.0;
    }
    public void setSourceFile(String sourceFile) {
    this.sourceFile = sourceFile;
}

    public String getSourceFile() {
        return sourceFile;
    }

    
}
