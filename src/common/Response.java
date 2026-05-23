package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.dataclasses.MusicBandEntry;

import java.util.List;

public class Response {

    private boolean success;
    private String message;
    private StringBuilder data;
    private boolean internalOnly;

    public int getServerData() {
        return serverData;
    }

    private int serverData;

    /** Lab 8: structured collection payload for GUI clients. */
    private List<MusicBandEntry> entries;

    public Response() {
    }


    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;

    }


    public Response(boolean success, String message, boolean internalOnly) {
        this.success = success;
        this.message = message;
        this.internalOnly = internalOnly;
    }

    public Response(boolean success, String message, boolean internalOnly, int serverData) {
        this.success = success;
        this.message = message;
        this.internalOnly = internalOnly;
        this.serverData = serverData;
    }

    public Response(boolean success, String message, StringBuilder data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Response(boolean success, String message, List<MusicBandEntry> entries) {
        this.success = success;
        this.message = message;
        this.entries = entries;
    }

    public String toJson() throws JsonProcessingException {
        return JsonDataMapper.getInstance().getMapper().writeValueAsString(this);
    }

    public static Response fromJson(String json) throws JsonProcessingException {
        return JsonDataMapper.getInstance().getMapper().readValue(json, Response.class);
    }


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public StringBuilder getData() {
        return data;
    }

    public boolean isInternalOnly() {return internalOnly;}

    public List<MusicBandEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<MusicBandEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "Response{\n" + "success=" + success + "\n, message=" + message + "\n, data=" + data + '}';
    }
}
