package net.codebot.shapes;

import java.util.ArrayList;

public class Store {
    private ArrayList<Floor> floors;
    private int currentFloor = 0;

    public Store(ArrayList<Floor> floors){
        this.floors = floors;
    }

    void updateFloor(Floor floor, int location){
        floors.set(location,floor);
    }

    void switchFloor(int floor){
        this.currentFloor = floor;
    }

    Floor getCurrentFloor(){
        return this.floors.get(this.currentFloor);
    }

    void addFloor(Floor floor){
        this.floors.add(floor);
    }

    public ArrayList<Floor> getFloors(){
        return this.floors;
    }

    Floor getFloor(int floorNum){
        return this.floors.get(floorNum);
    }


}
