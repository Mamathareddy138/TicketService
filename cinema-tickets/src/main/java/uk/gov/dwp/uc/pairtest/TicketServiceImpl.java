package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
    	
    	int trAdult = 0;
    	int trChild = 0;
    	int trInfant = 0;
    	for (TicketTypeRequest tr : ticketTypeRequests) {
    		if(tr.getTicketType() == Type.ADULT) {
    			trAdult = tr.getNoOfTickets();
    		}
    		if(tr.getTicketType() == Type.CHILD) {
    			trChild = tr.getNoOfTickets();
    		}
    		if(tr.getTicketType() == Type.INFANT) {
    			trInfant = tr.getNoOfTickets();
    		}
    	}
    	if((trChild > 0 || trInfant >0)&& trAdult <=0) {
    		throw new InvalidPurchaseException();
    	}
    	int totalTikets = trAdult+trChild;
    	int totalCost = trAdult*20 + trChild*10;
    	if( totalTikets+trInfant > 20) {
    		throw new InvalidPurchaseException();
    	}
    	SeatReservationService srs = new SeatReservationServiceImpl();
    	TicketPaymentService tps = new TicketPaymentServiceImpl();
    	srs.reserveSeat(accountId, totalTikets);
    	tps.makePayment(accountId, totalCost);
    }

}
