package com.sample.hotel.app;

import com.sample.hotel.entity.Booking;
import com.sample.hotel.entity.BookingStatus;
import com.sample.hotel.entity.RoomReservation;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.core.event.EntitySavingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookingEventListener {

    @Autowired
    private DataManager dataManager;

    @EventListener
    void onOrderSaving(EntitySavingEvent<Booking> event) {
        if (event.isNewEntity() || event.getEntity() != null) {
            Booking booking = event.getEntity();
            booking.setDepartureDate(booking.getArrivalDate().plusDays(booking.getNightsOfStay()));
        }
    }


    @EventListener
    @Transactional
    public void onBookingChangedBeforeCommit(EntityChangedEvent<Booking> event) {
        if (event.getType() == EntityChangedEvent.Type.UPDATED) {
            Booking booking = dataManager.load(event.getEntityId()).one();
            if (event.getChanges().isChanged("status") && booking.getStatus() == BookingStatus.CANCELLED) {
                // Id<Object> id = event.getChanges().getOldReferenceId("roomReservation");
                RoomReservation roomReservation = booking.getRoomReservation();
                //RoomReservation roomReservation = (RoomReservation) dataManager.load(id).optional().orElse(null);
                if (roomReservation != null) {
                    //System.out.println("!!!");
                    dataManager.remove(roomReservation);
                }
            }
        }

    }

}