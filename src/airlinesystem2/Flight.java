package airlinesystem2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Flight{

    private String flightID, destination;
    private double econPrice, businessPrice, premiumPrice;
    private Date departDate;
    private int econSeats, businessSeats, premiumSeats;
    private static String FLIGHT_FILE = "Flight.txt";
    private static ArrayList<Flight> flights = new ArrayList<>();
    private static ArrayList<String[]> flightData = new ArrayList<>();
    private static int currentSeats = 0;

    public Flight() {
        this(" ", " ", 0.0, 0.0, 0.0, 0, 0, 0);
        departDate = new Date(0);
        flights.clear();
        flightData.clear();
    }

    public Flight(String flightID, String destination, double econPrice, double businessPrice, double premiumPrice, int econSeats,
            int businessSeats, int premiumSeats) {
        this.flightID = flightID;
        this.destination = destination;
        this.econPrice = econPrice;
        this.businessPrice = businessPrice;
        this.premiumPrice = premiumPrice;
        this.econSeats = econSeats;
        this.businessSeats = businessSeats;
        this.premiumSeats = premiumSeats;
    }

    public String getFlightID() {
        return flightID;
    }

    public String getDestination() {
        return destination;
    }

    public double getEconPrice() {
        return econPrice;
    }

    public double getBusinessPrice() {
        return businessPrice;
    }

    public double getPremiumPrice() {
        return premiumPrice;
    }

    public Date getDepartDate() {
        return departDate;
    }

    public int getEconSeats() {
        return econSeats;
    }

    public int getBusinessSeats() {
        return businessSeats;
    }

    public int getPremiumSeats() {
        return premiumSeats;
    }

    public static ArrayList<String[]> getFlightData() {
        return flightData;
    }
    
    //setter method
    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setEconPrice(double econPrice) {
        this.econPrice = econPrice;
    }

    public void setBusinessPrice(double businessPrice) {
        this.businessPrice = businessPrice;
    }

    public void setPremiumPrice(double premiumPrice) {
        this.premiumPrice = premiumPrice;
    }

    public void setDepartDate(Date departDate) {
        this.departDate = departDate;
    }

    public void setEconSeats(int econSeats) {
        this.econSeats = econSeats;
    }

    public void setBusinessSeats(int businessSeats) {
        this.businessSeats = businessSeats;
    }

    public void setPremiumSeats(int premiumSeats) {
        this.premiumSeats = premiumSeats;
    }

    @Override
    public String toString() {
        return flightID + "|" + econPrice + "|" + businessPrice + "|" + premiumPrice + "|" + destination + "|"
                + new SimpleDateFormat("dd/MM/yyyy").format(departDate) + "|" + econSeats + "|" + businessSeats + "|" + premiumSeats;
    }

    public static ArrayList<Flight> displayFlightDetails() throws ParseException {
        System.out.println("             ===============================================================================================================================================================");
        System.out.println("            |                                                                   ALL FLIGHT DETAILS                                                                          |");
        System.out.println("            |===============================================================================================================================================================|");
        System.out.println("            | No.  | Flight | Economy Price  | Business Price | Premium  Price |     Destination     | Departure Date |     Economy     |    Business     |     Premium     |");
        System.out.println("            |      |   ID   | (RM) (One way) | (RM) (One way) | (RM) (One way) |                     |                |  Available Seat |  Available Seat |  Available Seat |");
        System.out.println("            |______|________|________________|________________|________________|_____________________|________________|_________________|_________________|_________________|");
        int count = 1;  // For displaying the flight number
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(FLIGHT_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Flight flight = new Flight(line);
                    flights.add(flight);

                    System.out.format("            | %-4d | %-6s | %-14.2f | %-14.2f | %-14.2f | %-19s | %-14s | %-15d | %-15d | %-15d |\n",
                            count++, flight.flightID, flight.econPrice, flight.businessPrice, flight.premiumPrice, flight.destination,
                            new SimpleDateFormat("dd/MM/yyyy").format(flight.departDate), flight.econSeats, flight.businessSeats, flight.premiumSeats);
                }
                System.out.println("             ===============================================================================================================================================================\n");
            }
        } catch (IOException e) {
            System.out.println("Unable to open the file !\n");
        }
        return flights;
    }

    public Flight(String dataLine) throws ParseException {
        String[] parts = dataLine.split("\\|");

        flightID = parts[0];
        econPrice = Double.parseDouble(parts[1]);
        businessPrice = Double.parseDouble(parts[2]);
        premiumPrice = Double.parseDouble(parts[3]);
        destination = parts[4];

        departDate = new SimpleDateFormat("dd/MM/yyyy").parse(parts[5]);

        econSeats = Integer.parseInt(parts[6]);
        businessSeats = Integer.parseInt(parts[7]);
        premiumSeats = Integer.parseInt(parts[8]);
    }

    //Invoked to Main
    public static void reduceSeat(int selectedSeat, String selectedFlightId, String selectedFlightType) {
        ArrayList<Flight> updatedFlights = new ArrayList<>();
        //find the flight type and minus the seat available
        for (Flight flightDetails : flights) {
            if (flightDetails.getFlightID().equals(selectedFlightId)) {
                switch (selectedFlightType) {
                    case "Economy":
                        currentSeats = flightDetails.getEconSeats();
                        flightDetails.setEconSeats(currentSeats - selectedSeat);
                        break;
                    case "Business":
                        currentSeats = flightDetails.getBusinessSeats();
                        flightDetails.setBusinessSeats(currentSeats - selectedSeat);
                    case "Premium":
                        currentSeats = flightDetails.getPremiumSeats();
                        flightDetails.setPremiumSeats(currentSeats - selectedSeat);
                }
            }
            updatedFlights.add(flightDetails);
        }

        Set<String> processedIds = new HashSet<>(); // Set to keep track of processed IDs

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FLIGHT_FILE, false))) {
            for (Flight flight : updatedFlights) {
                String flightId = flight.getFlightID(); // Replace this with the actual method to get the flight ID
                if (!processedIds.contains(flightId)) {
                    writer.write(flight.toString());
                    writer.newLine();
                    processedIds.add(flightId); // Add the ID to the set to mark it as processed
                }
            }
            writer.close();
            // Clear the lists 
            updatedFlights.clear();
            flights.clear();

        } catch (IOException e) {
            System.out.println("Error to write the data into the file");
        }
    }
    
    //read flight details
     public static void readFlightData() {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(FLIGHT_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    flightData.add(details); // Add each flight data to the list
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + FLIGHT_FILE);
        }
        
        catch (IOException ex) {
            System.out.println("Unable to open the file!");
        }
    }
     
   //write flight details 
   //write new modify member data into the file 
    public static void writeNewFlightDataFile(ArrayList<String[]> flightData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FLIGHT_FILE, false))) {
            for (String[] flightRecord : flightData) {
                String flightFormatted = String.join("|", flightRecord);
                writer.write(flightFormatted);
                writer.newLine(); // Add a newline character after each line
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to write the data into file!\n");
        }
    }
}
