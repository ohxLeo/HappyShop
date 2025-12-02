package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import ci553.happyshop.utility.WindowBounds;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;


/**
 * The CustomerView is separated into two sections by a line :
 * <p>
 * 1. Search Page – Always visible, allowing customers to browse and search for products.
 * 2. the second page – display either the Trolley Page or the Receipt Page
 * depending on the current context. Only one of these is shown at a time.
 */

public class CustomerView {
    public CustomerController cusController;

    private final int WIDTH = UIStyle.customerWinWidth;
    private final int HEIGHT = UIStyle.customerWinHeight;
    private final int COLUMN_WIDTH = WIDTH / 2 - 10;

    private HBox hbRoot; // Top-level layout manager
    private VBox vbTrolleyPage;  //vbTrolleyPage and vbReceiptPage will swap with each other when need
    private VBox vbReceiptPage;

    TextField tfSearch; //for user input on the search page. Made accessible so it can be accessed or modified by CustomerModel

    //four controllers needs updating when program going on
    private ImageView ivProduct; //image area in searchPage
    private Label lbProductInfo;//product text info in searchPage
    private TextArea taTrolley; //in trolley Page
    private Label laSortType;
    private TextArea taReceipt;//in receipt page

    private ObservableList<Product> customerProductList; //observable product list
    ListView<Product> lvCustomerProducts; //A ListView observes the product list

    // Holds a reference to this CustomerView window for future access and management
    // (e.g., positioning the removeProductNotifier when needed).
    private Stage viewWindow;

    public void start(Stage window) {
        VBox vbSearchPage = createSearchPage();
        vbTrolleyPage = CreateTrolleyPage();
        vbReceiptPage = createReceiptPage();

        // Create a divider line
        Line line = new Line(0, 0, 0, HEIGHT);
        line.setStrokeWidth(4);
        line.setStroke(Color.LIGHTBLUE);
        VBox lineContainer = new VBox(line);
        lineContainer.setPrefWidth(4); // Give it some space
        lineContainer.setAlignment(Pos.CENTER);

        hbRoot = new HBox(10, vbSearchPage, lineContainer, vbTrolleyPage); //initialize to show trolleyPage
        hbRoot.setAlignment(Pos.CENTER);
        hbRoot.setStyle(UIStyle.rootStyle);

        Scene scene = new Scene(hbRoot, WIDTH, HEIGHT);
        window.setScene(scene);
        window.setTitle("🛒 HappyShop Customer Client");
        WinPosManager.registerWindow(window, WIDTH, HEIGHT); //calculate position x and y for this window
        window.show();
        viewWindow = window;// Sets viewWindow to this window for future reference and management.
    }

