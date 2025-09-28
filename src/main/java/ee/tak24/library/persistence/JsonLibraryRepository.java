package ee.tak24.library.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ee.tak24.library.model.*;
import ee.tak24.library.repository.LibraryRepository;
import ee.tak24.library.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * JSON-based implementation of LibraryRepository.
 * Demonstrates data persistence and file I/O operations.
 * 
 * This implementation provides persistent storage using JSON files,
 * with automatic loading and saving of data.
 */
public class JsonLibraryRepository implements LibraryRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonLibraryRepository.class);
    
    private final ObjectMapper objectMapper;
    private final String dataDirectory;
    private final Map<String, Member> members;
    private final Map<String, Book> books;
    private final Map<String, BorrowingRecord> borrowingRecords;
    private final IdGenerator idGenerator;

    /**
     * Constructor for JSON repository.
     * 
     * @param dataDirectory the directory to store JSON files
     */
    public JsonLibraryRepository(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.members = new ConcurrentHashMap<>();
        this.books = new ConcurrentHashMap<>();
        this.borrowingRecords = new ConcurrentHashMap<>();
        this.idGenerator = new IdGenerator();
        
        initializeDataDirectory();
        loadData();
    }

    /**
     * Constructor with default data directory.
     */
    public JsonLibraryRepository() {
        this.dataDirectory = "data";
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.members = new ConcurrentHashMap<>();
        this.books = new ConcurrentHashMap<>();
        this.borrowingRecords = new ConcurrentHashMap<>();
        this.idGenerator = new IdGenerator();
        
        initializeDataDirectory();
        loadData();
    }

    // Member operations
    @Override
    public void saveMember(Member member) {
        members.put(member.getId(), member);
        saveMembersToFile();
        logger.debug("Member saved: {}", member.getId());
    }

    @Override
    public Optional<Member> findMemberById(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    @Override
    public List<Member> findAllMembers() {
        return new ArrayList<>(members.values());
    }

    @Override
    public void deleteMember(String memberId) {
        members.remove(memberId);
        saveMembersToFile();
        logger.debug("Member deleted: {}", memberId);
    }

    @Override
    public boolean memberExists(String memberId) {
        return members.containsKey(memberId);
    }

    // Book operations
    @Override
    public void saveBook(Book book) {
        books.put(book.getIsbn(), book);
        saveBooksToFile();
        logger.debug("Book saved: {}", book.getIsbn());
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    @Override
    public List<Book> findAllBooks() {
        return new ArrayList<>(books.values());
    }

    @Override
    public List<Book> findBooksByCategory(BookCategory category) {
        return books.values().stream()
                .filter(book -> book.getCategory() == category)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBook(String isbn) {
        books.remove(isbn);
        saveBooksToFile();
        logger.debug("Book deleted: {}", isbn);
    }

    @Override
    public boolean bookExists(String isbn) {
        return books.containsKey(isbn);
    }

    // Borrowing record operations
    @Override
    public void saveBorrowingRecord(BorrowingRecord record) {
        borrowingRecords.put(record.getId(), record);
        saveBorrowingRecordsToFile();
        logger.debug("Borrowing record saved: {}", record.getId());
    }

    @Override
    public Optional<BorrowingRecord> findBorrowingRecordById(String recordId) {
        return Optional.ofNullable(borrowingRecords.get(recordId));
    }

    @Override
    public List<BorrowingRecord> findBorrowingRecordsByMember(String memberId) {
        return borrowingRecords.values().stream()
                .filter(record -> record.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingRecord> findBorrowingRecordsByBook(String bookIsbn) {
        return borrowingRecords.values().stream()
                .filter(record -> record.getBookIsbn().equals(bookIsbn))
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingRecord> findActiveBorrowingRecords() {
        return borrowingRecords.values().stream()
                .filter(record -> record.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingRecord> findOverdueBorrowingRecords() {
        LocalDate today = LocalDate.now();
        return borrowingRecords.values().stream()
                .filter(record -> record.getReturnDate() == null && record.getDueDate().isBefore(today))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBorrowingRecord(String recordId) {
        borrowingRecords.remove(recordId);
        saveBorrowingRecordsToFile();
        logger.debug("Borrowing record deleted: {}", recordId);
    }

    @Override
    public boolean borrowingRecordExists(String recordId) {
        return borrowingRecords.containsKey(recordId);
    }

    // Statistics
    @Override
    public int getTotalMemberCount() {
        return members.size();
    }

    @Override
    public int getTotalBookCount() {
        return books.size();
    }

    @Override
    public int getTotalBorrowingRecordCount() {
        return borrowingRecords.size();
    }

    @Override
    public int getActiveBorrowingCount() {
        return findActiveBorrowingRecords().size();
    }

    @Override
    public int getOverdueBorrowingCount() {
        return findOverdueBorrowingRecords().size();
    }

    // Private helper methods
    private void initializeDataDirectory() {
        try {
            Path dataPath = Paths.get(dataDirectory);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                logger.info("Created data directory: {}", dataDirectory);
            }
        } catch (IOException e) {
            logger.error("Failed to create data directory: {}", dataDirectory, e);
            throw new RuntimeException("Failed to initialize data directory", e);
        }
    }

    private void loadData() {
        loadMembers();
        loadBooks();
        loadBorrowingRecords();
        logger.info("Data loaded successfully - Members: {}, Books: {}, Records: {}", 
                   members.size(), books.size(), borrowingRecords.size());
    }

    private void loadMembers() {
        try {
            File membersFile = new File(dataDirectory, "members.json");
            if (membersFile.exists()) {
                List<Member> memberList = objectMapper.readValue(membersFile, new TypeReference<List<Member>>() {});
                members.clear();
                memberList.forEach(member -> members.put(member.getId(), member));
                logger.debug("Loaded {} members from file", members.size());
            }
        } catch (IOException e) {
            logger.error("Failed to load members from file", e);
        }
    }

    private void loadBooks() {
        try {
            File booksFile = new File(dataDirectory, "books.json");
            if (booksFile.exists()) {
                List<Book> bookList = objectMapper.readValue(booksFile, new TypeReference<List<Book>>() {});
                books.clear();
                bookList.forEach(book -> books.put(book.getIsbn(), book));
                logger.debug("Loaded {} books from file", books.size());
            }
        } catch (IOException e) {
            logger.error("Failed to load books from file", e);
        }
    }

    private void loadBorrowingRecords() {
        try {
            File recordsFile = new File(dataDirectory, "borrowing_records.json");
            if (recordsFile.exists()) {
                List<BorrowingRecord> recordList = objectMapper.readValue(recordsFile, new TypeReference<List<BorrowingRecord>>() {});
                borrowingRecords.clear();
                recordList.forEach(record -> borrowingRecords.put(record.getId(), record));
                logger.debug("Loaded {} borrowing records from file", borrowingRecords.size());
            }
        } catch (IOException e) {
            logger.error("Failed to load borrowing records from file", e);
        }
    }

    private void saveMembersToFile() {
        try {
            File membersFile = new File(dataDirectory, "members.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(membersFile, new ArrayList<>(members.values()));
            logger.debug("Saved {} members to file", members.size());
        } catch (IOException e) {
            logger.error("Failed to save members to file", e);
        }
    }

    private void saveBooksToFile() {
        try {
            File booksFile = new File(dataDirectory, "books.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(booksFile, new ArrayList<>(books.values()));
            logger.debug("Saved {} books to file", books.size());
        } catch (IOException e) {
            logger.error("Failed to save books to file", e);
        }
    }

    private void saveBorrowingRecordsToFile() {
        try {
            File recordsFile = new File(dataDirectory, "borrowing_records.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(recordsFile, new ArrayList<>(borrowingRecords.values()));
            logger.debug("Saved {} borrowing records to file", borrowingRecords.size());
        } catch (IOException e) {
            logger.error("Failed to save borrowing records to file", e);
        }
    }

    /**
     * Saves all data to files.
     * This method can be called to ensure all data is persisted.
     */
    public void saveAllData() {
        saveMembersToFile();
        saveBooksToFile();
        saveBorrowingRecordsToFile();
        logger.info("All data saved to files");
    }

    /**
     * Gets the data directory path.
     * 
     * @return the data directory path
     */
    public String getDataDirectory() {
        return dataDirectory;
    }
}
