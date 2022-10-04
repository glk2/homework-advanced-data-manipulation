package com.sample.hotel.app;

import com.sample.hotel.entity.Booking;
import com.sample.hotel.entity.Room;
import com.sample.hotel.entity.RoomReservation;
import io.jmix.core.DataManager;
import io.jmix.core.entity.KeyValueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookingService {

    @Autowired
    private DataManager dataManager;

    /**
     * Check if given room is suitable for the booking.
     * 1) Check that sleeping places is enough to fit numberOfGuests.
     * 2) Check that there are no reservations for this room at the same range of dates.
     * Use javax.persistence.EntityManager and JPQL query for querying database.
     *
     * @param booking booking
     * @param room    room
     * @return true if checks are passed successfully
     */
    public boolean isSuitable(Booking booking, Room room) {
        //todo implement me!
        if (room.getSleepingPlaces() >= booking.getNumberOfGuests()) {

            Map<String, Object> params = new HashMap<>();
            params.put("arDt", booking.getArrivalDate());
            params.put("deDt", booking.getDepartureDate());
            params.put("room", room);

            Long userCount = dataManager.loadValue("select count(r) from RoomReservation r where r.room=:room and not (r.booking.arrivalDate > :deDt or r.booking.departureDate <= :arDt)", Long.class).setParameters(params).one();
            if (userCount == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check that room is suitable for the booking, and create a reservation for this room.
     *
     * @param room    room to reserve
     * @param booking hotel booking
     *                Wrap operation into a transaction (declarative or manual).
     * @return created reservation object, or null if room is not suitable
     */
    @Transactional
    public RoomReservation reserveRoom(Booking booking, Room room) {
        //todo implement me!
        if (isSuitable(booking, room)) {
            RoomReservation roomReservation = dataManager.create(RoomReservation.class);
            roomReservation.setRoom(room);
            roomReservation.setBooking(booking);
            dataManager.save(roomReservation);

            return roomReservation;
        }
        return null;
    }
}