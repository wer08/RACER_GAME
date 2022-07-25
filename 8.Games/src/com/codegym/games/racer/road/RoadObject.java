package com.codegym.games.racer.road;

import com.codegym.games.racer.ShapeMatrix;
import com.codegym.games.racer.GameObject;
import java.util.List;

public class RoadObject extends GameObject {
    public RoadObjectType type;
    public int speed;
    
    public RoadObject(RoadObjectType type, int x, int y) {
        super(x, y);
        this.type = type;
        this.matrix = getMatrix(type);
        this.width = matrix[0].length;
        this.height = matrix.length;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void move(int boost, List<RoadObject> roadObjects)
    {
        this.y += boost;
    }

    public boolean isCollisionWithDistance(RoadObject roadObject, int distance) {
        if ((x - distance > roadObject.x + roadObject.width) || (x + width + distance < roadObject.x)) {
            return false;
        }

        if ((y - distance > roadObject.y + roadObject.height) || (y + height + distance < roadObject.y)) {
            return false;
        }

        return true;
    }

    private static int[][] getMatrix(RoadObjectType type) {
        switch (type) {
            case CAR:
                return ShapeMatrix.PASSENGER_CAR;
            case BUS:
                return ShapeMatrix.BUS;
            case SPORT_CAR:
                return ShapeMatrix.SPORT_CAR;
            case SPIKE:
                return ShapeMatrix.SPIKE;
            case DRUNK_CAR:
                return ShapeMatrix.DRUNK_CAR;
            case BEAM:
                return ShapeMatrix.BEAM;
            case POWER_UP:
                return ShapeMatrix.POWERUP;
            default:
                return ShapeMatrix.TRUCK;
        }
    }

    public static int getHeight(RoadObjectType type) {
        return getMatrix(type).length;
    }
}