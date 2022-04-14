package edu.kit.informatik.logic;

import edu.kit.informatik.logic.godfavorcards.GodFavors;

/**
 * Represents the Game Manager.
 *
 * @author uogok
 * @version 5.6
 */
public class OrlogGameManager {

    private static final int THORS_STRIKE_BASE_COST = 4;
    private static final int IDUNS_REGENERATION_BASE_COST = 4;
    private static final int MAXIMUM_AMOUNT_OF_GOD_POWER = 4;
    private int turnOfGame;
    private int phaseOfGame;
    private Player player1;
    private Player player2;
    private boolean didGameEnd;
    private boolean didEvaluationHappen;

    /**
     * Constructor for class.
     *
     * @param name1 Name of the first player.
     * @param name2 Name of the second player.
     * @param hp    health points of both players.
     * @param gp    god power of both players.
     */
    public OrlogGameManager(String name1, String name2, int hp, int gp) {
        this.turnOfGame = 0;
        this.phaseOfGame = 0;
        this.player1 = new Player(name1, hp, gp);
        this.player2 = new Player(name2, hp, gp);
        this.didGameEnd = false;
        this.didEvaluationHappen = false;
    }

    /**
     * Returns phase of game.
     *
     * @return Phase of game.
     */
    public int getPhaseOfGame() {
        return phaseOfGame;
    }

    /**
     * Sets phase of game.
     *
     * @param phaseOfGame is to be set as phaseOfGame.
     */
    public void setPhaseOfGame(int phaseOfGame) {
        this.phaseOfGame = phaseOfGame;
    }

    /**
     * Returns a String with both players name, hp and gp.
     *
     * @return String with both players info.
     */
    public String print() {
        String p1 = player1.getPlayerInfo();
        String p2 = player2.getPlayerInfo();
        return p1 + System.lineSeparator() + p2;
    }

    /**
     * Check if game ended with draw or win.
     *
     * @return true, if game ended.
     */

    public boolean isDidGameEnd() {
        return didGameEnd;
    }

    /**
     * Sets if game ended.
     *
     * @param didGameEnd this value is to be set as didGameEnd.
     */

    public void setDidGameEnd(boolean didGameEnd) {
        this.didGameEnd = didGameEnd;
    }

    /**
     * Represents a roll command.
     *
     * @param parameters A String array with 6 elements containing combat elements.
     * @return OK, if the roll function has been executed as expected.
     * @throws IllegalArgumentException throws exception.
     */

    public String roll(String[] parameters) throws IllegalArgumentException {
        int gp = 0;
        int attMelee = 0;
        int attLong = 0;
        int defMelee = 0;
        int defLong = 0;
        int steal = 0;

        for (int i = 0; i < parameters.length; i++) {
            switch (parameters[i]) {
                case "MA":
                    attMelee++;
                    break;
                case "MD":
                    defMelee++;
                    break;
                case "GMD":
                    defMelee++;
                    gp++;
                    break;
                case "RA":
                    attLong++;
                    break;
                case "GRA":
                    attLong++;
                    gp++;
                    break;
                case "RD":
                    defLong++;
                    break;
                case "GRD":
                    defLong++;
                    gp++;
                    break;
                case "ST":
                    steal++;
                    break;
                case "GST":
                    steal++;
                    gp++;
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Error, the given dice roll doesn't exist. " + "Please pay attention to syntax.");
            }
        }
        if (gp > MAXIMUM_AMOUNT_OF_GOD_POWER) {
            throw new IllegalArgumentException("Error, chosen too many combat elements with god-power. "
                    + "Please only select a maximum of 4 god-power combat elements.");
        } else {
            int[] effects = new int[] {gp, attMelee, attLong, defMelee, defLong, steal};
            addEffectstoPlayer(effects);
            goToNextPhase();
            return "OK";
        }
    }

    /**
     * Represents godfavor command.
     *
     * @param gf    represents an element of the enum GodFavors.
     * @param level Level of god favor.
     * @return OK, if the roll function has been executed as expected.
     * @throws IllegalArgumentException throws exception
     */

    public String godFavor(String gf, int level) throws IllegalArgumentException {
        Player examplePlayer = getPlayerWhoseTurnItIs();
        GodFavors godFavorOfPlayer = godFavorStringToEnum(gf);

        examplePlayer.setGodFavorCost(calculateGodFavorCost(godFavorOfPlayer, level));
        examplePlayer.setGodFavor(godFavorOfPlayer);
        examplePlayer.setGodFavorLevel(level);
        return "OK";
    }

