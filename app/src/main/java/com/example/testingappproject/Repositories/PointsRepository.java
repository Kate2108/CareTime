package com.example.testingappproject.Repositories;

import com.example.testingappproject.data.PointDao;
import com.example.testingappproject.model.Point;

import java.util.List;

public class PointsRepository {
    private PointDao pointDao;

    public PointsRepository(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public Point getPoint(long tracker_id, long date_id){
        return pointDao.getPoint(tracker_id, date_id);
    }

    public List<Point> getAllPoints(){
        return pointDao.getAllPoints();
    }

    public void insertPoint(Point point){
        pointDao.insertPoint(point);
    }

    public void updatePoint(Point point){
        pointDao.updatePoint(point);
    }
}
