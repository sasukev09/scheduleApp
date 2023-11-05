package Models;


/**
 * This class manages data associated Report screen, specifically the Report showing appointments by month and type.
 */
public class ReportByMonthType {
    /**
     * The month name.
     */
    private String month;

    /**
     * The type of appointment.
     */
    private String type;

    /**
     * The number of appointments in the same month and type.
     */
    private int count;

    /**
     * This is the constructor for the ReportByMonthType class.
     * @param month Name of the month.
     * @param type  Type of appointment.
     * @param count The number of appointments with the same month and type.
     */
    public ReportByMonthType(String month, String type, int count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    /**
     * This method gets the name of the month.
     * @return Returns the name of the month.
     */
    public String getMonth() {
        return month;
    }

    /**
     * This method gets the type of appointment.
     * @return Returns the type of appointment.
     */
    public String getType() {
        return type;
    }

    /**
     * This method gets the count of appointments with the same month and type.
     * @return Returns the number of appointments.
     */
    public int getCount() {
        return count;
    }
}
