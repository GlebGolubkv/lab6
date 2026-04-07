package common;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.Serializable;

public class Response implements Serializable {

    private static long serialVersionUID = 1L;
    private boolean success;
    private String message;
    private StringBuilder data;

    public Response() {}



    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }

    public Response(boolean success, String message, StringBuilder data) {
        this.success = success;
        this.message = message;
        this.data = data;
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

    @Override
    public String toString() {
        return "Response{\n" + "success=" + success + "\n, message=" + message + "\n, data=" + data + '}';
    }
}
