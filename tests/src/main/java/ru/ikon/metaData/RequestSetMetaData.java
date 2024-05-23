package ru.ikon.metaData;

import java.util.ArrayList;
import java.util.List;

public class RequestSetMetaData {
    private final List<RequestMetaData> list = new ArrayList<>();

    public RequestSetMetaData() {
    }

    public List<RequestMetaData> getList() {
        return list;
    }
}
