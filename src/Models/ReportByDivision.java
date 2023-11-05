package Models;


/**
 * This class manages data associated Report screen, specifically the Report showing the number of customers per division.
 */
public class ReportByDivision {

    /**
     * Name of division.
     */
    private String division;

    /**
     * The number of customers in the division.
     */
    private int count;

    /**
     * This is the constructor for ReportByDivision objects.
     * @param division Name of division.
     * @param count Number of customers in division.
     */
    public ReportByDivision(String division, int count) {
        this.division = division;
        this.count = count;
    }

    /**
     * This method gets the division name.
     * @return Returns name of division.
     */
    public String getDivision() {
        return division;
    }

    /**
     * This method gets the number of customers in the division.
     * @return Returns number of customers in the division.
     */
    public int getCount() {
        return count;
    }
}
