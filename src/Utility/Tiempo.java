package Utility;


import Controller.UpdateAppointmentScreenController;
import Models.Appointment;
import Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * This class handles time conversion duties, Tiempo is time in spanish.
 */
public class Tiempo {

    /**
     * This method converts String values of time and date into formatted Timestamp date/time.
     *
     * @param time String value of time
     * @param date String value of date
     * @return Returns Timestamp value date/time.
     */
    public static Timestamp convertStringTimeDate2TimeStamp(String time, String date)
    {
        DateTimeFormatter formatHourMin = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatYearMonthDay = DateTimeFormatter.ofPattern("yyy-MM-dd");

        LocalTime localTime = LocalTime.parse(time, formatHourMin);
        LocalDate localDate = LocalDate.parse(date,formatYearMonthDay);
        LocalDateTime LDT = LocalDateTime.of(localDate, localTime);

        return Timestamp.valueOf(LDT);
    }

    /**
     * This method adds the system default time zone to a LocalDateTime variable.
     * @param dateTime LocalDateTime variable to which the user would like specify as the system default timezone.
     * @return Returns LocalDateTime value set to system default time zone.
     */
    public static LocalDateTime attachLocalTimeZone(LocalDateTime dateTime)
    {
        ZonedDateTime ZDT = dateTime.atZone(ZoneId.systemDefault());
        return ZDT.toLocalDateTime();
    }

    /**
     * This method adds Eastern Standard Time zone to the LocalDateTime arg.
     * @param dateTime LocalDateTime in which the user would like to specify as EST.
     * @return Returns LocalDateTime value set to EST.
     */
    public static LocalDateTime attachESTTimeZone(LocalDateTime dateTime)
    {
        ZonedDateTime ZDT = dateTime.atZone(ZoneId.of("America/New_York"));
        return ZDT.toLocalDateTime();
    }

    /**
     * This method accepts a LocalDateTime argument and converts the timezone it is associated with from EST to system default time zone.
     * @param dateTime LocalDateTime in which the user would like to convert from EST to system default time zone.
     * @return Returns LocalDateTime value set to system default time zone.
     */
    public static LocalDateTime convertESTToLocalTimeZone(LocalDateTime dateTime)
    {
        ZonedDateTime ZDT = dateTime.atZone(ZoneId.of("America/New_York"));
        ZDT = ZDT.withZoneSameInstant(ZoneId.systemDefault());

        return ZDT.toLocalDateTime();
    }


