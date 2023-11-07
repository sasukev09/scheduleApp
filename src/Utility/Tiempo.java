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
 * This class handles time conversion duties.
 */
public class Tiempo {

    /**
     * This method converts String values of time and date into properly formatted Timestamp date/time.
     *
     * @param time String value of time
     * @param date String value of date
     * @return Returns Timestamp value date/time.
     */
    public static Timestamp convertStringTimeDate2TimeStamp(String time, String date)        // not UTC
    {
        DateTimeFormatter hourMinFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");

        LocalTime localTime = LocalTime.parse(time, hourMinFormatter);
        LocalDate localDate = LocalDate.parse(date,dateFormatter);
        LocalDateTime ldt = LocalDateTime.of(localDate, localTime);

        return Timestamp.valueOf(ldt);
    }

    /**
     * This method adds the system default time zone to a LocalDateTime variable.
     * @param dateTime LocalDateTime variable to which the user would like specify as the system default timezone.
     * @return Returns LocalDateTime value set to system default time zone.
     */
    public static LocalDateTime attachLocalTimeZone(LocalDateTime dateTime)
    {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault());

        return zdt.toLocalDateTime();
    }

    /**
     * This method adds Eastern Standard Time zone to the LocalDateTime arg.
     * @param dateTime LocalDateTime in which the user would like to specify as EST.
     * @return Returns LocalDateTime value set to EST.
     */
    public static LocalDateTime attachESTTimeZone(LocalDateTime dateTime)
    {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.of("America/New_York"));
//        zdt = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));

        return zdt.toLocalDateTime();
    }

    /**
     * This method accepts a LocalDateTime argument and converts the timezone it is associated with from EST to system default time zone.
     * @param dateTime LocalDateTime in which the user would like to convert from EST to system default time zone.
     * @return Returns LocalDateTime value set to system default time zone.
     */
    public static LocalDateTime convertESTToLocalTimeZone(LocalDateTime dateTime)
    {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.of("America/New_York"));
        zdt = zdt.withZoneSameInstant(ZoneId.systemDefault());

        return zdt.toLocalDateTime();
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

        //   >>----->   establish user requested LDT in Zone ID system.default()   <-----<<
        requestedStartLDT = Tiempo.attachLocalTimeZone(requestedStartLDT);
        requestedEndLDT = Tiempo.attachLocalTimeZone(requestedEndLDT);


        System.out.println("Requested start time in local time = " + requestedStartLDT);
        System.out.println("Requested end time in local time = " + requestedEndLDT);

        //   >>----->   extract local time values   <-----<<
        LocalTime requestedStartTime = requestedStartLDT.toLocalTime();
        LocalTime requestedEndTime = requestedEndLDT.toLocalTime();


        //   >>----->   establish business hours in EST   <-----<<
        LocalDateTime workdayStartTimeEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0));      // ---   8:00 am start of workday
        workdayStartTimeEST = Tiempo.attachESTTimeZone(workdayStartTimeEST);                                 // ---   set timezone to EST

        LocalDateTime workdayStartLDT = Tiempo.convertESTToLocalTimeZone(workdayStartTimeEST);               // ---   set variable to LDT equivalent
        LocalTime workdayStartLocalTime = workdayStartLDT.toLocalTime();                                            // ---   extract local time


        LocalDateTime workdayEndTimeEST = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0));       // ---   10:00 am end of workday
        workdayEndTimeEST = Tiempo.attachESTTimeZone(workdayEndTimeEST);                                     // ---   set timezone to EST

        LocalDateTime workdayEndLDT = Tiempo.convertESTToLocalTimeZone(workdayEndTimeEST);                   // ---   set variable to LDT equivalent
        LocalTime workdayEndLocalTime = workdayEndLDT.toLocalTime();                                                // ---   extract local time


        System.out.println("Workday start time in local time - " + workdayStartLocalTime.toString() + ". Requested start time - " + requestedStartTime.toString());
        System.out.println("Workday end time in local time - " + workdayEndLocalTime.toString() + ". Requested end time - " + requestedEndTime.toString());

        //   >>----->   evaluate if start/end times are during business hours   <-----<<
        //   >>----->   note requestedStartTime >= workdayStartLocalTime; requestedEndTime <= workdayEndLocalTime   <-----<<
        if ((requestedStartTime.equals(workdayStartLocalTime) || requestedStartTime.isAfter(workdayStartLocalTime)) && requestedStartTime.isBefore(workdayEndLocalTime) &&
                (requestedEndTime.isAfter(workdayStartLocalTime)) && (requestedEndTime.equals(workdayEndLocalTime) || requestedEndTime.isBefore(workdayEndLocalTime)))
        {
            return true;
        }

        return false;

    }


    //   >>----->   This is commented out. Per program instructor, appointments can be scheduled 7 days/week   <-----<<
    //   >>----->   It is commented out on AddAppointmentScreenController and ModifyScreenController as well.   <-----<<
    //   >>----->   I chose not to delete the code in case it was a traditional 5 day workweek.   <-----<<
