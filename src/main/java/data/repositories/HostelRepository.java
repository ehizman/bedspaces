package data.repositories;

import data.models.BedSpace;
import data.models.Gender;
import data.models.Hostel;
import data.models.HostelName;
import exceptions.HostelManagementException;
import exceptions.NoAvailableBedspaceException;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

public class HostelRepository {
    private static final Hostel[] hostels = new Hostel[4];
    private final Queue<BedSpace> availableBedSpacesForMales = new PriorityQueue<>(160);
    private final Queue<BedSpace> availableBedSpacesForFemales = new PriorityQueue<>(160);

    static {
        hostels[0] = new Hostel("HALL1", Gender.FEMALE);
        hostels[1] = new Hostel("HALL2", Gender.FEMALE);
        hostels[2] = new Hostel("HALL3", Gender.MALE);
        hostels[3] = new Hostel("HALL4", Gender.MALE);
    }

    public HostelRepository() {
       findAllAvailableBedSpaces();
    }

    private void findAllAvailableBedSpaces(){
        for (Hostel hostel: hostels) {
            if (hostel.getHostelCategory()==Gender.FEMALE){
                availableBedSpacesForFemales.addAll(hostel.findAllAvailableBedSpaces());
            }
            else{
                availableBedSpacesForMales.addAll(hostel.findAllAvailableBedSpaces());
            }
        }
    }

    public static Hostel[] getHostels() {
        return hostels;
    }

    public Queue<BedSpace> getAvailableBedSpacesForMales() {
        return availableBedSpacesForMales;
    }

    public Queue<BedSpace> getAvailableBedSpacesForFeMales() {
        return availableBedSpacesForFemales;
    }

    public int getTotalNumberOfAvailableBedSpaces() {
        return availableBedSpacesForFemales.size()+availableBedSpacesForMales.size();
    }

    public BedSpace returnAvailableFemaleSpace() throws NoAvailableBedspaceException {
        try {
            return availableBedSpacesForFemales.poll();

        }
        catch(NoSuchElementException exception){
            throw new NoAvailableBedspaceException(exception.getMessage());
        }
    }

    public BedSpace returnAvailableMaleSpace() throws NoAvailableBedspaceException {
        try {
            return availableBedSpacesForMales.poll();

        }
        catch(NoSuchElementException exception){
            throw new NoAvailableBedspaceException(exception.getMessage());
        }
    }

    public Hostel[] getAllHostels(){
        return hostels;
    }

    public Hostel findHostelByName(String hostelName) throws HostelManagementException {
        for (Hostel hostel: hostels) {
            if (hostel.getName() == HostelName.valueOf(hostelName)){
                return hostel;
            }
        }
        throw new HostelManagementException("specified hostel name does not exist");
    }
}
