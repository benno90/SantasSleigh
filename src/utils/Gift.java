package utils;

/**
 *
 * @author benno
 */
public class Gift extends Location implements Comparable<Gift> {

    private final int id;
    private final double weight;
    
    private Object[] neighbours;

    public Gift(int id, double longitude, double latitude, double weight) {
        super(longitude, latitude);

        this.id = id;
        this.weight = weight;
        neighbours = null;
    }

    public double getWeigth() {
        return weight;
    }

    public int getID() {
        return id;
    }

    @Override
    public int compareTo(Gift other) {
        return Integer.compare(this.id, other.id);
    }
    
    public void setNeighbours(Object[] neighbours) {
        this.neighbours = neighbours;
    }
    
    public Object[] getNeighbours() {
        return neighbours;
    }

}
