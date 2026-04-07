package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.dataclasses.CommandType;
import common.dataclasses.MusicBand;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private CommandType commandType;
    private String argument;
    private MusicBand musicBand;

    public Request() {
    }

    public Request(CommandType commandType, String argument, MusicBand musicBand) {
        this.commandType = commandType;
        this.argument = argument;
        this.musicBand = musicBand;
    }



    public String toJson() throws JsonProcessingException {
        return JsonDataMapper.getInstance().getMapper().writeValueAsString(this);
    }


    public static Request fromJson(String json) throws JsonProcessingException {
        return JsonDataMapper.getInstance().getMapper().readValue(json, Request.class);

    }

    public CommandType getCommandType() {
        return commandType;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }

    public String getArgument() {
        return argument;
    }
}