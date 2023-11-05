package Models;


import javafx.collections.ObservableList;

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
     * This method gets the ID number of Contact object.
     *
     * @return ID number of contact
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * The method gets the contact name of the Contact object.
     *
     * @return name of contact
     */
    public String getContactName() {
        return contactName;
    }

//    /**
//     * Sets ID number of contact
//     *
//     * @param contactID contact ID number
//     */
//    public void setContactID(int contactID) {
//        this.contactID = contactID;
//    }
//
//    /**
//     * sets name of contact
//     *
//     * @param contactName name of contact
//     */
//    public void setContactName(String contactName) {
//        this.contactName = contactName;
//    }
//
//    /**
//     * gets contact email address
//     * @return email address of contact
//     */
//    public String getContactEmail() {
//        return contactEmail;
//    }
//
//    /**
//     * sets email address of contact
//     *
//     * @param contactEmail email address of contact
//     */
//    public void setContactEmail(String contactEmail) {
//        this.contactEmail = contactEmail;
//    }

}
