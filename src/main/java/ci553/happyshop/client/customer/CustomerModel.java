package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Order;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.ProductListFormatter;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

/**
 * You can either directly modify the CustomerModel class to implement the required tasks,
 * or create a subclass of CustomerModel and override specific methods where appropriate.
 */
public class CustomerModel {
    public CustomerView cusView;
    public DatabaseRW databaseRW; //Interface type, not specific implementation
    //Benefits: Flexibility: Easily change the database implementation.

    private Product theProduct = null; // product found from search
    private final ArrayList<Product> trolley = new ArrayList<>(); // a list of products in trolley

    // Four UI elements to be passed to CustomerView for display updates.
    private String imageName = "imageHolder.jpg";                // Image to show in product preview (Search Page)
    private String displayLaSearchResult = "No Product was searched yet"; // Label showing search result message (Search Page)
    private String displayTaTrolley = "";                                // Text area content showing current trolley items (Trolley Page)
    private String displayTaReceipt = "";                                // Text area content showing receipt after checkout (Receipt Page)

    //SELECT productID, description, image, unitPrice,inStock quantity
    void search() throws SQLException {
        String input = cusView.tfSearch.getText().trim();


        ArrayList<Product> results = new ArrayList<>();

        if (!input.isEmpty()) {
            if (input.matches("\\d+")) {
                Product p = databaseRW.searchByProductId(input);
                if (p != null) {
                    results.add(p);
                }
            } else {
                results = databaseRW.searchProduct(input);
            }
//        if (!productId.isEmpty()) {
//            theProduct = databaseRW.searchByProductId(productId); //search database
//            if (theProduct != null && theProduct.getStockQuantity() > 0) {
//                double unitPrice = theProduct.getUnitPrice();
//                String description = theProduct.getProductDescription();
//                int stock = theProduct.getStockQuantity();
//
//                String baseInfo = String.format("Product_Id: %s\n%s,\nPrice: £%.2f", productId, description, unitPrice);
//                String quantityInfo = stock < 100 ? String.format("\n%d units left.", stock) : "";
//                displayLaSearchResult = baseInfo + quantityInfo;
//                System.out.println(displayLaSearchResult);
//            } else {
            theProduct = null;
            displayLaSearchResult = "No Product was found with ID " + input;
            System.out.println("No Product was found with ID " + input);
        }
        else {
            theProduct = null;
            displayLaSearchResult = "Please type ProductID";
            System.out.println("Please type ProductID.");
        }
        ObservableList<Product> observable =
                FXCollections.observableArrayList(results);

        cusView.updateObservableProductList(observable);
        updateView();
    }

    void addToTrolley(Product productChosen) {
        theProduct = productChosen;
        if (theProduct != null) {

            boolean prodExist = false; // used to state if item is within the trolley already, default = false

            for (Product product : trolley) { // looks at each item in the list (trolley)
                if (product.getProductId().equals(theProduct.getProductId())) { // check if current product is within trolley already
                    prodExist = true; // item is in trolley already
                    int newQuantity = product.getOrderedQuantity() + 1; // get current quantity then increment by 1
                    product.setOrderedQuantity(newQuantity); // set new quantity
                    break;
                }
            }

            if (!prodExist) { // if product is not in trolley, add to trolley
                theProduct.setOrderedQuantity(1);
                trolley.add(theProduct);
            }

            makeOrganizedTrolley(); // sort trolley
            displayTaTrolley = ProductListFormatter.buildString(trolley); //build a String for trolley so that we can show it
        } else {
            displayLaSearchResult = "Please search for an available product before adding it to the trolley";
            System.out.println("must search and get an available product before add to trolley");
        }
        displayTaReceipt = ""; // Clear receipt to switch back to trolleyPage (receipt shows only when not empty)
        updateView();
    }

    void RemoveFromTrolley(Product chosenProduct) {
        if (chosenProduct == null) return;

        int newQty = chosenProduct.getOrderedQuantity() - 1;
        chosenProduct.setOrderedQuantity(Math.max(newQty, 0)); // never below 0

        // If quantity reaches 0, remove the product from the trolley
        if (chosenProduct.getOrderedQuantity() == 0) {
            trolley.remove(chosenProduct);
        }
        displayTaTrolley = ProductListFormatter.buildString(trolley);
        updateView();
    }

