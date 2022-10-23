package com.example.inventoryapplication;

/**
 * a part that was made in house
 */
public class InHouse extends Part {
    private int machineId;

    public InHouse(int machineId, int id, String name, double price, int stock, int min, int max) {
        super(id, name, price, stock, min, max);
        this. machineId = machineId;
    }

    /**
     * @return the id of the machine that created the part
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId sets the id of the machine that made the part
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