    private VBox createSearchPage() {
        Label laPageTitle = new Label("Search by Product ID/Name");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        Label laId = new Label("ID/Name:");
        laId.setStyle(UIStyle.labelStyle);
        tfSearch = new TextField();
        tfSearch.setPromptText("eg. 0001 / TV | * to see all");
        tfSearch.setStyle(UIStyle.textFiledStyle);

        tfSearch.setOnAction(actionEvent -> {
            try {
                cusController.doAction("\uD83D\uDD0D");  //pressing enter can also do search
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button btnSearch = new Button("\uD83D\uDD0D"); // button to search
        btnSearch.setStyle(UIStyle.buttonStyle);
        btnSearch.setOnAction(this::buttonClicked);
        HBox hbSearch = new HBox(10, laId, tfSearch, btnSearch);

        ivProduct = new ImageView("imageHolder.jpg");
        ivProduct.setFitHeight(60);
        ivProduct.setFitWidth(60);
        ivProduct.setPreserveRatio(true); // Image keeps its original shape and fits inside 60×60
        ivProduct.setSmooth(true); //make it smooth and nice-looking

        lbProductInfo = new Label("Thank you for shopping with us.");
        lbProductInfo.setWrapText(true);
        lbProductInfo.setMinHeight(Label.USE_PREF_SIZE);  // Allow auto-resize
        lbProductInfo.setStyle(UIStyle.labelMulLineStyle);
        HBox hbSearchResult = new HBox(5, ivProduct, lbProductInfo);
        hbSearchResult.setAlignment(Pos.CENTER_LEFT);

        customerProductList = FXCollections.observableArrayList();
        lvCustomerProducts = new ListView<>(customerProductList);


        VBox vbSearchPage = new VBox(15, laPageTitle, hbSearch, lvCustomerProducts);
        vbSearchPage.setPrefWidth(COLUMN_WIDTH);
        vbSearchPage.setAlignment(Pos.TOP_CENTER);
        vbSearchPage.setStyle("-fx-padding: 15px;");

        lvCustomerProducts.setCellFactory(param -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (empty || product == null) {
                    setGraphic(null);
                    System.out.println("empty item");
                } else {
                    String imageName = product.getProductImageName(); // Get image name (e.g. "0001.jpg")
                    String relativeImageUrl = StorageLocation.imageFolder + imageName;
                    // Get the full absolute path to the image
                    Path imageFullPath = Paths.get(relativeImageUrl).toAbsolutePath();
                    String imageFullUri = imageFullPath.toUri().toString();// Build the full image Uri

                    ImageView ivPro;
                    try {
                        ivPro = new ImageView(new Image(imageFullUri, 50, 45, true, true)); // Attempt to load the product image
                    } catch (Exception e) {
                        // If loading fails, use a default image directly from the resources folder
                        ivPro = new ImageView(new Image("imageHolder.jpg", 50, 45, true, true)); // Directly load from resources
                    }

                    Button btnAdd = new Button("+");
                    btnAdd.setOnAction(e -> {

                        try {
                            cusController.doTrolleyAction("Add", product);
                        } catch (SQLException | IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    });

                    Button btnSub = new Button("-");
                    btnSub.setOnAction(e -> {
                        try {
                            cusController.doTrolleyAction("Remove", product);
                        } catch (SQLException | IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    });


                    Label laProToString = new Label(String.format("%s\nPrice: £%.2f", product.getProductDescription(), product.getUnitPrice())); // Create a label for product details
                    Label laProStock = new Label(cusController.CheckAvailStock(product));

                    if (product.getStockQuantity() <= 10) {
                        laProStock.setStyle("-fx-text-fill: red;");
                    }

                    VBox ProSting = new VBox(0, laProToString, laProStock);

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS); // used to create space between product info and buttons

                    System.out.println("SHOP LABEL: " + laProToString);
                    HBox hbox = new HBox(10, ivPro, ProSting, spacer, btnAdd, btnSub); // Put ImageView and label in a horizontal layout
                    setGraphic(hbox);  // Set the whole row content
                }
            }
        });

        return vbSearchPage;
    }


    private VBox CreateTrolleyPage() {
        Label laPageTitle = new Label("🛒🛒  Trolley 🛒🛒");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        taTrolley = new TextArea();
        taTrolley.setEditable(false);
        taTrolley.setPrefSize(WIDTH / 2, HEIGHT - 50);

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(this::buttonClicked);
        btnCancel.setStyle(UIStyle.buttonStyle);

        Button btnCheckout = new Button("Check Out");
        btnCheckout.setOnAction(this::buttonClicked);
        btnCheckout.setStyle(UIStyle.buttonStyle);

        laSortType = new Label("Sorted by Product ID");
        laSortType.setStyle(UIStyle.labelStyle);

        Button btnSort = new Button("Sort");
        btnSort.setOnAction(this::buttonClicked);
        btnSort.setStyle(UIStyle.buttonStyle);


        HBox hbBtns = new HBox(8, btnCancel, btnSort, btnCheckout);
        hbBtns.setStyle("-fx-padding: 15px;");
        hbBtns.setAlignment(Pos.CENTER);

        vbTrolleyPage = new VBox(5, laPageTitle, taTrolley, laSortType, hbBtns);
        vbTrolleyPage.setPrefWidth(COLUMN_WIDTH);
        vbTrolleyPage.setAlignment(Pos.TOP_CENTER);
        vbTrolleyPage.setStyle("-fx-padding: 15px;");
        return vbTrolleyPage;
    }

    private VBox createReceiptPage() {
        Label laPageTitle = new Label("Receipt");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        taReceipt = new TextArea();
        taReceipt.setEditable(false);
        taReceipt.setPrefSize(WIDTH / 2, HEIGHT - 50);

        Button btnCloseReceipt = new Button("OK & Close"); //btn for closing receipt and showing trolley page
        btnCloseReceipt.setStyle(UIStyle.buttonStyle);

        btnCloseReceipt.setOnAction(this::buttonClicked);

        vbReceiptPage = new VBox(15, laPageTitle, taReceipt, btnCloseReceipt);
        vbReceiptPage.setPrefWidth(COLUMN_WIDTH);
        vbReceiptPage.setAlignment(Pos.TOP_CENTER);
        vbReceiptPage.setStyle(UIStyle.rootStyleYellow);
        return vbReceiptPage;
    }


    private void buttonClicked(ActionEvent event) {
        try {
            Button btn = (Button) event.getSource();
            String action = btn.getText();
            if (action.equals("Add to Trolley")) {
                showTrolleyOrReceiptPage(vbTrolleyPage); //ensure trolleyPage shows if the last customer did not close their receiptPage
            }
            if (action.equals("OK & Close")) {
                showTrolleyOrReceiptPage(vbTrolleyPage);
            }
            cusController.doAction(action);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(String imageName, String searchResult, String trolley, String receipt, int sortType) {

        ivProduct.setImage(new Image(imageName));
        lbProductInfo.setText(searchResult);
        taTrolley.setText(trolley);
        if (!receipt.isEmpty()) {
            showTrolleyOrReceiptPage(vbReceiptPage);
            taReceipt.setText(receipt);
        }
        String sortText = "Sorted by Product ID";
        if (sortType == 1) {
            sortText = "Sorted by Product ID";
        } else if (sortType == 2) {
            sortText = "Sorted by Product Name";
        } else if (sortType == 3) {
            sortText = "Sorted by Price | Low to High";
        } else if (sortType == 4) {
            sortText = "Sorted by Price | High to Low";
        }

        laSortType.setText(sortText);
        System.out.println(sortType);


    }

    public void updateObservableProductList(ObservableList<Product> productList) {
        int proCounter = productList.size();
        System.out.println(proCounter);

        // Optional: update a label if you want a summary like warehouse
        // You might need to add this label to your search page first
        // laSearchSummary.setText(proCounter + " products found");
        // laSearchSummary.setVisible(true);

        // Update the observable list
        customerProductList.clear();
        customerProductList.addAll(productList);

        // Optionally, select the first product and update the image/info display
        if (!customerProductList.isEmpty()) {
            lvCustomerProducts.getSelectionModel().select(1);
            Product firstProduct = lvCustomerProducts.getSelectionModel().getSelectedItem();

            // Update image and info labels
            String imageName = firstProduct.getProductImageName();
            String relativeImageUrl = StorageLocation.imageFolder + imageName;
            Path imageFullPath = Paths.get(relativeImageUrl).toAbsolutePath();
            String imageFullUri = imageFullPath.toUri().toString();
            ivProduct.setImage(new Image(imageFullUri));
            lbProductInfo.setText(firstProduct.toString());
        } else {
            ivProduct.setImage(new Image("imageHolder.jpg"));
            lbProductInfo.setText("No products found.");
        }
    }


    // Replaces the last child of hbRoot with the specified page.
    // the last child is either vbTrolleyPage or vbReceiptPage.
    private void showTrolleyOrReceiptPage(Node pageToShow) {
        int lastIndex = hbRoot.getChildren().size() - 1;
        if (lastIndex >= 0) {
            hbRoot.getChildren().set(lastIndex, pageToShow);
        }
    }

    WindowBounds getWindowBounds() {
        return new WindowBounds(viewWindow.getX(), viewWindow.getY(),
                viewWindow.getWidth(), viewWindow.getHeight());
    }
}
