package com.example.testingappproject.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.testingappproject.model.TrackerDatePoint;

import java.util.List;

@Dao
public interface TrackerDatePointDao {

    @Query("SELECT id FROM points " +
            "WHERE tracker_id LIKE :tracker_id AND date_id LIKE :date_id")
    long getPointId(long tracker_id, long date_id);

    // хотим получить информацию по абсолютно всем трекерам, но только за последнюю дату
    // 1) доставать специально для каждого трекера, но как потом объедеинить полученное в live data
    // 2) получать все сразу: по всей таблице трекеров и по одной дате: функция есть ли элемент в массиве?
    // 3) отдельно получить все Point_ы, потом беря у них id, прицеплять нужный трекер
//    @Query("SELECT points.date_id, points.tracker_id, headline, max_points, img_res, points FROM trackers, points WHERE points.tracker_id LIKE :tracker_id AND points.date_id LiKE :last_date_id")
//    LiveData<List<TrackerDatePoint>> getTrackerPoint(long last_date_id, long tracker_id);

    @Query("SELECT points.date_id, points.tracker_id, headline, max_points, img_res, points FROM trackers, points" +
            " WHERE points.tracker_id LIKE :tracker_id AND points.date_id LiKE :last_date_id")
    TrackerDatePoint getTrackerPoint(long last_date_id, long tracker_id);

//    @Query("SELECT * from points WHERE points.date_id LIKE :tracker_id JOIN trackers ON trackers.id = points.tracker_id")
//    @Query("select * from trackers, points FULL OUTER JOIN Customers ON Orders.CustomerID = Customers.CustomerID WHERE Customers.CustomerID >10")
    //  SELECT Parts.Part, Categories.Catnumb AS Cat, Categories.Price
        //      FROM Parts FULL OUTER JOIN Categories
        //	     ON Parts.Cat = Categories.Catnumb

    // SELECT employee.*, department.*
        //FROM   employee
        //       LEFT JOIN department
        //          ON employee.DepartmentID = department.DepartmentID
        //UNION ALL
        //SELECT employee.*, department.*
        //FROM   department
        //       LEFT JOIN employee
        //          ON employee.DepartmentID = department.DepartmentID
        //WHERE  employee.DepartmentID IS NULL


//    @Query("SELECT trackers.*, points.* " +
//            "FROM trackers " +
//            "LEFT JOIN points " +
//            "ON trackers.id = points.tracker_id " +
//            "UNION ALL " +
//            "SELECT trackers.*, points.* FROM points " +
//            "LEFT JOIN trackers " +
//            "ON points.date_id = :last_date_id")

//    @Query("SELECT * " +
//            "FROM trackers " +
//            "LEFT JOIN points " +
//            "ON trackers.id = points.tracker_id " +
//            "UNION " +
//            "SELECT * FROM points " +
//            "LEFT JOIN trackers " +
//            "ON points.date_id = :last_date_id")
    @Query("SELECT * " +
            "FROM trackers LEFT JOIN points " +
            "ON trackers.id = points.tracker_id WHERE points.date_id = :last_date_id")
    LiveData<List<TrackerDatePoint>> getTrackerPoints(long last_date_id);

    @Query("SELECT * " +
            "FROM trackers LEFT JOIN points " +
            "ON trackers.id = points.tracker_id WHERE points.date_id = :last_date_id")
    List<TrackerDatePoint> testingList(long last_date_id);
}
