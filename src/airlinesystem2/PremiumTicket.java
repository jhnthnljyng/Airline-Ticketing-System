/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airlinesystem2;

/**
 *
 * @author user
 */
public abstract class PremiumTicket extends Ticket{
    @Override 
    public void processTicket(String[] ticketData) {
        if (ticketData[4].equalsIgnoreCase("Premium")) {
            super.processTicket(ticketData);
        }
    }
}