    private int calculateGodFavorCost(GodFavors gf, int level) throws IllegalArgumentException {
        String godsGrace = gf.toString();
        if (checkLevelValidity(level)) {
            switch (godsGrace) {
                case "TS":
                    return THORS_STRIKE_BASE_COST * level;
                case "TT":
                    return 3 * level;
                case "IR":
                    return IDUNS_REGENERATION_BASE_COST + 3 * (level - 1);
                default:
                    throw new IllegalArgumentException(
                            "Error, the given god-favor does not exist. Please pay attention to syntax.");
            }
        } else {
            throw new IllegalArgumentException(
                    "Error, the level of a god-favor must be between 1-3 including 1 and 3.");
        }
    }

    /**
     * Represents evaluate command.
     *
     * @return OK, if the roll function has been executed as expected.
     */
    public String evaluate() {
        evaluateCombatElementsWithoutGod();
        String printMessage = evaluateIfGameEnded();
        if (!isDidGameEnd()) {
            evaluateGodFavors();
            printMessage = evaluateIfGameEnded();
        }
        setDidEvaluationHappen(true);
        goToNextPhase();
        return printMessage;
    }

    private void goToNextTurn() {
        ++this.turnOfGame;
    }

    private void goToNextPhase() {
        ++this.phaseOfGame;
    }

    private void resetTurnOfGame() {
        if (isDidEvaluationHappen()) {
            this.turnOfGame = -1;
            setDidEvaluationHappen(false);
        }
    }

    private void resetPhaseOfGame() {
        if (isDidEvaluationHappen()) {
            this.phaseOfGame = -1;
            setDidEvaluationHappen(false);
        }
    }

    private void evaluateCombatElementsWithoutGod() {
        evaluateMeleeAttacks();
        evaluateLongRangeAttacks();
        evaluateSteals();
    }

    /**
     * Returns value of didEvaluationHappen.
     * 
     * @return value of didEvaluationHappen.
     */
    public boolean isDidEvaluationHappen() {
        return didEvaluationHappen;
    }

    /**
     * Sets value of didEvaluationHappen.
     *
     * @param didEvaluationHappen is to be set.
     */
    public void setDidEvaluationHappen(boolean didEvaluationHappen) {
        this.didEvaluationHappen = didEvaluationHappen;
    }

    private String evaluateIfGameEnded() {
        String message;
        if (player1.getHealthPoints() != 0 && player2.getHealthPoints() != 0) {
            return print();
        } else if (player1.getHealthPoints() == 0 && player2.getHealthPoints() == 0) {
            message = "draw";
        } else {
            message = (player1.getHealthPoints() == 0) ? (player2.getName() + " wins") : (player1.getName() + " wins");
        }
        setDidGameEnd(true);
        return message;
    }

    private void evaluateGodFavors() {
        if (checkIfPlayerHasGodFavor(1) || checkIfPlayerHasGodFavor(2)) {
            evaluateTT();
            evaluateTS();
            evaluateIR();
        }
    }

    private boolean checkIfPlayerHasGodFavor(int id) {
        if (id == 1) {
            return player1.getGodFavor() != null;
        } else if (id == 2) {
            return player2.getGodFavor() != null;
        }
        return false;
    }

    private void evaluateTT() {
        if (checkIfPlayerHasGodFavor(1) && player1.getGodFavor().toString().equals("TT")) {
            int gfLevel = player1.getGodFavorLevel();
            int cost = player1.getGodFavorCost();
            int gp = player1.getGodPower();

            if (gp >= cost && gfLevel >= 1 && gfLevel <= 3) {
                player2.diminishGodFavorLevel(gfLevel);
                player1.diminishGP(cost);
            }
        }
        if (checkIfPlayerHasGodFavor(2) && player2.getGodFavor().toString().equals("TT")) {
            int gfLevel = player2.getGodFavorLevel();
            int cost = player2.getGodFavorCost();
            int gp = player2.getGodPower();

            if (gp >= cost && gfLevel >= 1 && gfLevel <= 3) {
                player1.diminishGodFavorLevel(gfLevel);
                player2.diminishGP(cost);
            }
        }
    }

    private int calculateThorsThunderDMG(int level) {
        return checkLevelValidity(level) ? (2 + 3 * (level - 1)) : 0;
    }

    private void evaluateTS() {
        if (checkIfPlayerHasGodFavor(1) && player1.getGodFavor().toString().equals("TS")) {
            int gfLevel = player1.getGodFavorLevel();
            int cost = player1.getGodFavorCost();
            int gp = player1.getGodPower();

            if (gp >= cost) {
                player2.diminishHP(calculateThorsThunderDMG(gfLevel));
                player1.diminishGP(cost);
            }
        }
        if (checkIfPlayerHasGodFavor(2) && player2.getGodFavor().toString().equals("TS")) {
            int gfLevel = player2.getGodFavorLevel();
            int cost = player2.getGodFavorCost();
            int gp = player2.getGodPower();

            if (gp >= cost) {
                player1.diminishHP(calculateThorsThunderDMG(gfLevel));
                player2.diminishGP(cost);
            }
        }
    }

