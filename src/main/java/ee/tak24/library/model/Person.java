package ee.tak24.library.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Base class representing a person in the library system.
 * Demonstrates encapsulation and basic OOP principles.
 */
public abstract class Person {
    private final String id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private Address address;

    /**
     * Constructor for creating a new Person.
     * 
     * @param id unique identifier for the person
     * @param firstName person's first name
     * @param lastName person's last name
     * @param email person's email address
     * @param dateOfBirth person's date of birth
     * @param address person's address (composition relationship)
     */
    protected Person(String id, String firstName, String lastName, String email, 
                    LocalDate dateOfBirth, Address address) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "Date of birth cannot be null");
        this.address = Objects.requireNonNull(address, "Address cannot be null");
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    // Setters with validation
    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName, "First name cannot be null");
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName, "Last name cannot be null");
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email cannot be null");
    }

    public void setAddress(Address address) {
        this.address = Objects.requireNonNull(address, "Address cannot be null");
    }

    /**
     * Abstract method that must be implemented by subclasses.
     * Demonstrates polymorphism and inheritance.
     * 
     * @return a string representation of the person's role
     */
    public abstract String getRole();

    /**
     * Gets the full name of the person.
     * 
     * @return concatenated first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Calculates the age of the person based on their date of birth.
     * 
     * @return age in years
     */
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s %s', email='%s', age=%d}", 
                           getClass().getSimpleName(), id, firstName, lastName, email, getAge());
    }
}
