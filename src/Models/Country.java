package Models;

/**
 * This class manages the Country data.
 */
public class Country {

    /**
     * The country ID number of the Country object.
     */
    private int countryID;

    /**
     * The country name of the Country object.
     */
    private String countryName;

    /**
     * This is the constructor for a new Country.
     *
     * @param id ID of country (int)
     * @param name Name of country (String)
     */
    public Country(int id, String name)
    {
        this.countryID = id;
        this.countryName = name;
    }

    /**
     * This method gets the country name of Country object.
     *
     * @return Returns the country name.
     */
    public String getCountryName()
    {
        return countryName;
    }

    /**
     * This method overrides the toString() method.
     * @return Returns country name of Country object.
     */
    @Override
    public String toString()
    {
        return this.getCountryName();
    }
}

