package com.codegym.games.racer.road;

import com.codegym.games.racer.PlayerCar;
import com.codegym.games.racer.RacerGame;
import com.codegym.games.racer.ShapeMatrix;

import java.util.List;

public class Beam extends RoadObject
{
    public Beam(double x,double y)
    {
        super(RoadObjectType.BEAM,  x, y);
        speed=-2;
    }
    public void move(double boost)
    {
        this.y += boost;
    }

}
