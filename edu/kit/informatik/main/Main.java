package edu.kit.informatik.main;

import edu.kit.informatik.logic.OrlogGameManager;
import edu.kit.informatik.ui.OrlogUI;

/**
 * This class is to be run first.
 * 
 * @author uogok
 * @version 2
 */
public final class Main {

    private static final String PARAMETER_SEPARATOR = ";";
    private static final int NAME_PLAYER1_POSITION = 0;
    private static final int NAME_PLAYER2_POSITION = 1;
    private static final int HP_POSITION = 2;
    private static final int GP_POSITION = 3;
    private static final int MAX_PARAMETER_LENGTH = 4;

    /**
     * Constructor for the class Main but it cannot be instantiated.
     */
    private Main() {
        throw new AssertionError("Utility Classes must not be instantiated!");
    }

    /**
     * This method is to be executed first.
     * @param args input from cmd.
     */
    public static void main(String[] args) {

        String startDetails = args[0];
        String[] parameters = startDetails.split(PARAMETER_SEPARATOR);
        try {
            if (areParametersvalid(parameters)) {
                String namePlayer1 = parameters[NAME_PLAYER1_POSITION];
                String namePlayer2 = parameters[NAME_PLAYER2_POSITION];
                int hp = Integer.valueOf(parameters[HP_POSITION]);
                int gp = Integer.valueOf(parameters[GP_POSITION]);
                OrlogGameManager orlogGameManager = new OrlogGameManager(namePlayer1, namePlayer2, hp, gp);
                OrlogUI orlogUI = new OrlogUI(orlogGameManager);
                orlogUI.interactiveSystem();
            } else {
                return;
            }
        } catch (NumberFormatException i) {
            System.out.println("Error, the given input doesn't contain the required format for the number.");
            return;
        } catch (IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
            return;
        }
    }

    private static boolean areParametersvalid(String[] parameters)
            throws NumberFormatException, IllegalArgumentException {
        if (parameters.length != MAX_PARAMETER_LENGTH) {
            throw new IllegalArgumentException("Error, the input doesn't contain the number of parameters required. "
                    + "Number of parameters required are 4. Please don't have any spaces "
                    + "or semicolons in the cmd message.");
        }
        boolean nameFromPlayer1Valid = nameValid(parameters[NAME_PLAYER1_POSITION]);
        boolean nameFromPlayer2Valid = nameValid(parameters[NAME_PLAYER2_POSITION]);
        boolean hpValid = hpValid(Integer.valueOf(parameters[HP_POSITION]));
        boolean gpValid = gpValid(Integer.valueOf(parameters[GP_POSITION]));
        boolean valid = nameFromPlayer1Valid && nameFromPlayer2Valid && hpValid && gpValid;
        return valid;
    }

    private static boolean nameValid(String name) throws IllegalArgumentException {
        // contains space or ';' not allowed
        boolean contains = name.contains(" ") || name.contains(";") || name.isEmpty();
        if (contains) {
            throw new IllegalArgumentException("Error, the name must not contain spaces or semicolons.");
        }
        return true;
    }

    private static boolean hpValid(int hp) {
        // must be >= 5
        if (hp < 5) {
            throw new IllegalArgumentException("Error, the health points must at least be 5 points.");
        }
        return true;
    }

    private static boolean gpValid(int gp) {
        // must be >= 0
        if (gp < 0) {
            throw new IllegalArgumentException("Error, the god points must at least be 0 points.");
        }
        return true;
    }
}
