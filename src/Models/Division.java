package Models;


import DAO.DBCountries;

/**
 * This class manages all Division data.
 */
public class Division {

    /**
     * The ID number of the Division object.
     */
    private int divisionID;

    /**
     * The division name of the Division object.
     */
    private String divisionName;

    /**
     * The country ID number of the Division object.
     */
    private int countryID;

    /**
     * This is the constructor for Division class.
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
     * This method gets the division ID number of the Division object.
     * @return Returns the division ID number.
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * This method gets the division name of the Division object.
     * @return Returns name of division.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * This method gets country ID number of the Division object.
     * @return Returns the ID number of country.
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * This method gets the country name of the Division object.
     * @param countryID ID number of country
     * @return Returns name of country.
     */
    public String getCountryName(int countryID)
    {
        String countryName = "";

        try
        {
            Country c = DBCountries.getCountry(countryID);
            countryName = c.getCountryName();
        }

        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        return countryName;
    }

//    /**
//     * sets division ID number
//     * @param divisionID ID number of division
//     */
//    public void setDivisionID(int divisionID) {
//        this.divisionID = divisionID;
//    }
//
//    /**
//     * sets division name
//     * @param divisionName name of division
//     */
//    public void setDivisionName(String divisionName) {
//        this.divisionName = divisionName;
//    }
//
//    /**
//     * sets country ID number
//     * @param countryID ID number of country
//     */
//    public void setCountryID(int countryID) {
//        this.countryID = countryID;
//    }

    /**
     * Overrides the toString() method.
     * @return Returns divison name of the Division object.
     */
    @Override
    public String toString()
    {
        return divisionName;
    }

}
