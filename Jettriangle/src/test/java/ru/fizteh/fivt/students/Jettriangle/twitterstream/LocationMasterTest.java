package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 19.12.15.
 */
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationMasterTest {
    @Test
    public void testGetLocation() throws Exception {
        String place = "Moscow";
        double eps = 1e-5;
        Location location = LocationBuilder.getLocation(place);
        assertEquals(location.getLatitudeCenter(), 55.75396, eps);
        assertEquals(location.getLongitudeCenter(), 37.620393, eps);
    }
}
