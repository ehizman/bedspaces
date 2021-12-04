package data.models;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hostel {
    private HostelName name;
    private Room[] rooms;
    private Gender hostelCategory;

    public Hostel(String hostelName, Gender hostelCategory){
        name = HostelName.valueOf(hostelName);
        rooms = new Room[20];
        for (int i = 1; i <= rooms.length; i++) {
            rooms[i-1] = new Room(hostelName +" Room "+i);
        }
        this.hostelCategory = hostelCategory;
    }


    public List<BedSpace> findAllAvailableBedSpaces() {
        List<BedSpace> bedSpaces = new ArrayList<>();
        for (Room room: rooms) {
            bedSpaces.addAll(Arrays.asList(room.getBedSpaces()));
        }
        return bedSpaces;
    }

    public Gender getHostelCategory() {
        return hostelCategory;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public HostelName getName() {
        return name;
    }
}
