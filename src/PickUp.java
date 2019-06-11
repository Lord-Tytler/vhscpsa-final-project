public interface PickUp{
    /**
     * runs all pickup behavior of item then returns true if pick up was successful
     * @param grabber - character object that is attepting to pick up item
     * @return boolean representing whether pick up attempt was successful or not
     */
    public boolean pickUp(Character grabber);
    public boolean getIsHeld();
}