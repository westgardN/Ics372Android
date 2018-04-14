package edu.metrostate.ics372.thatgroup.clinicaltrial.android.catalog;

import org.junit.Test;

import edu.metrostate.ics372.thatgroup.clinicaltrial.catalog.ClinicalTrialCatalogUtilIty;

import static junit.framework.Assert.assertEquals;

public class TrialCatalogTest {
    @Test
    public void isAndroidShouldReturnTrue() {
        boolean expected = true;
        boolean actual = ClinicalTrialCatalogUtilIty.isAndroid();

        assertEquals(expected, actual);
    }
}
