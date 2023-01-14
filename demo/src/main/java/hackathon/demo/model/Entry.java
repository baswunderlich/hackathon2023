package hackathon.demo.model;

public class Entry {
    public long time;
    public int value;
    public int chargingCycles;

    public Entry(long time, int value, int chargingCycles) {
        this.time = time;
        this.value = value;
        this.chargingCycles = chargingCycles;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getChargingCycles() {
        return chargingCycles;
    }

    public void setChargingCycles(int chargingCycles) {
        this.chargingCycles = chargingCycles;
    }
}
