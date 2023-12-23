package com.example.toy_project.controller;

import com.example.toy_project.entity.PointEntity;
import com.example.toy_project.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointRepository pointRepository;

    @GetMapping("/point")
    public ResponseEntity<Void> insertPoint() throws ParseException {
        PointEntity entity = new PointEntity();
//        entity.setPoint((Point) wktToGeometry("POINT( 2 5)"));
        entity.setPoint(convertToPoint(2.0, 5.0));
        System.out.println(pointRepository.save(entity));
        return ResponseEntity.ok().build();
    }

    public Geometry wktToGeometry(String wellKnownText)
            throws ParseException {
        return new WKTReader().read(wellKnownText);
    }

    public static Point convertToPoint(Double lat, Double lon) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(lat, lon);

        return geometryFactory.createPoint(coordinate);
    }
}
