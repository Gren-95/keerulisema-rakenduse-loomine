package ee.tak24.library.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ee.tak24.library.model.Book;
import ee.tak24.library.model.Member;
import ee.tak24.library.model.MembershipType;
import ee.tak24.library.service.LibraryService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX GUI for the Library Management System.
 * Demonstrates modern GUI development with JavaFX and clean architecture.
 * 
 * This class provides a user-friendly graphical interface for all library
 * management operations including member and book management.
 */
public class LibraryGUI extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(LibraryGUI.class);
    
    private LibraryService libraryService;
    private TableView<Member> memberTable;
    private TableView<Book> bookTable;
    private ObservableList<Member> memberData;
    private ObservableList<Book> bookData;
    
    private TextField memberFirstNameField;
    private TextField memberLastNameField;
    private TextField memberEmailField;
    private TextField memberDobField;
    private TextField memberStreetField;
    private TextField memberCityField;
    private TextField memberPostalCodeField;
    private TextField memberCountryField;
    private ComboBox<MembershipType> memberTypeCombo;
    
    private TextField bookTitleField;
    private TextField bookAuthorField;
    private TextField bookIsbnField;
    private TextField bookPublisherField;
    private TextField bookPublicationDateField;
    private ComboBox<String> bookCategoryCombo;
    
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Library Management System GUI...");
        
        // Initialize library service
        initializeLibraryService();
        
        // Create main UI
        primaryStage.setTitle("Library Management System - TAK24");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        
        TabPane tabPane = new TabPane();
        
        // Member Management Tab
        Tab memberTab = createMemberManagementTab();
        tabPane.getTabs().add(memberTab);
        
        // Book Management Tab
        Tab bookTab = createBookManagementTab();
        tabPane.getTabs().add(bookTab);
        
        // Statistics Tab
        Tab statsTab = createStatisticsTab();
        tabPane.getTabs().add(statsTab);
        
        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Load initial data
        refreshMemberData();
        refreshBookData();
        
        logger.info("Library Management System GUI started successfully");
    }
    
    /**
     * Initialize the library service with dependencies.
     */
    private void initializeLibraryService() {
        try {
            // Use the same initialization as the CLI application
            ee.tak24.library.util.LibraryFactory factory = new ee.tak24.library.util.LibraryFactory();
            ee.tak24.library.persistence.JsonLibraryRepository repository = new ee.tak24.library.persistence.JsonLibraryRepository();
            this.libraryService = new ee.tak24.library.service.LibraryServiceImpl(repository);
            factory.initializeWithSampleData(libraryService);
        } catch (Exception e) {
            logger.error("Failed to initialize library service", e);
            showAlert("Error", "Failed to initialize library service: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Create the member management tab.
     */
    private Tab createMemberManagementTab() {
        Tab tab = new Tab("Member Management");
        
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        
        // Member List
        Label memberListLabel = new Label("Members:");
        memberTable = new TableView<>();
        memberData = FXCollections.observableArrayList();
        memberTable.setItems(memberData);
        
        // Member table columns
        TableColumn<Member, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(120);
        
        TableColumn<Member, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameColumn.setPrefWidth(150);
        
        TableColumn<Member, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setPrefWidth(200);
        
        TableColumn<Member, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipType"));
        typeColumn.setPrefWidth(100);
        
        TableColumn<Member, Boolean> activeColumn = new TableColumn<>("Active");
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        activeColumn.setPrefWidth(80);
        
        memberTable.getColumns().addAll(idColumn, nameColumn, emailColumn, typeColumn, activeColumn);
        memberTable.setPrefHeight(200);
        
        // Member form
        Label formLabel = new Label("Add New Member:");
        GridPane memberForm = createMemberForm();
        
        // Buttons
        HBox buttonBox = new HBox(10);
        Button refreshMembersBtn = new Button("Refresh");
        refreshMembersBtn.setOnAction(e -> refreshMemberData());
        
        Button clearFormBtn = new Button("Clear Form");
        clearFormBtn.setOnAction(e -> clearMemberForm());
        
        buttonBox.getChildren().addAll(refreshMembersBtn, clearFormBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        mainLayout.getChildren().addAll(memberListLabel, memberTable, formLabel, memberForm, buttonBox);
        tab.setContent(mainLayout);
        
        return tab;
    }
    
    /**
     * Create the book management tab.
     */
    private Tab createBookManagementTab() {
        Tab tab = new Tab("Book Management");
        
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        
        // Book List
        Label bookListLabel = new Label("Books:");
        bookTable = new TableView<>();
        bookData = FXCollections.observableArrayList();
        bookTable.setItems(bookData);
        
        // Book table columns
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setPrefWidth(150);
        
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);
        
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(150);
        
        TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setPrefWidth(100);
        
        TableColumn<Book, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(100);
        
        bookTable.getColumns().addAll(isbnColumn, titleColumn, authorColumn, categoryColumn, statusColumn);
        bookTable.setPrefHeight(200);
        
        // Book form
        Label formLabel = new Label("Add New Book:");
        GridPane bookForm = createBookForm();
        
        // Buttons
        HBox buttonBox = new HBox(10);
        Button refreshBooksBtn = new Button("Refresh");
        refreshBooksBtn.setOnAction(e -> refreshBookData());
        
        Button clearFormBtn = new Button("Clear Form");
        clearFormBtn.setOnAction(e -> clearBookForm());
        
        buttonBox.getChildren().addAll(refreshBooksBtn, clearFormBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        mainLayout.getChildren().addAll(bookListLabel, bookTable, formLabel, bookForm, buttonBox);
        tab.setContent(mainLayout);
        
        return tab;
    }
    
    /**
     * Create the statistics tab.
     */
    private Tab createStatisticsTab() {
        Tab tab = new Tab("Statistics");
        
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));
        
        Label statsLabel = new Label("Library Statistics");
        statsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        ListView<String> statsList = new ListView<>();
        statsList.setPrefHeight(400);
        
        Button refreshStatsBtn = new Button("Refresh Statistics");
        refreshStatsBtn.setOnAction(e -> refreshStatistics(statsList));
        
        mainLayout.getChildren().addAll(statsLabel, statsList, refreshStatsBtn);
        tab.setContent(mainLayout);
        
        return tab;
    }
    
    /**
     * Create the member form.
     */
    private GridPane createMemberForm() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        
        // Form fields
        memberFirstNameField = new TextField();
        memberLastNameField = new TextField();
        memberEmailField = new TextField();
        memberDobField = new TextField();
        memberStreetField = new TextField();
        memberCityField = new TextField();
        memberPostalCodeField = new TextField();
        memberCountryField = new TextField();
        memberTypeCombo = new ComboBox<>();
        memberTypeCombo.getItems().addAll(MembershipType.values());
        memberTypeCombo.setValue(MembershipType.BASIC);
        
        // Form layout
        form.add(new Label("First Name:"), 0, 0);
        form.add(memberFirstNameField, 1, 0);
        form.add(new Label("Last Name:"), 2, 0);
        form.add(memberLastNameField, 3, 0);
        
        form.add(new Label("Email:"), 0, 1);
        form.add(memberEmailField, 1, 1);
        form.add(new Label("Date of Birth (yyyy-MM-dd):"), 2, 1);
        form.add(memberDobField, 3, 1);
        
        form.add(new Label("Street:"), 0, 2);
        form.add(memberStreetField, 1, 2);
        form.add(new Label("City:"), 2, 2);
        form.add(memberCityField, 3, 2);
        
        form.add(new Label("Postal Code:"), 0, 3);
        form.add(memberPostalCodeField, 1, 3);
        form.add(new Label("Country:"), 2, 3);
        form.add(memberCountryField, 3, 3);
        
        form.add(new Label("Membership Type:"), 0, 4);
        form.add(memberTypeCombo, 1, 4);
        
        // Add member button
        Button addMemberBtn = new Button("Add Member");
        addMemberBtn.setOnAction(e -> addMember());
        form.add(addMemberBtn, 0, 5);
        
        return form;
    }
    
    /**
     * Create the book form.
     */
    private GridPane createBookForm() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        
        // Form fields
        bookTitleField = new TextField();
        bookAuthorField = new TextField();
        bookIsbnField = new TextField();
        bookPublisherField = new TextField();
        bookPublicationDateField = new TextField();
        bookCategoryCombo = new ComboBox<>();
        bookCategoryCombo.getItems().addAll("FICTION", "NON_FICTION", "TECHNICAL", "EDUCATIONAL", "REFERENCE");
        bookCategoryCombo.setValue("FICTION");
        
        // Form layout
        form.add(new Label("Title:"), 0, 0);
        form.add(bookTitleField, 1, 0);
        form.add(new Label("Author:"), 2, 0);
        form.add(bookAuthorField, 3, 0);
        
        form.add(new Label("ISBN:"), 0, 1);
        form.add(bookIsbnField, 1, 1);
        form.add(new Label("Publisher:"), 2, 1);
        form.add(bookPublisherField, 3, 1);
        
        form.add(new Label("Publication Date (yyyy-MM-dd):"), 0, 2);
        form.add(bookPublicationDateField, 1, 2);
        form.add(new Label("Category:"), 2, 2);
        form.add(bookCategoryCombo, 3, 2);
        
        // Add book button
        Button addBookBtn = new Button("Add Book");
        addBookBtn.setOnAction(e -> addBook());
        form.add(addBookBtn, 0, 3);
        
        return form;
    }
    
    /**
     * Add a new member.
     */
    private void addMember() {
        try {
            String firstName = memberFirstNameField.getText().trim();
            String lastName = memberLastNameField.getText().trim();
            String email = memberEmailField.getText().trim();
            String dobStr = memberDobField.getText().trim();
            String street = memberStreetField.getText().trim();
            String city = memberCityField.getText().trim();
            String postalCode = memberPostalCodeField.getText().trim();
            String country = memberCountryField.getText().trim();
            MembershipType membershipType = memberTypeCombo.getValue();
            
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || dobStr.isEmpty()) {
                showAlert("Validation Error", "Please fill in all required fields", Alert.AlertType.WARNING);
                return;
            }
            
            LocalDate dateOfBirth = LocalDate.parse(dobStr, dateFormatter);
            
            Member member = libraryService.registerMember(firstName, lastName, email, dateOfBirth, 
                                                        street, city, postalCode, country, membershipType);
            refreshMemberData();
            clearMemberForm();
            
            showAlert("Success", "Member added successfully with ID: " + member.getId(), Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            logger.error("Error adding member", e);
            showAlert("Error", "Failed to add member: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Add a new book.
     */
    private void addBook() {
        try {
            String title = bookTitleField.getText().trim();
            String author = bookAuthorField.getText().trim();
            String isbn = bookIsbnField.getText().trim();
            String publisher = bookPublisherField.getText().trim();
            String pubDateStr = bookPublicationDateField.getText().trim();
            String categoryStr = bookCategoryCombo.getValue();
            
            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publisher.isEmpty() || pubDateStr.isEmpty()) {
                showAlert("Validation Error", "Please fill in all required fields", Alert.AlertType.WARNING);
                return;
            }
            
            LocalDate publicationDate = LocalDate.parse(pubDateStr, dateFormatter);
            ee.tak24.library.model.BookCategory category = ee.tak24.library.model.BookCategory.valueOf(categoryStr);
            
            Book book = libraryService.addBook(isbn, title, author, publisher, publicationDate, category, 1);
            refreshBookData();
            clearBookForm();
            
            showAlert("Success", "Book added successfully with ISBN: " + book.getIsbn(), Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            logger.error("Error adding book", e);
            showAlert("Error", "Failed to add book: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Refresh member data in the table.
     */
    private void refreshMemberData() {
        try {
            List<Member> members = libraryService.getAllMembers();
            memberData.clear();
            memberData.addAll(members);
        } catch (Exception e) {
            logger.error("Error refreshing member data", e);
            showAlert("Error", "Failed to refresh member data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Refresh book data in the table.
     */
    private void refreshBookData() {
        try {
            List<Book> books = libraryService.getAllBooks();
            bookData.clear();
            bookData.addAll(books);
        } catch (Exception e) {
            logger.error("Error refreshing book data", e);
            showAlert("Error", "Failed to refresh book data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Refresh statistics.
     */
    private void refreshStatistics(ListView<String> statsList) {
        try {
            ee.tak24.library.service.LibraryStatistics stats = libraryService.getLibraryStatistics();
            statsList.getItems().clear();
            
            statsList.getItems().add("Total Members: " + stats.getTotalMembers());
            statsList.getItems().add("Active Members: " + stats.getActiveMembers());
            statsList.getItems().add("Total Books: " + stats.getTotalBooks());
            statsList.getItems().add("Available Books: " + stats.getAvailableBooks());
            statsList.getItems().add("Borrowed Books: " + stats.getBorrowedBooks());
            statsList.getItems().add("Total Borrowing Records: " + stats.getTotalBorrowingRecords());
            statsList.getItems().add("Overdue Books: " + stats.getOverdueBorrowings());
            
        } catch (Exception e) {
            logger.error("Error refreshing statistics", e);
            showAlert("Error", "Failed to refresh statistics: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Clear the member form.
     */
    private void clearMemberForm() {
        memberFirstNameField.clear();
        memberLastNameField.clear();
        memberEmailField.clear();
        memberDobField.clear();
        memberStreetField.clear();
        memberCityField.clear();
        memberPostalCodeField.clear();
        memberCountryField.clear();
        memberTypeCombo.setValue(MembershipType.BASIC);
    }
    
    /**
     * Clear the book form.
     */
    private void clearBookForm() {
        bookTitleField.clear();
        bookAuthorField.clear();
        bookIsbnField.clear();
        bookPublisherField.clear();
        bookPublicationDateField.clear();
        bookCategoryCombo.setValue("FICTION");
    }
    
    /**
     * Show an alert dialog.
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Main method to launch the GUI application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
