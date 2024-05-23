package ru.ikon.metaData;

public class RequestMetaData {
    private long startRequestLaunch;
    private long endRequestLaunch;

    private long duration;

    private String requestType;

    public RequestMetaData() {
    }

    public RequestMetaData(long startRequestLaunch, long endRequestLaunch, long duration, String requestType) {
        this.startRequestLaunch = startRequestLaunch;
        this.endRequestLaunch = endRequestLaunch;
        this.duration = duration;
        this.requestType = requestType;
    }

    public long getStartRequestLaunch() {
        return startRequestLaunch;
    }

    public void setStartRequestLaunch(long startRequestLaunch) {
        this.startRequestLaunch = startRequestLaunch;
    }

    public long getEndRequestLaunch() {
        return endRequestLaunch;
    }

    public void setEndRequestLaunch(long endRequestLaunch) {
        this.endRequestLaunch = endRequestLaunch;
        if (this.startRequestLaunch == 0) {
            throw new RuntimeException("Wasn't set startRequestLaunch.");
        }
        this.duration = this.endRequestLaunch - this.startRequestLaunch;
    }

    public long getDuration() {
        return duration;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "RequestMetaData{" +
                "startRequestLaunch=" + startRequestLaunch +
                ", endRequestLaunch=" + endRequestLaunch +
                ", duration=" + duration +
                ", requestType='" + requestType + '\'' +
                '}';
    }
}
