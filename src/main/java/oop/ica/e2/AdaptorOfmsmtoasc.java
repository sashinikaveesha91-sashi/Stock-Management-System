/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oop.ica.e2;

public class AdaptorOfmsmtoasc extends StockascItem {
    //field
    private StockmsmItem StockmsmItem;
    
    //constructor
    public AdaptorOfmsmtoasc(int departmentId, String code, String titleAndDescription, int unitPrice, int quantityInStock){
        
        //Generate an object 
        super("", "", "", 0, 0, quantityInStock, "MengdasSportyMart.csv");
        
        // Generate field
        StockmsmItem = new StockmsmItem(departmentId,code, titleAndDescription,unitPrice,quantityInStock);
    }
    
    //override inherited Reader method
    @Override
      public String getPC() {
          
          if(StockmsmItem.getDepartmentId() == 1 )
          {
            return "RUN-"  + StockmsmItem.getCode() + "-MSM";
          }
          
           if(StockmsmItem.getDepartmentId() == 2 )
          {
            return "SWM-"  + StockmsmItem.getCode() + "-MSM";
          }
           
           if(StockmsmItem.getDepartmentId() == 3 )
          {
            return "CYC-"  + StockmsmItem.getCode() + "-MSM";
          }
    
          return StockmsmItem.getCode();
    }
       
    @Override
    public String getpT() {
      
        return StockmsmItem.getName();
    }
    
    @Override
    public String getProdDescription() {
        
        return StockmsmItem.getDescription();
    }
    
    @Override
    public int getPricePound() {
        return StockmsmItem.getpounds();
    }
    
    @Override
    public int getPp() {
        return StockmsmItem.getpence();
    }
    
     @Override
     public double getPrice() {
        return StockmsmItem.getpounds() + StockmsmItem.getpence() / 100.0;
    }
    
}// end of class
