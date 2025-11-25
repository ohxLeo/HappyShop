package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerController {
    public CustomerModel cusModel;

    public void doAction(String action) throws SQLException, IOException {
        switch (action) {
            case "\uD83D\uDD0D":
                cusModel.search();
                break;
            case "Cancel":
                cusModel.cancel();
                break;
            case "Check Out":
                cusModel.checkOut();
                break;
            case "OK & Close":
                cusModel.closeReceipt();
                break;
            case "Sort":
                cusModel.sortChange();
                break;
        }
    }
    public void doTrolleyAction(String action, Product product) throws SQLException, IOException {
        switch (action){
            case "Add":
                cusModel.addToTrolley(product);
                break;
            case "Remove":
                cusModel.RemoveFromTrolley(product);
                break;
        }

    }

    public String CheckAvailStock(Product product){
        String ProdStockMsg = cusModel.checkStock(product);
        return ProdStockMsg;
    }

}
