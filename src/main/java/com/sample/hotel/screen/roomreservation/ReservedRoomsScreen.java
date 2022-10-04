package com.sample.hotel.screen.roomreservation;

import com.sample.hotel.entity.Client;
import com.sample.hotel.entity.RoomReservation;
import io.jmix.core.DataManager;
import io.jmix.ui.Dialogs;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("ReservedRooms")
@UiDescriptor("reserved-rooms.xml")
@LookupComponent("roomReservationsTable")
public class ReservedRoomsScreen extends StandardLookup<RoomReservation> {

    @Autowired
    private DataManager dataManager;
    @Autowired
    private GroupTable<RoomReservation> roomReservationsTable;
    @Autowired
    private Dialogs dialogs;

    @Subscribe("roomReservationsTable.viewClientEmail")
    public void onRoomReservationsTableViewClientEmail(Action.ActionPerformedEvent event) {
        RoomReservation reservation = roomReservationsTable.getSingleSelected();
        if (reservation == null) {
            return;
        }

        //reservation.getBooking().get

        //Client client = reservation.getBooking().getClient();
        Client fullClient = dataManager.load(Client.class).id(reservation.getBooking().getClient().getId()).one();

        dialogs.createMessageDialog()
                .withCaption("Client email")
                .withMessage(fullClient.getEmail())
                .show();
    }
}