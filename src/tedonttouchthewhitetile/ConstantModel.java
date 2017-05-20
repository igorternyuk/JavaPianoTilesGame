package tedonttouchthewhitetile;

public interface ConstantModel {
    int getTileNumber();
    int getTileX(int index);
    int getTileY(int index);
    boolean isTileBlack(int index);
    boolean isGameOver();
    long getScore();
}