//    /**
//     * This method evaluates if user requested appointment dates are during a Monday - Friday workweek
//     * @param requestedStartLDT user requested start date
//     * @param requestedEndLDT user requested end date
//     * @return true if start/end dates are Monday - Friday, false if not
//     */
//    public static boolean isMondayThruFriday(LocalDateTime requestedStartLDT, LocalDateTime requestedEndLDT)
//    {
//        DayOfWeek workWeekStartDay = DayOfWeek.MONDAY;
//        DayOfWeek workWeekEndDay = DayOfWeek.FRIDAY;
//
//        if ((workWeekEndDay.getValue() < requestedStartLDT.getDayOfWeek().getValue()) ||
//                (requestedStartLDT.getDayOfWeek().getValue() < workWeekStartDay.getValue()) ||
//                ((workWeekEndDay.getValue() < requestedEndLDT.getDayOfWeek().getValue()) ||
//                        (requestedEndLDT.getDayOfWeek().getValue() < workWeekStartDay.getValue())))
//        {
//            return false;
//        }
//        return true;
//    }


    /**
     * This method evaluates if user requested appointment date/time will overlap any existing appointments.
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

        //   >>---------->   if modifying appointment, remove appointment to be modified from list   <----------<<
        if (customer.hasAppointments() && !newAppointment)
        {
            for (Appointment a : customerAppointments)
            {
                if (a.getAppointmentID() != UpdateAppointmentScreenController.selectedAppointment.getAppointmentID())
                {
                    updatedCustomerAppointments.add(a);
                }
            }
            customerAppointments = updatedCustomerAppointments;
        }

        if (customer.hasAppointments()) {


            System.out.println(customer.getCustomerName() + " has " + customerAppointments.size() + " appointments");

            for (Appointment a : customerAppointments)
            {
                // >>----->  get appointment start/end times   <-----<<
                LocalDateTime thisAppointmentStart = a.getAppointmentStart();
                LocalDateTime thisAppointmentEnd = a.getAppointmentEnd();

                System.out.println("This appointment start/end : " + thisAppointmentStart + " - " + thisAppointmentEnd);
                System.out.println("Requested start/end : " + requestStartLocalDateTime + " - " + requestEndLocalDateTime);

                // >>----->  check start times   <-----<<
                if (requestStartLocalDateTime.equals(thisAppointmentStart) ||
//                        (requestedStartLDT.equals(thisAppointmentEnd)) ||         //   commented out if back to back appointments not allowed
                        (requestStartLocalDateTime.isAfter(thisAppointmentStart) && requestStartLocalDateTime.isBefore(thisAppointmentEnd)))
                {
                    System.out.println("This appointment start time/date conflicts with existing appointment ID " + a.getAppointmentID());
                    return 1;
                }
                // >>----->  check end times   <-----<<
                else if (requestEndLocalDateTime.equals(thisAppointmentEnd) ||
//                        (requestedEndLDT.equals(thisAppointmentStart)) ||         //   commented out if back to back appointments not allowed
                        ((requestEndLocalDateTime.isAfter(thisAppointmentStart) && requestEndLocalDateTime.isBefore(thisAppointmentEnd))))
                {
                    System.out.println("This appointment end time/date conflicts with existing appointment ID " + a.getAppointmentID());
                    return 2;
                }
                // >>----->  check if requested times encompass an existing appointment  <-----<<
                else if (requestStartLocalDateTime.isBefore(thisAppointmentStart) && requestEndLocalDateTime.isAfter(thisAppointmentEnd))
                {
                    System.out.println("Appointment ID " + a.getAppointmentID() + " is during the requested start/end time.");
                    return 3;
                }

            }
            // >>----->  customer has appointments but no conflicts found   <-----<<
            System.out.println(customer.getCustomerName() + " has appointments today, but no conflicts detected.");
        }

        // >>----->  customer has no appointments   <-----<<
        else //if (!customer.hasAppointments())
        {
            System.out.println(customer.getCustomerName() + " has no appointments. Adding this appointment.");
        }
        return 4;
    }

}