    void checkOut() throws IOException, SQLException {
        if (!trolley.isEmpty()) {
            // Group the products in the trolley by productId to optimize stock checking
            // Check the database for sufficient stock for all products in the trolley.
            // If any products are insufficient, the update will be rolled back.
            // If all products are sufficient, the database will be updated, and insufficientProducts will be empty.
            // Note: If the trolley is already organized (merged and sorted), grouping is unnecessary.
            //ArrayList<Product> groupedTrolley= groupProductsById(trolley);
            trolley.sort(Comparator.comparing(Product::getProductId)); // sorts list by productID for picker
            ArrayList<Product> insufficientProducts = databaseRW.purchaseStocks(trolley);

            if (insufficientProducts.isEmpty()) { // If stock is sufficient for all products
                //get OrderHub and tell it to make a new Order
                OrderHub orderHub = OrderHub.getOrderHub();
                Order theOrder = orderHub.newOrder(trolley);
                trolley.clear();
                displayTaTrolley = "";
                displayTaReceipt = String.format(
                        "Order_ID: %s\nOrdered_Date_Time: %s\n%s",
                        theOrder.getOrderId(),
                        theOrder.getOrderedDateTime(),
                        ProductListFormatter.buildString(theOrder.getProductList())
                );
                System.out.println(displayTaReceipt);
            } else { // Some products have insufficient stock — build an error message to inform the customer
                StringBuilder errorMsg = new StringBuilder();
                for (Product p : insufficientProducts) {
                    errorMsg.append("• ").append(p.getProductId()).append(", ")
                            .append(p.getProductDescription()).append(" (Only ")
                            .append(p.getStockQuantity()).append(" available, ")
                            .append(p.getOrderedQuantity()).append(" requested)\n");
                }
                theProduct = null;

                for (Product p : insufficientProducts) {
                    // Go through each product that doesn't have enough stock
                    for (int i = 0; i < trolley.size(); i++) {
                        Product t = trolley.get(i);
                        if (t.getProductId().equals(p.getProductId())) {
                            trolley.remove(i); // remove from trolley
                            displayTaTrolley = ProductListFormatter.buildString(trolley);
                            break; // stop
                        }
                    }
                }
                // 2. Trigger a message window to notify the customer about the insufficient stock, rather than directly changing displayLaSearchResult.
                //You can use the provided RemoveProductNotifier class and its showRemovalMsg method for this purpose.
                //remember close the message window where appropriate (using method closeNotifierWindow() of RemoveProductNotifier class)
                RemoveProductNotifier notifier = new RemoveProductNotifier();
                notifier.cusView = this.cusView;      // <--- assign the main CustomerView
                notifier.showRemovalMsg(errorMsg.toString());

// Build a simple message to display
                StringBuilder message = new StringBuilder("The following products were removed because there is not enough stock:\n");
                for (Product p : insufficientProducts) {
                    message.append("- ").append(p.getProductId()).append(": ").append(p.getProductDescription()).append(" (Only ").append(p.getStockQuantity()).append(" left, ").append(p.getOrderedQuantity()).append(" requested)\n");
                }
                notifier.showRemovalMsg(message.toString());

                //displayLaSearchResult = "Checkout failed due to insufficient stock for the following products:\n" + errorMsg.toString();
                System.out.println("stock is not enough");
            }
        } else {
            displayTaTrolley = "Your trolley is empty";
            System.out.println("Your trolley is empty");
        }
        updateView();
    }

    /**
     * Groups products by their productId to optimize database queries and updates.
     * By grouping products, we can check the stock for a given `productId` once, rather than repeatedly
     */


    void cancel() {

        for (Product p : trolley) {
            p.setOrderedQuantity(0);
        }

        trolley.clear();
        displayTaTrolley = "";
        updateView();
    }

    void closeReceipt() {
        displayTaReceipt = "";
    }

    void updateView() {
        if (theProduct != null) {
            imageName = theProduct.getProductImageName();
            String relativeImageUrl = StorageLocation.imageFolder + imageName; //relative file path, eg images/0001.jpg
            // Get the full absolute path to the image
            Path imageFullPath = Paths.get(relativeImageUrl).toAbsolutePath();
            imageName = imageFullPath.toUri().toString(); //get the image full Uri then convert to String
            System.out.println("Image absolute path: " + imageFullPath); // Debugging to ensure path is correct

        } else {
            imageName = "imageHolder.jpg";
        }
        cusView.update(imageName, displayLaSearchResult, displayTaTrolley, displayTaReceipt, sortChoice);
    }
    /*
     extra notes:
    Path.toUri(): Converts a Path object (a file or a directory path) to a URI object.
    File.toURI(): Converts a File object (a file on the filesystem) to a URI object
    */

    //for test only
    public ArrayList<Product> getTrolley() {
        return trolley;
    }

    int sortChoice = 1; // for sort choice

    void sortChange() { // change sort type then sort
        if (sortChoice < 4) {
            sortChoice++;
        } else {
            sortChoice = 1; // reset to first option
        }
        makeOrganizedTrolley();
    }

    void makeOrganizedTrolley() {

        switch (sortChoice) {
            case 1:
                // sorts list so new item is in correct place - by productID
                trolley.sort(Comparator.comparing(Product::getProductId));
                System.out.println("Trolley sorted by Product ID.");

                break;

            case 2:
                // sorts list so new item is in correct place - by productName
                trolley.sort(Comparator.comparing(Product::getProductDescription));
                System.out.println("Trolley sorted by Product Name.");
                break;

            case 3:
                // sorts list so new item is in correct place - by total product price (prod price x quantity) low to high
                trolley.sort(Comparator.comparingDouble(Product -> Product.getUnitPrice() * Product.getOrderedQuantity()));
                System.out.println("Trolley sorted by Total Product Price.");
                break;
            case 4:
                // sorts list so new item is in correct place - by total product price (prod price x quantity) high to low
                Comparator<Product> comparator = Comparator.comparingDouble(p -> p.getUnitPrice() * p.getOrderedQuantity());
                comparator = comparator.reversed();
                trolley.sort(comparator);
                System.out.println("Trolley sorted by Total Product Price.");
                break;
        }
        displayTaTrolley = ProductListFormatter.buildString(trolley);
        updateView();

//        System.out.println("Sorted List:");
//        for (Product p : trolley) {
//            System.out.println("Name: " + p.getProductDescription() + " (ID: " + p.getProductId() + ", Qty: " + p.getOrderedQuantity() + ", Price: " + (p.getUnitPrice() * p.getOrderedQuantity()) + ")");
//        }
    }

    public String checkStock(Product currentproduct) {
        if (currentproduct == null) {
            System.out.println("No product to check stock for");
            return null;
        }

        int productStock = currentproduct.getStockQuantity();
        System.out.println("STOCK QUAN: " + productStock);

        String stockMessage;

        if (productStock == 0) {
            stockMessage = "No stock available";
        } else if (productStock < 15) {
            stockMessage = "Low stock: only " + productStock + " left!";
        } else {
            stockMessage = "In stock: " + productStock;
        }

        return stockMessage;
    }
}
