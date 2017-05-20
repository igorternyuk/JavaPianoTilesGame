package tedonttouchthewhitetile;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.Timer;

public class Model implements ConstantModel, ActionListener{
    private final int ANIMATION_SPEED = 5;
    private final int TIMER_DELAY = 7;
    private final ArrayList<ViewUpdater> views = new ArrayList<>();
    private final LinkedList<Tile> tiles = new LinkedList<>();
    private final Random rand = new Random();
    private final Timer timer;
    private long score = 0L;
    private int ticker = 0;
    private GameState gameState = GameState.PENDING;
    
    public Model(){
       createInitialTileSetup();
       timer = new Timer(TIMER_DELAY, this);
       timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ++ticker;
        if(gameState == GameState.ANIMATION){
            this.animateTiles();
            if(isLastRowToDelete()){
                removeLastRow();
                gameState = GameState.PENDING;
                ticker = 0;
            }
        }
        notifyAllViews();
    }
    
    public void click(int x, int y) {
        if (gameState == GameState.PENDING) {
            if ((y / Constants.TILE_HEIGHT) != Constants.ROW_COUNT - 1) {
                //System.out.println("Кликнули не на последний ряд");
                gameState = GameState.GAME_OVER;
            } else {
                addNewRow(-Constants.TILE_HEIGHT, true);
                for (Tile tile : tiles) {
                    if (tile.isPointInside(x, y)) {
                        if (tile.isBlack_) {
                            //System.out.println("Кликнули на черный тайл");
                            score += ticker < 100 ? 100 - ticker : 10;
                            gameState = GameState.ANIMATION;
                            //System.out.println("Включаем анимацию");
                        } else {
                            //System.out.println("Кликнули на белый тайл");
                            gameState = GameState.GAME_OVER;
                        }
                        break;
                    }
                }
            }
        }

    }
    
    public void prepareNewGame(){
        tiles.clear();
        createInitialTileSetup();
        score = 0;
//        ticker = 0;
        gameState = GameState.PENDING;
    }
    
    public void addView(ViewUpdater view){
        views.add(view);        
    }
    
    public boolean removeView(ViewUpdater view){
        return views.remove(view);
    }
    
    public void notifyAllViews(){
        for(ViewUpdater view : views){
            view.updateView();
        }
    }
    @Override
    public int getTileNumber() {
        return tiles.size();
    }

    @Override
    public int getTileX(int index) {
        return tiles.get(index).getX();
    }
    
    @Override
    public int getTileY(int index) {
        return tiles.get(index).getY();
    }

    @Override
    public boolean isTileBlack(int index) {
        return tiles.get(index).isBlack();
    }

    @Override
    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    @Override
    public long getScore() {
        return score;
    }
    
    private void createInitialTileSetup(){
        for(int i = 0; i < Constants.ROW_COUNT; ++i){
            addNewRow(i * Constants.TILE_HEIGHT, false);
        }
    }
    
    private void addNewRow(int y, boolean addFront) {
        int randBlackTilePos = rand.nextInt(Constants.COL_COUNT);
        for (int j = 0; j < Constants.COL_COUNT; ++j) {
            boolean isBlack = j == randBlackTilePos;
            if(addFront){
                tiles.addFirst(new Tile(j * Constants.TILE_WIDTH, y, isBlack));
            } else {
                tiles.addLast(new Tile(j * Constants.TILE_WIDTH, y, isBlack));
            }
        }
    }
    
    private boolean isLastRowToDelete(){
        boolean result = false;
        for(Tile tile : tiles){
            if(tile.getY() >= Constants.ROW_COUNT * Constants.TILE_HEIGHT){
                result = true;
                break;
            }
        }
        return result;
    }
    
    private void removeLastRow(){
        for(int i = 0; i < Constants.COL_COUNT; ++i){
            tiles.removeLast();
        }
    }
    
    private void animateTiles(){
        for(Tile tile : tiles){
            tile.moveDown(ANIMATION_SPEED);
        }
    }
    
    public void printInfo(){
        int k = 0;
        for(Tile tile : tiles){
            System.out.println("tile number - " + (k + 1) + " X = " + tile.getX() + " Y =" + tile.getY() + " isBlack" + tile.isBlack());
            ++k;
        }

    }
    
    private enum GameState{
        PENDING,
        ANIMATION,
        GAME_OVER
    }

    private class Tile{
        Point position;
        boolean isBlack_;
        public Tile(int x, int y, boolean isBlack){
            position = new Point(x, y);
            isBlack_ = isBlack;
        }
        
        public void moveDown(int speed){
            position.y += speed;
        }
        public int getX(){
            return position.x;
        }
        
        public int getY(){
            return position.y;
        }
        
        public boolean isBlack(){
            return isBlack_;
        }
        
        public boolean isPointInside(int x, int y){
            return x > position.x && x < position.x + Constants.TILE_WIDTH &&
                   y > position.y && y < position.y + Constants.TILE_HEIGHT;
        }
    }
}
