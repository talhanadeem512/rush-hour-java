package rushhour.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import rushhour.model.*;

import java.util.Locale;

/**
 * Loads Rush Hour board configuration from a text file.
 */
public class TextBoardLoader {
    /**
     * Loads game state from the specified text file.
     *
     * @param path the path to the text configuration file.
     * @return the game state represented by the file.
     * @throws IllegalArgumentException if the path is null or the file contains invalid configuration.
     * @throws IOException if the file does not exist, is not a regular file or cannot be read.
     *
     */
    public GameState load(Path path) throws IOException {
        if(path == null){
            throw new IllegalArgumentException("Path cannot be null.");
        }

        if(!Files.exists(path) || !Files.isRegularFile(path)){
            throw new IOException("File does not exist or it is not a regular file.");
        }

        List<String> lines = Files.readAllLines(path);

        int rows = 0;
        int columns = 0;
        String humanHeroId = null;
        String machineHeroId = null;
        int humanExitCoordinate = -1;
        int machineExitCoordinate = -1;
        ExitSide humanExitSide = null;
        ExitSide machineExitSide = null;
        List<Vehicle> vehicles = new ArrayList<>();

        for(String line : lines){
            line = line.trim();
            if(line.isBlank()){
                continue;
            }
            String[] parts = line.split("=", 2);

            if(parts.length != 2){
                throw new IllegalArgumentException("Invalid configuration line: " + line);
            }

            String key = parts[0].trim();
            String value = parts[1].trim();
            if (value.isEmpty()) {
                throw new IllegalArgumentException(
                        "Configuration value cannot be empty: " + key
                );
            }

            switch(key){
                case "rows" -> rows = Integer.parseInt(value);
                case "columns" -> columns = Integer.parseInt(value);
                case "humanHeroId" -> humanHeroId = value;
                case "machineHeroId" -> machineHeroId = value;
                case "humanExit" -> {
                    String[] exitParts = value.split(",");
                    if (exitParts.length != 2) {
                        throw new IllegalArgumentException("Invalid human exit configuration: " + value);
                    }
                    humanExitSide = ExitSide.valueOf(exitParts[0].trim().toUpperCase(Locale.ROOT));
                    humanExitCoordinate = Integer.parseInt(exitParts[1].trim());
                }
                case "machineExit" -> {
                    String[] exitParts = value.split(",");
                    if (exitParts.length != 2) {
                        throw new IllegalArgumentException("Invalid machine exit configuration: " + value);
                    }
                    machineExitSide = ExitSide.valueOf(exitParts[0].trim().toUpperCase(Locale.ROOT));
                    machineExitCoordinate = Integer.parseInt(exitParts[1].trim());
                }
                case "vehicle" -> {
                    String[] vehicleParts = value.split(",");
                    if (vehicleParts.length != 5) {
                        throw new IllegalArgumentException("Invalid vehicle configuration: " + value);
                    }
                    String id = vehicleParts[0].trim();
                    if (id.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Vehicle ID cannot be empty."
                        );
                    }
                    Orientation orientation = Orientation.valueOf(vehicleParts[1].trim().toUpperCase(Locale.ROOT));
                    int length = Integer.parseInt(vehicleParts[2].trim());
                    int row =  Integer.parseInt(vehicleParts[3].trim());
                    int column =  Integer.parseInt(vehicleParts[4].trim());
                    vehicles.add(new Vehicle(
                            id,
                            length,
                            orientation,
                            new Position(row, column)
                    ));
                }

                default -> throw new IllegalArgumentException("Unknown configuration line: " + key);
            }
        }

        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Board dimensions must be positive.");
        }

        if (humanHeroId == null || machineHeroId == null || machineExitSide == null || humanExitSide == null) {
            throw new IllegalArgumentException("Missing required board configuration.");
        }

        if (humanExitCoordinate < 0 || machineExitCoordinate < 0) {
            throw new IllegalArgumentException("Exit coordinates cannot be negative.");
        }

        Board board = new Board(rows, columns);
        for(Vehicle vehicle : vehicles){
             board.addVehicle(vehicle);
        }

        return new GameState(
                board,
                humanHeroId,
                machineHeroId,
                humanExitCoordinate,
                machineExitCoordinate,
                humanExitSide,
                machineExitSide
        );
    }
}
