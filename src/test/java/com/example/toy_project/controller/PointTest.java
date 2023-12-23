package com.example.toy_project.controller;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.example.toy_project.entity.PointEntity;
import com.example.toy_project.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PointTest {

    private final PointRepository pointRepository;

    @Test
    public void shouldConvertWktToGeometry() throws ParseException {
        Geometry geometry = wktToGeometry("POINT (2 5)");

        assertEquals("Point", geometry.getGeometryType());
        assertInstanceOf(Point.class, geometry);
    }

    @Test
    public void test() throws ParseException {

        PointEntity entity = new PointEntity();
        entity.setPoint((Point) wktToGeometry("POINT (2 5)"));
        System.out.println(pointRepository.save(entity));

        assertThat(pointRepository.findAll()).isNotEmpty();
    }

    public PointEntity insertPoint(String point) throws ParseException {
        PointEntity entity = new PointEntity();
        entity.setPoint((Point) wktToGeometry(point));
        return pointRepository.save(entity);
    }

    public Geometry wktToGeometry(String wellKnownText)
            throws ParseException {
        return new WKTReader().read(wellKnownText);
    }
}
