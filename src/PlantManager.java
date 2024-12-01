import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.util.logging.Level.parse;

public class PlantManager {
    private final List<Plant> listOfPlants = new ArrayList<>();

    public List<Plant> getListOfPlants() {
        return listOfPlants;
    }

    public void addPlant(Plant plant) {
        listOfPlants.add(plant);
    }

    public void addPlants(List<Plant> listOfPlants) {
        listOfPlants.addAll(listOfPlants);
    }

    public Plant getPlant(int index) {
        return listOfPlants.get(index);
    }

    public void removePlant(Plant plant) {
        listOfPlants.remove(plant);
    }

    public List<Plant> copyOfPlants = new ArrayList<Plant>(listOfPlants);

    public void clearListOfPlants() {
        listOfPlants.clear();
    }

    public void getWateringDate() {
        System.out.println("\nInformace o zálivce (název, datum poslední zálivky, znovu zalít dne):");
        for (Plant plantItem : getListOfPlants()) {
            System.out.println(plantItem.getWateringInfo());
        }
    }

    public void getPlantsOverWatering() {
        System.out.println("\nSeznam rostlin k okamžitému zalití:");
        for (Plant plantItem : getListOfPlants()) {
            if (plantItem.getWatering().plusDays(plantItem.getFrequencyOfWatering()).isBefore(LocalDate.now())) {
                System.out.println(plantItem.getName());
            }
        }
    }

    public void readFromFile(String filename,String delimiter) throws PlantException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNumber = lineNumber + 1;
                Plant plant = parse(line, lineNumber, delimiter);
                listOfPlants.add(plant);
                //System.out.println();
            }
        } catch (FileNotFoundException e) {
            throw new PlantException("Soubor "+filename+ " nebyl nalezen!");
        }
    }

    public Plant parse(String line, int lineNumber, String delimiter) throws PlantException {
        int numberOfItemsRequired = 5;
        String[] parts = line.split(delimiter);
        if (parts.length != numberOfItemsRequired) {
            listOfPlants.clear();
            throw new PlantException("Chybný počet položek na řádku číslo " +
                    lineNumber + ". Na řádku se očekává " + numberOfItemsRequired +
                    " položek.");
        }

        String name = parts[0].trim();
        String notes = parts[1].trim();

        int frequencyOfWatering;
        try {
            frequencyOfWatering = Integer.parseInt(parts[2].trim());
            if (frequencyOfWatering <= 0) {
                listOfPlants.clear();
                throw new PlantException("záporná nebo nulová frekvence zálivky");
            }
        } catch (Exception e) {
            listOfPlants.clear();
            throw new PlantException("Chybný formát položky '" + parts[2].trim() +
                    "' na řádku " + lineNumber + " ("+e.getMessage()+").");
        }

        LocalDate watering;
        try {
            watering = LocalDate.parse(parts[3].trim());
        } catch (DateTimeParseException exd) {
            listOfPlants.clear();
            throw new PlantException("Chybný formát položky '" + parts[3].trim() +
                    "' na řádku " + lineNumber + " ("+exd.getMessage()+")." );
        }

        LocalDate planted;
        try {
            planted = LocalDate.parse(parts[4].trim());
        } catch (DateTimeParseException exd) {
            listOfPlants.clear();
            throw new PlantException("Chybný formát položky '" + parts[4].trim() +
                    "' na řádku " + lineNumber + " ("+exd.getMessage()+")." );
        }

        if (watering.isBefore(planted)) {
            listOfPlants.clear();
            throw new PlantException("Chyba na řádku " + lineNumber + ": Datum poslední" +
                    " zálivky nemůže předcházet datu zasazení rostliny.");
        }

        return new Plant(name, notes, frequencyOfWatering, watering, planted);
    }

    public void writeToFile(String filename,String delimiter) throws PlantException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {
            for (Plant plantItem : getListOfPlants()){
                writer.println(plantItem.getName() + delimiter + " " +
                        plantItem.getNotes() + delimiter + " " +
                        plantItem.getFrequencyOfWatering() + delimiter + " " +
                        plantItem.getWatering() + delimiter + " " +
                        plantItem.getPlanted());
            }
        } catch (IOException e) {
            throw new PlantException("Soubor "+filename+ " nebyl nalezen!");
        }
    }

    public void sortByWatering(String delimiter) {
        listOfPlants.sort(Comparator.comparing(Plant::getWatering));
    }

    public void getListOfPlantsOnScreen(String delimiter) {
        for (Plant plantItem : getListOfPlants()){
            System.out.println(plantItem.getName() + delimiter + " " +
                    plantItem.getNotes() + delimiter + " " +
                    plantItem.getFrequencyOfWatering() + delimiter + " " +
                    plantItem.getWatering() + delimiter + " " +
                    plantItem.getPlanted());
        }
    }
}//konec třídy
