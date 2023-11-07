package Models;


import DAO.DAOCountries;

/**
 * This class manages Division data.
 */
public class Division {

    /**
     * The ID number of a Division
     */
    private int divisionID;

    /**
     * The division name of a Division
     */
    private String divisionName;

    /**
     * The country ID number of a Division
     */
    private int countryID;

    /**
     * This is the constructor of a Division
     * @param divisionID ID number of division
     * @param divisionName Name of Division
     * @param countryID ID number of Division country
     */
    public Division(int divisionID, String divisionName, int countryID)
    {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**
     * This method gets the division ID number of a Division
     * @return Returns the division ID number.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * This method gets the division name of a Division
     * @return Returns name of division.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * This method gets country ID number of a Division
     * @return Returns the ID number of country.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * This method gets the country name of a Division
     * @param countryID ID number of country
     * @return Returns name of country.
     */
    public String getCountryName(int countryID)
    {
        String countryName = "";

        try
        {
            Country c = DAOCountries.getCountry(countryID);
            countryName = c.getCountryName();
        }

        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return countryName;
    }


    /**
     * Overrides the toString() method.
     * @return Returns divison name of a Division
     */
    @Override
    public String toString()
    {
        return divisionName;
    }

}
