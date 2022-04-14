package edu.kit.informatik.ui;

import java.util.Scanner;
import edu.kit.informatik.logic.OrlogGameManager;
import edu.kit.informatik.ui.commands.Commands;

/**
 * Represents the UI of the Orlog-Program.
 *
 * @author uogok
 * @version 4
 */
public class OrlogUI {
    private static final String COMMAND_SEPARATOR = " ";
    private static final String PARAMETER_SEPARATOR = ";";

    private static final String COMMAND_PRINT = "print";
    private static final int PRINT_PARAMETER_COUNT = 0;

    private static final String COMMAND_ROLL = "roll";
    private static final int ROLL_PARAMETER_COUNT = 6;

    private static final String COMMAND_TURN = "turn";
    private static final int TURN_PARAMETER_COUNT = 0;

    private static final String COMMAND_GOD_FAVOR = "godfavor";
    private static final int GOD_FAVOR_PARAMETER_COUNT = 2;
    private static final int GOD_FAVOR_PHASE_P1 = 4;
    private static final int GOD_FAVOR_PHASE_P2 = 5;

    private static final String COMMAND_EVALUATE = "evaluate";
    private static final int EVALUATE_PARAMETER_COUNT = 0;

    private static final String COMMAND_QUIT = "quit";
    private static final int QUIT_PARAMETER_COUNT = 0;

    private static final String ERROR_NOT_ENOUGH_CHAR = "Error, the "
            + "given input doesn't match the number of characters needed to run the command '";
    private static final String ERROR_NOT_ENOUGH_WORDS = "Error, the "
            + "given input doesn't match the number of words needed to run the command '";

    private boolean quit;
    private OrlogGameManager orlogGameManager;
    private Commands[] commandsOrderArray;
    private boolean didGodFavorHappen;

    /**
     * Constructor for class OrlogUI.
     * 
     * @param orlogGameManager is to be set as Game Manager.
     */
    public OrlogUI(OrlogGameManager orlogGameManager) {
        this.quit = false;
        this.orlogGameManager = orlogGameManager;
        this.didGodFavorHappen = false;
        setCommandArray();
    }

    /**
     * Sets the array in the order of commands which is to be executed.
     */
    public void setCommandArray() {

        commandsOrderArray = new Commands[] { Commands.roll, Commands.turn, Commands.roll, Commands.turn, Commands.turn,
                Commands.evaluate, Commands.turn };
    }

    private void executeCommandsInCommandArray(String[] parameters, String command, String input)
            throws IllegalArgumentException {
        switch (command) {
        case COMMAND_ROLL:
            replyOnConsole(roll(parameters));
            break;
        case COMMAND_TURN:
            if (input.equals(COMMAND_TURN)) {
                replyOnConsole(turn(parameters));
                setDidGodFavorHappen(false);
            } else {
                throw new IllegalArgumentException(ERROR_NOT_ENOUGH_CHAR + COMMAND_TURN + "'.");
            }
            break;
        case COMMAND_EVALUATE:
            if ((input.equals(COMMAND_EVALUATE))) {
                replyOnConsole(evaluate(parameters));
            } else {
                throw new IllegalArgumentException(ERROR_NOT_ENOUGH_CHAR + COMMAND_EVALUATE + "'.");
            }
            break;
        default:
            throw new IllegalArgumentException("Error, the first switch command is not working as expected.");
        }
    }

    private void executeCommandsOutsideOfCommandArray(String[] parameters, String command, String input, int phase)
            throws IllegalArgumentException {
        switch (command) {
        case COMMAND_PRINT:
            if (input.equals(COMMAND_PRINT)) {
                replyOnConsole(print(parameters));
            } else {
                throw new IllegalArgumentException(ERROR_NOT_ENOUGH_CHAR + COMMAND_PRINT + "'.");
            }
            break;
        case COMMAND_QUIT:
            if (input.equals(COMMAND_QUIT)) {
                this.quit = quit(parameters);
            } else {
                throw new IllegalArgumentException(ERROR_NOT_ENOUGH_CHAR + COMMAND_QUIT + "'.");
            }
            break;
        case COMMAND_GOD_FAVOR:
            if ((phase == GOD_FAVOR_PHASE_P1 || phase == GOD_FAVOR_PHASE_P2) && !hasGodFavorBeenUsed()) {
                replyOnConsole(godfavor(parameters));
                setDidGodFavorHappen(true);
            } else {
                throw new IllegalArgumentException("Error, the command 'godfavor' cannot be used in this turn.");
            }
            break;
        default:
            throw new IllegalArgumentException("Error, the command '" + command + "' cannot be used in this turn.");
        }
    }

