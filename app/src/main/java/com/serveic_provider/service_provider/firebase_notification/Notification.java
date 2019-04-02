package com.serveic_provider.service_provider.firebase_notification;

public class Notification {
    String message;
    String from;
    String serviceId;

    public Notification(){

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }

    public String getServiceId() {
        return serviceId;
    }

}
