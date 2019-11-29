package node;

import java.io.Serializable;

public class Node implements Serializable {

    private int id;
    private String name;
    //private String ip;

    Node(String name) {
        this.name = name;
    }

    Node(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