    /**
     * The interactive method which operates all inputs.
     */
    public void interactiveSystem() {
        Scanner sc = new Scanner(System.in);
        do {
            int phase = orlogGameManager.getPhaseOfGame();
            boolean didGameEnd = orlogGameManager.isDidGameEnd();
            String input = sc.nextLine();
            String[] split = input.split(COMMAND_SEPARATOR);
            String command = split[0];
            String[] parameters = split.length > 1 ? split[1].split(PARAMETER_SEPARATOR) : null;
            try {
                if (doesCommandExist(command) && split.length <= 2) {
                    if (commandsOrderArray[phase].toString().equals(command) && !didGameEnd) {

                        // checks the commands which are in the commandArray.
                        executeCommandsInCommandArray(parameters, command, input);
                    } else {
                        // checks the commands which aren't in the commandArray.
                        executeCommandsOutsideOfCommandArray(parameters, command, input, phase);
                    }
                } else {
                    throw new IllegalArgumentException(
                            "Error, the given command does not exist. Please pay attention to syntax.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error, the given input doesn't contain the required format for the number.");
            } catch (IllegalArgumentException iae) {
                System.out.println(iae.getMessage());
            }
        } while (!quit);
        sc.close();
        return;
    }

    /**
     * Returns true, if god favor has been used in this phase.
     * 
     * @return true, if god favor has been used in this phase.
     */
    public boolean hasGodFavorBeenUsed() {
        return didGodFavorHappen;
    }

    /**
     * Sets the value if god-favor has already been used in this phase.
     * 
     * @param didGodFavorHappen is to be set.
     */
    public void setDidGodFavorHappen(boolean didGodFavorHappen) {
        this.didGodFavorHappen = didGodFavorHappen;
    }

    private boolean doesCommandExist(String command) {
        Commands[] cArray = new Commands[] { Commands.roll, Commands.turn, Commands.evaluate, Commands.godfavor,
                Commands.quit, Commands.print };
        for (int i = 0; i < cArray.length; i++) {
            if (command.equals(cArray[i].toString())) {
                return true;
            }
        }
        return false;
    }

    private String print(String[] parameters) throws IllegalArgumentException {
        if ((PRINT_PARAMETER_COUNT == 0 && parameters == null) || parameters.length == PRINT_PARAMETER_COUNT) {
            return orlogGameManager.print();
        } else {
            throw new IllegalArgumentException(ERROR_NOT_ENOUGH_WORDS + COMMAND_PRINT + "'.");
        }
    }

    private String godfavor(String[] parameters) throws NumberFormatException, IllegalArgumentException {
        if (parameters != null && parameters.length == GOD_FAVOR_PARAMETER_COUNT) {
            String gf = parameters[0];
            int level = Integer.valueOf(parameters[1]);
            return orlogGameManager.godFavor(gf, level);
        } else {
            throw new IllegalArgumentException(ERROR_NOT_ENOUGH_WORDS + COMMAND_GOD_FAVOR + "'.");
        }
    }

    private String roll(String[] parameters) throws IllegalArgumentException {
        if (parameters != null && parameters.length == ROLL_PARAMETER_COUNT) {
            return orlogGameManager.roll(parameters);
        } else {
            throw new IllegalArgumentException(ERROR_NOT_ENOUGH_WORDS + COMMAND_ROLL + "'.");
        }
    }

    private String turn(String[] parameters) throws IllegalArgumentException {
        if ((TURN_PARAMETER_COUNT == 0 && parameters == null) || parameters.length == TURN_PARAMETER_COUNT) {
            return orlogGameManager.turn();
        } else {
            throw new IllegalArgumentException(ERROR_NOT_ENOUGH_WORDS + COMMAND_TURN + "'.");
        }
    }

    private String evaluate(String[] parameters) throws IllegalArgumentException {
        if ((EVALUATE_PARAMETER_COUNT == 0 && parameters == null) || parameters.length == EVALUATE_PARAMETER_COUNT) {
            return orlogGameManager.evaluate();
        } else {
            throw new IllegalArgumentException(ERROR_NOT_ENOUGH_WORDS + COMMAND_TURN + "'.");
        }
    }

    private boolean quit(String[] parameters) throws IllegalArgumentException {
        if ((QUIT_PARAMETER_COUNT == 0 && parameters == null) || parameters.length == QUIT_PARAMETER_COUNT) {
            return true;
        } else {
            throw new IllegalArgumentException(ERROR_NOT_ENOUGH_WORDS + COMMAND_QUIT + "'.");
        }
    }

    private void replyOnConsole(String str) {
        if (!str.isEmpty()) {
            System.out.println(str);
        }
    }
}
