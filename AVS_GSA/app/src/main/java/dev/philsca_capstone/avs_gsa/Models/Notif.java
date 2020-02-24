package dev.philsca_capstone.avs_gsa.Models;

public class Notif {
    private String flight;
    private String uid;
    private String reservationId;
    private String message;
    private String status;
    private String AWB;


    private boolean opened;

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getAWB() {
        return AWB;
    }

    public void setAWB(String AWB) {
        this.AWB = AWB;
    }

}
