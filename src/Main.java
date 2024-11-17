import java.time.LocalDate;
import java.util.Collections;
import java.util.Collection;

public class Main {
    private static final String FILENAME1 = "resources/kvetiny.txt";
    private static final String FILENAME2 = "resources/kvetinyVypis.txt";
    private static final String DELIMITER = "\t";

    public static void main(String[] args) throws PlantException {
        PlantManager plantManager = new PlantManager();
        readFile(plantManager);
        statisticsOfWatering(plantManager);
        fillPlant(plantManager);
        writeFile(plantManager);
        sortingList(plantManager);
    }

    private static void readFile(PlantManager plantManager) {
        try {
            plantManager.readFromFile(FILENAME1, DELIMITER);
            System.out.println("\nNačtení zvoleného souboru: vše v pořádku.");
        } catch (PlantException ex) {
            System.err.println("\nChyba při načtení souboru: "+ex.getMessage());
        }
    }

    private static void statisticsOfWatering(PlantManager plantManager) {
        plantManager.getWateringDate();
        plantManager.getPlantsOverWatering();
    }

    private static void fillPlant(PlantManager plantManager) {
        try {
            Plant plantOrchidej = new Plant("Orchidej", "Hawaii",
                    1, LocalDate.of(2024, 11, 17),
                    LocalDate.of(2024, 11, 17));
            plantManager.addPlant(plantOrchidej);
            for (int i = 1; i <= 10; i++) {
                Plant plant = new Plant("Tulipán na prodej "+i, 14);
                plantManager.addPlant(plant);
            }
            System.out.println("\nDodatečně zadané květiny jsou v pořádku a květina "
                    + plantManager.getPlant(3).getName() + " v seznamu je odstraněna.");
            plantManager.removePlant(plantManager.getPlant(3));
        } catch (PlantException ex) {
            System.err.println("\nDodatečně zadané květiny: Chyba ve frekvenci nebo v datu zálivky květiny: " + ex.getMessage());
        }
    }

    private static void writeFile(PlantManager plantManager) {
        System.out.println("\nVýpis do zvoleného souboru: viz soubor "+FILENAME2);
        try {
            System.out.println("\nNeseřazený seznam květin:");
            plantManager.getListOfPlantsOnScreen(DELIMITER);
            plantManager.writeToFile(FILENAME2, DELIMITER);
        } catch (PlantException ex) {
            System.err.println("Chyba při zápisu do souboru: "+ex.getMessage());
        }
    }

    private static void sortingList(PlantManager plantManager) {
        //Vyberte seřazení seznamu květin buď podle názvu, nebo dle datu zálivky:
        Collections.sort(plantManager.getListOfPlants());
        System.out.println("\nSeřazený seznam květin podle názvu:");
        //plantManager.sortByWatering(DELIMITER);
        //System.out.println("\nSeřazený seznam květin podle datu zálivky:");
        plantManager.getListOfPlantsOnScreen(DELIMITER);
        System.out.println("\nKonec programu");
    }
}//konec třídy