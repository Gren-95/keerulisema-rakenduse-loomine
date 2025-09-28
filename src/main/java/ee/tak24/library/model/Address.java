package ee.tak24.library.model;

import java.util.Objects;

/**
 * Value object representing an address. Demonstrates composition and
 * immutability principles.
 */
public class Address {

    private final String street;
    private final String city;
    private final String postalCode;
    private final String country;

    /**
     * Constructor for creating a new Address.
     *
     * @param street street address
     * @param city city name
     * @param postalCode postal/zip code
     * @param country country name
     */
    public Address(String street, String city, String postalCode, String country) {
        this.street = Objects.requireNonNull(street, "Street cannot be null");
        this.city = Objects.requireNonNull(city, "City cannot be null");
        this.postalCode = Objects.requireNonNull(postalCode, "Postal code cannot be null");
        this.country = Objects.requireNonNull(country, "Country cannot be null");
    }

    // Getters only - immutable object
    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    /**
     * Gets the full address as a formatted string.
     *
     * @return formatted address string
     */
    public String getFullAddress() {
        return String.format("%s, %s %s, %s", street, city, postalCode, country);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Address address = (Address) obj;
        return Objects.equals(street, address.street)
                && Objects.equals(city, address.city)
                && Objects.equals(postalCode, address.postalCode)
                && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, postalCode, country);
    }

    @Override
    public String toString() {
        return getFullAddress();
    }
}