    private int calculateIdunsRegeneration(int level) {
        return checkLevelValidity(level) ? (2 * level) : 0;
    }

    private boolean checkLevelValidity(int level) {
        return level >= 1 && level <= 3;
    }

    private void evaluateIR() {

        if (player1.getHealthPoints() != 0 && checkIfPlayerHasGodFavor(1)
                && player1.getGodFavor().toString().equals("IR")) {
            int gfLevel = player1.getGodFavorLevel();
            int cost = player1.getGodFavorCost();
            int gp = player1.getGodPower();

            if (gp >= cost) {
                player1.addHP(calculateIdunsRegeneration(gfLevel));
                player1.diminishGP(cost);
            }
        }
        if (player2.getHealthPoints() != 0 && checkIfPlayerHasGodFavor(2)
                && player2.getGodFavor().toString().equals("IR")) {
            int gfLevel = player2.getGodFavorLevel();
            int cost = player2.getGodFavorCost();
            int gp = player2.getGodPower();

            if (gp >= cost) {
                player2.addHP(calculateIdunsRegeneration(gfLevel));
                player2.diminishGP(cost);
            }
        }
    }

    private void evaluateSteals() {
        int p1Steal = player1.getSteal();
        int p2Steal = player2.getSteal();

        if (p1Steal > p2Steal) {
            // player1 steals
            if (p1Steal > player2.getGodPower()) {
                player1.addGP(player2.getGodPower());
                player2.setGodPower(0);
            } else {
                player2.diminishGP(p1Steal);
                player1.addGP(p1Steal);
            }
        } else if (p2Steal > p1Steal) {
            // player2 steals
            if (p2Steal > player1.getGodPower()) {
                player2.addGP(player1.getGodPower());
                player1.setGodPower(0);
            } else {
                player1.diminishGP(p2Steal);
                player2.addGP(p2Steal);
            }
        }
    }

    private void evaluateLongRangeAttacks() {

        int p1Shield = player1.getShieldLongRange();
        int p1Attack = player1.getAttackLongRange();
        int p2Shield = player2.getShieldLongRange();
        int p2Attack = player2.getAttackLongRange();

        if (p1Attack > p2Shield) {
            player2.diminishHP(p1Attack - p2Shield);
        }
        if (p2Attack > p1Shield) {
            player1.diminishHP(p2Attack - p1Shield);
        }
    }

    private void evaluateMeleeAttacks() {
        int p1Shield = player1.getShieldMelee();
        int p1Attack = player1.getAttackMelee();
        int p2Shield = player2.getShieldMelee();
        int p2Attack = player2.getAttackMelee();

        if (p1Attack > p2Shield) {
            player2.diminishHP(p1Attack - p2Shield);
        }
        if (p2Attack > p1Shield) {
            player1.diminishHP(p1Attack - p2Shield);
        }
    }

    /**
     * Returns a GodFavor in an enum element instead of String.
     *
     * @param gf String to be converted.
     * @return a GodFavor in an enum element instead of String.
     */
    public GodFavors godFavorStringToEnum(String gf) {
        GodFavors godFavor = null;
        switch (gf) {
            case "TS":
                godFavor = GodFavors.TS;
                break;
            case "TT":
                godFavor = GodFavors.TT;
                break;
            case "IR":
                godFavor = GodFavors.IR;
                break;
            default:
                throw new IllegalArgumentException(
                        "Error, the given god-favor doesn't exist. Please pay attention to syntax.");
        }
        return godFavor;
    }

    private Player getPlayerWhoseTurnItIs() {
        return (turnOfGame % 2 == 0) ? player1 : player2;
    }

    private void addEffectstoPlayer(int[] effects) {
        Player examplePlayer = getPlayerWhoseTurnItIs();
        examplePlayer.addGP(effects[0]);
        examplePlayer.setAttackMelee(effects[1]);
        examplePlayer.setAttackLongRange(effects[2]);
        examplePlayer.setShieldMelee(effects[3]);
        examplePlayer.setShieldLongRange(effects[3 + 1]);
        examplePlayer.setSteal(effects[3 + 2]);
    }

    /**
     * Represents the turn command.
     *
     * @return OK, if method works as expected.
     */
    public String turn() {
        resetPhaseOfGame();
        resetTurnOfGame();
        goToNextPhase();
        goToNextTurn();
        return getPlayerWhoseTurnItIs().getName();

    }

    /**
     * Gets the turn of the game.
     * 
     * @return turn of game.
     */
    public int getTurnOfGame() {
        return turnOfGame;
    }
}
