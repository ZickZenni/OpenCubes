import net.opencubes.client.OpenCubes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class Main {
    static final Logger LOGGER = LogManager.getLogger("Main");

    public static void main(String[] args) {
        OpenCubes openCubes;
        try {
            openCubes = new OpenCubes();
        } catch (Throwable throwable) {
            LOGGER.error("Unhandled game exception: ", throwable);
            JOptionPane.showMessageDialog(null, "An fatal error occurred while starting the game: \n" + throwable.getMessage(), "Fatal Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            openCubes.run();
        } catch (Throwable throwable) {
            LOGGER.error("Unhandled game exception: ", throwable);
            openCubes.shutdown();
            JOptionPane.showMessageDialog(null, "An fatal error occurred. The game needs to exit: \n" + throwable.getMessage(), "Fatal Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}