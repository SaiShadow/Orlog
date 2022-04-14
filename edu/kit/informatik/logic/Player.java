package edu.kit.informatik.logic;

import edu.kit.informatik.logic.godfavorcards.GodFavors;

/**
 * Represents the player.
 *
 * @author uogok
 * @version 3.67
 *
 */
public class Player {

    private String name;
    private int healthPoints;
    private int godPower;
    private int shieldLongRange;
    private int shieldMelee;
    private int attackMelee;
    private int attackLongRange;
    private int steal;
    private GodFavors godFavor;
    private int godFavorLevel;
    private int godFavorCost;

    /**
     * Constructor for player class.
     *
     * @param name         the players name.
     * @param healthPoints the health-points of the player.
     * @param godPower     the god power of a player.
     */
    Player(String name, int healthPoints, int godPower) {
        setGodPower(godPower);
        setHealthPoints(healthPoints);
        setName(name);
    }

    /**
     * Resets all defence, steal, attack and god favour stats of player to 0.
     */
    public void resetAfterEvaluation() {
        setShieldLongRange(0);
        setShieldMelee(0);
        setAttackMelee(0);
        setAttackLongRange(0);
        setGodFavor(null);
        setGodFavorLevel(0);
        setSteal(0);
    }

    /**
     * Subtract hp from the value in the parameter. But hp can never go lower than
     * 0.
     *
     * @param hp this amount is to be subtracted.
     */
    public void diminishHP(int hp) {
        this.healthPoints -= hp;
        if (this.healthPoints < 0) {
            setHealthPoints(0);
        }
    }

    /**
     * Subtract god-favor level from the value in the parameter.
     *
     * @param gfLevel this amount is to be subtracted.
     */
    public void diminishGodFavorLevel(int gfLevel) {
        this.godFavorLevel -= gfLevel;
    }

    /**
     * Adds hp of the player to the value in parameter.
     *
     * @param hp is to be added to the hp of the player.
     */
    public void addHP(int hp) {
        this.healthPoints += hp;
    }

    /**
     * Gets the value of how many times the player steals.
     *
     * @return value of how many times the player steals.
     */
    public int getSteal() {
        return steal;
    }

    /**
     * Sets the value of how many times the player steals.
     *
     * @param steal value of how many times the player steals.
     */
    public void setSteal(int steal) {
        this.steal = steal;
    }

    /**
     * Adds parameter value to the god points of the player.
     *
     * @param gp is to be added.
     */
    public void addGP(int gp) {
        this.godPower += gp;
    }

    /**
     * Gets god favor cost.
     *
     * @return god favor cost.
     */
    public int getGodFavorCost() {
        return godFavorCost;
    }

    /**
     * Sets god favor cost.
     *
     * @param godFavorCost is to be set.
     */
    public void setGodFavorCost(int godFavorCost) {
        this.godFavorCost = godFavorCost;
    }

    /**
     * Subtracts parameter value to the god points of the player.
     *
     * @param gp is to be added.
     */
    public void diminishGP(int gp) {
        this.godPower -= gp;
        if (this.godPower < 0) {
            setGodPower(0);
        }
    }

    /**
     * Gets players info which contains name, hp and gp.
     *
     * @return String with players info.
     */
    public String getPlayerInfo() {
        String info = getName() + ";" + getHealthPoints() + ";" + getGodPower();
        return info;
    }

    /**
     * Gets value of shieldLongRange.
     *
     * @return value of shieldLongRange.
     */
    public int getShieldLongRange() {
        return shieldLongRange;
    }

    /**
     * Sets value of shieldLongRange.
     *
     * @param shieldLongRange is to be set.
     */
    public void setShieldLongRange(int shieldLongRange) {
        this.shieldLongRange = shieldLongRange;
    }

    /**
     * Gets value of shieldMelee.
     *
     * @return value of shieldMelee.
     */
    public int getShieldMelee() {
        return shieldMelee;
    }

    /**
     * Sets value of shieldMelee.
     *
     * @param shieldMelee is to be set.
     */
    public void setShieldMelee(int shieldMelee) {
        this.shieldMelee = shieldMelee;
    }

    /**
     * Gets value of attackMelee.
     *
     * @return value of attackMelee.
     */
    public int getAttackMelee() {
        return attackMelee;
    }

    /**
     * Sets value of attackMelee.
     *
     * @param attackMelee is to be set.
     */
    public void setAttackMelee(int attackMelee) {
        this.attackMelee = attackMelee;
    }

    /**
     * Gets value of attackLongRange.
     *
     * @return value of attackLongRange.
     */
    public int getAttackLongRange() {
        return attackLongRange;
    }

    /**
     * Sets value of attackLongRange
     *
     * @param attackLongRange is to be set.
     */
    public void setAttackLongRange(int attackLongRange) {
        this.attackLongRange = attackLongRange;
    }

    /**
     * Gets value of godFavor.
     *
     * @return value of godFavor.
     */
    public GodFavors getGodFavor() {
        return godFavor;
    }

    /**
     * Sets value of godFavor.
     *
     * @param godFavor is to be set.
     */
    public void setGodFavor(GodFavors godFavor) {
        this.godFavor = godFavor;
    }

    /**
     * Gets value of godFavorLevel.
     *
     * @return value of godFavorLevel.
     */
    public int getGodFavorLevel() {
        return godFavorLevel;
    }

    /**
     * Sets value of godFavorLevel.
     *
     * @param godFavorLevel is to be set.
     */
    public void setGodFavorLevel(int godFavorLevel) {
        this.godFavorLevel = godFavorLevel;
    }

    /**
     * Gets name of player.
     *
     * @return name of player.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of player.
     *
     * @param name is to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets healthPoints of player.
     *
     * @return healthPoints of player.
     */
    public long getHealthPoints() {
        return healthPoints;
    }

    /**
     * Sets healthPoints of player.
     *
     * @param healthPoints is to be set.
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Gets godPower of player.
     *
     * @return godPower of player.
     */
    public int getGodPower() {
        return godPower;
    }

    /**
     * Sets godPower of player.
     *
     * @param godPower is to be set.
     */
    public void setGodPower(int godPower) {
        this.godPower = godPower;
    }

}
