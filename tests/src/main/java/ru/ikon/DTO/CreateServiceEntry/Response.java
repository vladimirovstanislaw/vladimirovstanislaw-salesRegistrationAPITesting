package ru.ikon.DTO.CreateServiceEntry;

public class Response {
    private ServiceEntry serviceEntry;

    public Response() {
    }

    public Response(ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    public ServiceEntry getServiceEntry() {
        return serviceEntry;
    }

    public void setServiceEntry(ServiceEntry serviceEntry) {
        this.serviceEntry = serviceEntry;
    }

    @Override
    public String toString() {
        return "Response{" +
                "serviceEntry=" + serviceEntry +
                '}';
    }
}
