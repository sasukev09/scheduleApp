package Models;

/**
 * This class manages Country data.
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
     * This is the constructor for new Country object.
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

//    /**
//     * This method gets the country ID number of the Country object.
//     * @return Returns the ID number of country.
//     */
//    public int getCountryID()
//    {
//        return countryID;
//    }
//
//    /**
//     * This method sets country ID.
//     * @param countryID ID of country
//     */
//    public void setCountryID(int countryID)
//    {
//        this.countryID = countryID;
//    }
//
//    /**
//     * This method sets country's name.
//     * @param countryName Name of country
//     */
//    public void setCountryName(String countryName)
//    {
//        this.countryName = countryName;
//    }

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

