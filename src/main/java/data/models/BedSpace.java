package data.models;

import lombok.Data;

@Data
public class BedSpace implements Comparable<BedSpace>{
    private String id;
    private boolean isEmpty;

    public BedSpace(String id) {
        this.id = id;
        isEmpty = true;
    }

    @Override
    public int compareTo(BedSpace o) {
        return this.id.compareTo(o.id);
    }
}
