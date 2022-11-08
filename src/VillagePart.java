

public class VillagePart {
    private final String villagePartCode;
    private final String name;
    private final String villageCode;

    public VillagePart(String villagePartCode, String name, String villageCode) {
        this.villagePartCode = villagePartCode;
        this.name = name;
        this.villageCode = villageCode;
    }

    public String getVillagePartCode() {
        return villagePartCode;
    }

    public String getName() {
        return name;
    }

    public String getVillageCode() {
        return villageCode;
    }
}
