package data.repositories;

import data.models.BedSpace;
import data.models.Gender;
import data.models.Hostel;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

class HostelRepositoryTest {
    private HostelRepository hostelRepository;

    @BeforeEach
    void setUp() {
        hostelRepository = new HostelRepository();
    }

    @Test
    void getHostels() {
        Hostel[] expectedArray = new Hostel[4];
        expectedArray[0]= new Hostel("HALL1", Gender.FEMALE);
        expectedArray[1]= new Hostel("HALL2", Gender.FEMALE);
        expectedArray[2]= new Hostel("HALL3", Gender.MALE);
        expectedArray[3]= new Hostel("HALL4", Gender.MALE);
        assertThat(HostelRepository.getHostels()[0], samePropertyValuesAs(expectedArray[0]));
        assertThat(HostelRepository.getHostels()[1], samePropertyValuesAs(expectedArray[1]));
        assertThat(HostelRepository.getHostels()[2], samePropertyValuesAs(expectedArray[2]));
        assertThat(HostelRepository.getHostels()[3], samePropertyValuesAs(expectedArray[3]));
    }

    @Test
    void getAvailableBedSpaces() {
        assertThat(hostelRepository.getTotalNumberOfAvailableBedSpaces(), is(320));
    }

    @Test
    void returnAvailableMaleSpace(){
        assertThat(hostelRepository.returnAvailableMaleSpace(), notNullValue());
        assertThat(hostelRepository.getAvailableBedSpacesForMales().size(), is(159));
        assertThat(hostelRepository.getTotalNumberOfAvailableBedSpaces(), is(319));
    }

    @Test
    void returnAvailableFemaleSpace(){
        assertThat(hostelRepository.returnAvailableFemaleSpace(), notNullValue());
        assertThat(hostelRepository.getAvailableBedSpacesForFeMales().size(), is(159));
        assertThat(hostelRepository.getTotalNumberOfAvailableBedSpaces(), is(319));
    }
}