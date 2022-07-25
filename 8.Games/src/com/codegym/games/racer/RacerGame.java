package com.codegym.games.racer;

import com.codegym.engine.cell.Color;
import com.codegym.engine.cell.Game;
import com.codegym.engine.cell.Key;
import com.codegym.games.racer.road.RoadManager;
import com.codegym.games.racer.road.RoadObject;

import java.util.List;
import java.util.ListIterator;

public class RacerGame extends Game 
{
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH / 2;
    public static final int ROADSIDE_WIDTH = 14;
    private static final int RACE_GOAL_CARS_COUNT = 10;

    public PlayerCar getPlayer() {
        return player;
    }

    private int score;
    private boolean isWon;
    
    private ProgressBar progressBar;
    
    private FinishLine finishLine;
    
    private boolean isGameStopped;
    
    private PlayerCar player;
    
    private RoadManager roadManager;

    private RoadMarking roadMarking;
    
    private void win()
    {
        isWon =true;
        isGameStopped = true;
        showMessageDialog(Color.PINK,"YOU WON !!! CONGRATULATIONS",Color.BLACK,32);
        stopTurnTimer();
        
    }
    
    private void gameOver()
    {
        isGameStopped = true;
        showMessageDialog(Color.BLACK,"You Lost",Color.RED,32);
        stopTurnTimer();
        player.stop();
    }

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    @Override
    public void onTurn(int step) {
        roadManager.generateNewRoadObjects(this);
        score -= 5;
        setScore(score);
         if(finishLine.isCrossed(player))
                {
                    win();
                    drawScene();
                    return;
                }
        if(roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT)
            {
                finishLine.show();
                moveAll();
            }
        if(roadManager.checkCrash(player))
        {
            gameOver();
            drawScene();
        }
      if(roadManager.checkBeamHit())
        {
           RoadObject objectToRemove = roadManager.beamHit();
            ListIterator<RoadObject> iterator = roadManager.getItems().listIterator();
            while(iterator.hasNext())
            {
                if(iterator.next().equals(objectToRemove))
                {
                    iterator.remove();
                }
            }

        }
        else
        {
        moveAll();
        drawScene();
        }
        
    }

    private void createGame() 
    {
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        roadManager = new RoadManager();
        finishLine = new FinishLine();
        score = 3500;
        drawScene();
        setTurnTimer(40);
        isGameStopped = false;
        isWon=false;
    }

    private void drawScene() {
        drawField();
        progressBar.draw(this);
        finishLine.draw(this);
        roadMarking.draw(this);
        player.draw(this);
        roadManager.draw(this);
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (x == CENTER_X) {
                    setCellColor(x, y, Color.WHITE);
                } else if (x >= ROADSIDE_WIDTH && x < WIDTH - ROADSIDE_WIDTH) {
                    setCellColor(x, y, Color.DIMGREY);
                } else {
                    setCellColor(x, y, Color.GREEN);
                }
            }
        }
    }

    private void moveAll() {
        roadMarking.move(player.speed);
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
        player.move();
    }


    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x > WIDTH - 1 || x < 0 || y < 0 || y > HEIGHT - 1) {
            return;
        }
        super.setCellColor(x, y, color);
    }

    @Override
    public void onKeyPress(Key key) {
        if(key== Key.SPACE&&isGameStopped==true)
        {
            createGame();
        }
        if(key==Key.ESCAPE&&isWon==true)
        {
            createGame();
           roadManager.newLevelSpeedUp();
        }
        if(key==Key.ENTER)
        {

            roadManager.generateBeam(this);
        }

        
        if(key == Key.UP)
        {
            player.speed =2;
        }
        if (key == Key.RIGHT) {
            player.setDirection(Direction.RIGHT);
        } else if (key == Key.LEFT) {
            player.setDirection(Direction.LEFT);
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        
        if(key==Key.UP)
        player.speed =1;
        
        if ((key == Key.RIGHT && player.getDirection() == Direction.RIGHT)
                || (key == Key.LEFT && player.getDirection() == Direction.LEFT)) {
            player.setDirection(Direction.NONE);
        }
    }
}