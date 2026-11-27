package rushhour.loader;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import rushhour.model.GameState;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.IllformedLocaleException;

public class TextBoardLoaderTest {
    private final TextBoardLoader loader = new TextBoardLoader();

    @Test
    void shouldLoadBoardFromTextFile() throws IOException {
        Path path = Paths.get("/Volumes/Work/JAVA/RushHour/src/main/resources/boards/default-board.txt");
         GameState gameState = loader.load(path);

         assertNotNull(gameState);
         assertNotNull(gameState.getBoard());
         assertEquals(6,gameState.getBoard().getRows());
         assertEquals(6,gameState.getBoard().getColumns());
         assertEquals(4, gameState.getBoard().getVehicles().size());
    }

    @Test
    void shouldRejectNullPath(){
        assertThrows(IllegalArgumentException.class,() -> loader.load(null));
    }

    @Test
    void shouldThrowIOExceptionWhenFileDoesNotExist(){
        Path path = Path.of("/Volumes/Work/JAVA/RushHour/src/main/resources/boards/non-existant.txt");

        assertThrows(IOException.class,() -> loader.load(path));
    }

    @Test
    void shouldRejectInvalidConfigurationLine() throws IOException {
        Path path = Files.createTempFile("invalid-configuration", ".txt");

        try{
            Files.writeString(path,"invalidLineWithoutEquals");

            assertThrows(IllegalArgumentException.class,() -> loader.load(path));
        }
        finally{
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectUnknownConfigurationKey() throws IOException {
        Path path = Files.createTempFile("unknown-key", ".txt");

        try{
            Files.writeString(path,"unknownKey=value");
            assertThrows(IllegalArgumentException.class,() -> loader.load(path));
        }
        finally{
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectInvalidBoardDimensions() throws IOException {
        Path path = Files.createTempFile("invalid-dimensions", ".txt");

        try {
            Files.writeString(path, """
                rows=0
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectInvalidVehicleConfiguration() throws IOException {
        Path path = Files.createTempFile("invalid-vehicle", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectInvalidVehicleOrientation() throws IOException {
        Path path = Files.createTempFile("invalid-orientation", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,DIAGONAL,2,2,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectInvalidVehicleLength() throws IOException {
        Path path = Files.createTempFile("invalid-length", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,4,2,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectVehicleOutsideBoard() throws IOException {
        Path path = Files.createTempFile("vehicle-outside-board", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,5
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectOverlappingVehicles() throws IOException {
        Path path = Files.createTempFile("overlapping-vehicles", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,2,2
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectMissingHumanHero() throws IOException {
        Path path = Files.createTempFile("missing-human-hero", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectMissingMachineHero() throws IOException {
        Path path = Files.createTempFile("missing-machine-hero", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectMissingRequiredConfiguration() throws IOException {
        Path path = Files.createTempFile("missing-configuration", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectInvalidExitSide() throws IOException {
        Path path = Files.createTempFile("invalid-exit-side", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=DIAGONAL,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectNegativeExitCoordinate() throws IOException {
        Path path = Files.createTempFile("negative-exit-coordinate", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,-1
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectExitCoordinateOutsideBoard() throws IOException {
        Path path = Files.createTempFile("exit-coordinate-outside-board", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,6
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectDuplicateVehicleIds() throws IOException {
        Path path = Files.createTempFile("duplicate-vehicle-ids", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=H,VERTICAL,2,0,0
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectInvalidExitConfiguration() throws IOException {
        Path path = Files.createTempFile("invalid-exit-configuration", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectVehicleWithEmptyId() throws IOException {
        Path path = Files.createTempFile("empty-vehicle-id", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectVehicleWithEmptyOrientation() throws IOException {
        Path path = Files.createTempFile("empty-vehicle-orientation", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectInvalidVehiclePositionFormat() throws IOException {
        Path path = Files.createTempFile("invalid-vehicle-position", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectMissingBoardDimensions() throws IOException {
        Path path = Files.createTempFile("missing-board-dimensions", ".txt");

        try {
            Files.writeString(path, """
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectMissingHumanExit() throws IOException {
        Path path = Files.createTempFile("missing-human-exit", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                machineExit=RIGHT,4
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }

    @Test
    void shouldRejectMissingMachineExit() throws IOException {
        Path path = Files.createTempFile("missing-machine-exit", ".txt");

        try {
            Files.writeString(path, """
                rows=6
                columns=6
                humanHeroId=H
                machineHeroId=M
                humanExit=RIGHT,2
                vehicle=H,HORIZONTAL,2,2,1
                vehicle=M,HORIZONTAL,2,4,1
                """);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> loader.load(path)
            );
        } finally {
            Files.deleteIfExists(path);
        }
    }
}
