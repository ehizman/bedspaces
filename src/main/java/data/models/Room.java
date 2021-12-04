package data.models;

import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
public class Room {
    private String id;
    private BedSpace[] bedSpaces;

    public Room(String roomId){
        id = roomId;
        bedSpaces = new BedSpace[4];
        for (int i = 1; i <= bedSpaces.length; i++) {
            bedSpaces[i-1] = new BedSpace(id+" Bedspace "+i);
        }
    }
}