    /**
     * This method evaluates if the requested appointment start/end times are during business hours (8:00AM - 10:00PM EST).
     * @param requestedStartLDT user requested appointment start time
     * @param requestedEndLDT user requested appointment end time
     * @return Returns true if appointment start/end times are within business hours, false if not.
     * @throws DateTimeException In the event of a DateTime error.
     */
    public static boolean businessHours(LocalDateTime requestedStartLDT, LocalDateTime requestedEndLDT) throws DateTimeException
    {
        //establish user requested Local date zone in zone ID from the system
        requestedStartLDT = Tiempo.attachLocalTimeZone(requestedStartLDT);
        requestedEndLDT = Tiempo.attachLocalTimeZone(requestedEndLDT);


        System.out.println("Requested start time in local time = " + requestedStartLDT);
        System.out.println("Requested end time in local time = " + requestedEndLDT);

        //getting local time values
        LocalTime requestedStartTime = requestedStartLDT.toLocalTime();
        LocalTime requestedEndTime = requestedEndLDT.toLocalTime();


        //setting up business hours in EST
        LocalDateTime businessHourStartTimeEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0));      // ---   8:00 am start of workday
        businessHourStartTimeEST = Tiempo.attachESTTimeZone(businessHourStartTimeEST);                                 // ---   set timezone to EST

        LocalDateTime workdayStartLDT = Tiempo.convertESTToLocalTimeZone(businessHourStartTimeEST);               // ---   set variable to LDT equivalent
        LocalTime workdayStartLocalTime = workdayStartLDT.toLocalTime();                                            // ---   extract local time


        LocalDateTime businessHourEndTimeEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));       // ---   10:00 am end of workday
        businessHourEndTimeEST = Tiempo.attachESTTimeZone(businessHourEndTimeEST);                                     // ---   set timezone to EST

        LocalDateTime workdayEndLDT = Tiempo.convertESTToLocalTimeZone(businessHourEndTimeEST);                   // ---   set variable to LDT equivalent
        LocalTime workdayEndLocalTime = workdayEndLDT.toLocalTime();                                                // ---   extract local time


        System.out.println("Workday start time in local time - " + workdayStartLocalTime.toString() + ". Requested start time - " + requestedStartTime.toString());
        System.out.println("Workday end time in local time - " + workdayEndLocalTime.toString() + ". Requested end time - " + requestedEndTime.toString());

        //for start and end times, if they are within business hours
        if ((requestedStartTime.equals(workdayStartLocalTime) || requestedStartTime.isAfter(workdayStartLocalTime)) && requestedStartTime.isBefore(workdayEndLocalTime) &&
                (requestedEndTime.isAfter(workdayStartLocalTime)) && (requestedEndTime.equals(workdayEndLocalTime) || requestedEndTime.isBefore(workdayEndLocalTime)))
        {
            return true;
        }
        return false;

    }

    /**
     * This method evaluates if user created appointment date/time will overlap any existing appointments.
     * @param newAppointment true if this appointment is a new appointment to be added, false if modifying existing appointment
     * @param customer Customer in which appointment is being scheduled
     * @param requestStartLocalDateTime user requested appointment start date/time
     * @param requestEndLocalDateTime user requested appointment end date/time
     * @return Returns 1 if overlap is found for user requested start date/time, returns 2 if overlap is found for user
     * requested end date/time, returns 3 if an appointment is already scheduled  between user requested start/end
     * date/time, returns 4 if no overlap or no appointments are found for customer.
     */
    public static int overlappingTime(boolean newAppointment, Customer customer, LocalDateTime requestStartLocalDateTime, LocalDateTime requestEndLocalDateTime)
    {
        ObservableList<Appointment> customerAppointments = customer.getCustomerAppointmentList();
        ObservableList<Appointment> updatedCustomerAppointments = FXCollections.observableArrayList();

        System.out.println(customer.getCustomerName());

        //if modified, remove appointment to be modified from list
        if (customer.hasAppointments() && !newAppointment)
        {
            for (Appointment removeApt : customerAppointments)
            {
                if (removeApt.getAppointmentID() != UpdateAppointmentScreenController.selectedAppointment.getAppointmentID())
                {
                    updatedCustomerAppointments.add(removeApt);
                }
            }
            customerAppointments = updatedCustomerAppointments;
        }

        if (customer.hasAppointments()) {

            System.out.println(customer.getCustomerName() + " has " + customerAppointments.size() + " appointments");

            for (Appointment apt : customerAppointments)
            {
                //getting start and end times for appointment
                LocalDateTime thisAppointmentStart = apt.getAppointmentStart();
                LocalDateTime thisAppointmentEnd = apt.getAppointmentEnd();

                System.out.println("This appointment start/end : " + thisAppointmentStart + " - " + thisAppointmentEnd);
                System.out.println("Requested start/end : " + requestStartLocalDateTime + " - " + requestEndLocalDateTime);

                //checking start times
                if (requestStartLocalDateTime.equals(thisAppointmentStart) ||
                        (requestStartLocalDateTime.isAfter(thisAppointmentStart) && requestStartLocalDateTime.isBefore(thisAppointmentEnd)))
                {
                    System.out.println("This appointment start time/date conflicts with existing appointment ID " + apt.getAppointmentID());
                    return 1;
                }
                //checking end times
                else if (requestEndLocalDateTime.equals(thisAppointmentEnd) ||
                        ((requestEndLocalDateTime.isAfter(thisAppointmentStart) && requestEndLocalDateTime.isBefore(thisAppointmentEnd))))
                {
                    System.out.println("This appointment end time/date conflicts with existing appointment ID " + apt.getAppointmentID());
                    return 2;
                }
                //checking if created appointment clashes with an ongoing appointment
                else if (requestStartLocalDateTime.isBefore(thisAppointmentStart) && requestEndLocalDateTime.isAfter(thisAppointmentEnd))
                {
                    System.out.println("Appointment ID " + apt.getAppointmentID() + " is during the requested start/end time.");
                    return 3;
                }

            }
            //no issues detected
            System.out.println(customer.getCustomerName() + " has appointments today, no issues detected.");
        }

        //if a customer has no appointments
        else
        {
            System.out.println(customer.getCustomerName() + " has no appointments. Adding new appointment.");
        }
        return 4;
    }

}
