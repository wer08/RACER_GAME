package com.codegym.games.racer.road;

import com.codegym.games.racer.RacerGame;
import com.codegym.games.racer.PlayerCar;


import com.codegym.engine.cell.Game;
import com.codegym.games.racer.ShapeMatrix;

import java.util.*;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - RacerGame.ROADSIDE_WIDTH;
    private static final int FIRST_LANE_POSITION =16;
    private static final int FOURTH_LANE_POSITION = 44;
    private static final int PLAYER_CAR_DISTANCE = 12;
    private List<RoadObject> items= new ArrayList<RoadObject>();
    private List<Beam> beams = new ArrayList<Beam>();
    private List<PowerUP> powerUPS = new ArrayList<PowerUP>();
    private int passedCarsCount=0;
    
    
    public int getPassedCarsCount()
    {
        return passedCarsCount;
    }

    public List<RoadObject> getItems()
    {
        return items;
    }

    private boolean isRoadSpaceFree(RoadObject object)
    {
        ListIterator<RoadObject> iterator = items.listIterator();
        while(iterator.hasNext())
        {
            if(iterator.next().isCollisionWithDistance(object,PLAYER_CAR_DISTANCE))
                return false;
        }
        return true;
    }
    public RoadObject beamHit ()
    {

        for (RoadObject objects: items)
        {
            if (isHit(objects))
            {
                return objects;
            }
        }


        return null;
    }
    public boolean checkBeamHit ()
    {

        ListIterator<RoadObject> Iterator = items.listIterator();

            while (Iterator.hasNext())
            {
                if (isHit(Iterator.next()))
                {
                    return true;
                }
            }

        return false;
    }
    private boolean isHit(RoadObject object)
    {
        for (Beam beam:beams) {

            for (int matrixX = 0; matrixX < object.width ; matrixX++) {
                for (int matrixY = 0; matrixY < object.height; matrixY++) {

                    if (object.matrix[matrixY][matrixX] != 0 && matrixX + beam.x == object.x && matrixY + beam.y == object.y)
                        return true;
                }
            }
        }
       return false;
    }


    public boolean checkPowerUP(PlayerCar playerCar)
    {
        ListIterator <PowerUP> iterator = powerUPS.listIterator();
        while(iterator.hasNext())
        {
            if(iterator.next().isCollision(playerCar))
            {
                System.out.println("PowerUP checked");
                return true;
            }
        }
        return false;
    }
    public boolean checkCrash(PlayerCar playerCar)
    {
         ListIterator <RoadObject> iterator = items.listIterator();
        while(iterator.hasNext())
        {
            if(iterator.next().isCollision(playerCar))
            {
              return true;  
            }
        }
        return false;
    }

    private void deletePassedPowerUps()
    {
        ListIterator<PowerUP> iterator = powerUPS.listIterator();
        while(iterator.hasNext())
        {
            PowerUP powerUP = iterator.next();
            if(powerUP.y >= RacerGame.HEIGHT)
            {
                System.out.println("usunieto PowerUP");
                iterator.remove();
            }
        }
    }
    
    
    private void deletePassedItems()
    {
        ListIterator <RoadObject> iterator = items.listIterator();
        while(iterator.hasNext())
        {
            RoadObject item = iterator.next();
            if(item.y >= RacerGame.HEIGHT)
                {
                    iterator.remove();
                    if(item.type != RoadObjectType.SPIKE)
                    passedCarsCount++;
                }
        }
                
        
    }
    private void deletePassedBeams()
    {
        ListIterator <Beam>iterator = beams.listIterator();
        while(iterator.hasNext())
        {
            Beam item = iterator.next();
            if(item.y <= 0)
            {
                iterator.remove();
            }
        }


    }
    
    
    private boolean spikeExists()
    {
        for(RoadObject roadObject : items)
        {
            if(roadObject.type == RoadObjectType.SPIKE)
            return true;
        }
        return false;
    }
    
    private boolean movingCarExists()
    {
        ListIterator<RoadObject> iterator = items.listIterator();
        while(iterator.hasNext())
        {
            if(iterator.next().type == RoadObjectType.DRUNK_CAR )
            return true;
            
        }
        return false;
    }
    private boolean powerUPExists()
    {
        ListIterator<PowerUP> iterator = powerUPS.listIterator();
        while(iterator.hasNext())
        {
                return true;
        }
        return false;
    }
    private void generatePowerUP(Game game)
    {
        if(game.getRandomNumber(100) < 10 && !powerUPExists())
        {
            //System.out.println("Wygenerowano powerUP");
            addPowerUPS(game);
        }

    }
    
    private void generateMovingCar(Game game)
    {
        if(game.getRandomNumber(100)<10&&!movingCarExists())
        {
            addRoadObject(RoadObjectType.DRUNK_CAR,game);
        }
    }
    public Beam generateBeam(RacerGame game)
    {

        Beam beam = new Beam(game.getPlayer().x+2,RacerGame.HEIGHT- ShapeMatrix.PLAYER.length-4);
        beams.add(beam);
        return beam;
    }
    
    private void generateSpike( Game game)
    {
   
        if(game.getRandomNumber(100) < 10 && !spikeExists())
        addRoadObject(RoadObjectType.SPIKE,game);
    }
    private void generateRegularCar(Game game)
    {
        int carTypeNumber = game.getRandomNumber(4);
        if(game.getRandomNumber(100)<30)
        {
            addRoadObject(RoadObjectType.values()[carTypeNumber],game);
        }
    }
    
    public void generateNewRoadObjects(Game game)
    {
        generateSpike(game);   
        generateRegularCar(game);
        generateMovingCar(game);
        generatePowerUP(game);
    }
    
    
    public void draw(Game game)
    {
        for(RoadObject roadObject: items)
        {
            roadObject.draw(game);
        }
        for(Beam beams: beams)
        {
            beams.draw(game);
        }
        for (PowerUP powerUP: powerUPS)
        {
            //System.out.println("Narysowano PowerUP");
            powerUP.draw(game);
        }
    }
    public void move(int boost)
    {
         for(RoadObject roadObject: items)
        {
            roadObject.move(roadObject.speed + boost,items);
        }
        for (Beam beams:beams)
        {
            beams.move(beams.speed);
        }
        for (PowerUP powers:powerUPS)
        {
            powers.move(powers.speed);
        }
        deletePassedItems();
        deletePassedBeams();
        deletePassedPowerUps();
        
    }
    private void addPowerUPS(Game game)
    {
        int x = game.getRandomNumber(FIRST_LANE_POSITION,FOURTH_LANE_POSITION);
        int y = -1*RoadObject.getHeight(RoadObjectType.POWER_UP);
        PowerUP powerUP = createPowerUP(x,y,game);
        if(powerUP != null&&isRoadSpaceFree(powerUP))
        {
           System.out.println("Dodano PowerUP");
            System.out.println(powerUP.y);
            powerUPS.add(powerUP);
        }

    }
    
    
    private void addRoadObject(RoadObjectType type, Game game)
    {
        
        int x = game.getRandomNumber(FIRST_LANE_POSITION,FOURTH_LANE_POSITION);
        int y = -1*RoadObject.getHeight(type);
        RoadObject roadObject = createRoadObject(type,x,y,game);
        if(roadObject != null&&isRoadSpaceFree(roadObject))
        items.add(roadObject);
        
        
    }

    public void newLevelSpeedUp()
    {
        for (RoadObject objects:items)
        {
            double previousSpeed = objects.speed;
            objects.setSpeed(previousSpeed+1);
        }
    }
    private PowerUP createPowerUP(int x,int y, Game game)
    {
        return new PowerUP(x,y);
    }
    

    private RoadObject createRoadObject(RoadObjectType type, int x, int y,Game game)
    {
        if (type==RoadObjectType.SPIKE)
        {
            return new Spike(x,y);
        }
        else if(type==RoadObjectType.POWER_UP)
        {
            return new PowerUP(x,y);
        }
        else if(type==RoadObjectType.DRUNK_CAR)
        {
            return new MovingCar(x,y); 
        }
        else if(type!=RoadObjectType.SPIKE&&type!=RoadObjectType.DRUNK_CAR)
        {
            int random = game.getRandomNumber(40);
            Car car = new Car(type,x,y);
            if(random<10)
            car.setSpeed(2);
            return car;
        }
        else
        {
            return null;
        }
    }
}
