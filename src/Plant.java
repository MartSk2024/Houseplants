import java.time.LocalDate;

public class Plant implements Comparable<Plant> {
    private String name;
    private String notes;
    private LocalDate planted;
    private LocalDate watering;
    private int frequencyOfWatering;

    public Plant(String name, String notes, int frequencyOfWatering, LocalDate watering, LocalDate planted) throws PlantException {
        this.name = name;
        this.notes = notes;
        this.setFrequencyOfWatering(frequencyOfWatering);
        this.watering = watering;
        this.setPlanted(planted);
    }

    public Plant(String name, int frequencyOfWatering) throws PlantException {
        this.name = name;
        this.notes = "";
        this.setFrequencyOfWatering(frequencyOfWatering);
        this.watering = LocalDate.now();
        this.planted = LocalDate.now();
    }

    public Plant(String name) throws PlantException {
        this.name = name;
        this.notes = "";
        this.frequencyOfWatering = 7;
        this.watering = LocalDate.now();
        this.planted = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getFrequencyOfWatering() {
        return frequencyOfWatering;
    }

    public void setFrequencyOfWatering(int frequencyOfWatering) throws PlantException {
        if (frequencyOfWatering <= 0) {
            throw new PlantException("Frekvence zálivky nemůže být nula nebo záporná! Zadal jsi: "+frequencyOfWatering);
        }
        this.frequencyOfWatering = frequencyOfWatering;
    }

    public LocalDate getWatering() {
        return watering;
    }

    public void setWatering(LocalDate watering) {
        this.watering = watering;
    }

    public LocalDate getPlanted() {
        return planted;
    }

    public void setPlanted(LocalDate planted) throws PlantException {
        if (getWatering().isBefore(planted)) {
            throw new PlantException("Datum poslední zálivky nemůže předcházet datu zasazení rostliny.");
        }
        this.planted = planted;
    }

    public String getWateringInfo() {
        return  getName()+", "+getWatering()+", "+getWatering().plusDays(getFrequencyOfWatering());
    }

    public void doWateringNow() {
        this.watering = LocalDate.now();
    }

    @Override
    public int compareTo(Plant otherPlant) {
        return name.compareTo(otherPlant.name);
    }
}//konec třídy
