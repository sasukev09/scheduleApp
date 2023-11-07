package Models;

/**
 * This class manages Contact data.
 */
public class Contact {

    /**
     * The contact ID number of the Contact object.
     */
    private int contactID;

    /**
     * The contact name of the Contact object.
     */
    private String contactName;

    /**
     * The email address of the Contact object.
     */
    private String contactEmail;

    /**
     * This is the constructor for new Contact objects.
     *
     * @param contactID ID number of contact
     * @param contactName name of contact
     * @param contactEmail email of contact
     */
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * This method gets the ID number for a Contact.
     *
     * @return ID number of contact
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * The method gets the contact name for a Contact.
     *
     * @return name of contact
     */
    public String getContactName() {
        return contactName;
    }

}
