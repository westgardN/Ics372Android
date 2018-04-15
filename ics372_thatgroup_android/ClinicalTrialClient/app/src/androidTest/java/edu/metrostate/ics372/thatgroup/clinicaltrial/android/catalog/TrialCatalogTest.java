package edu.metrostate.ics372.thatgroup.clinicaltrial.android.catalog;

import edu.metrostate.ics372.thatgroup.clinicaltrial.catalog.ClinicalTrialCatalogUtilIty;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrialCatalogTest {
    @Test
    public void isAndroidShouldReturnTrue() {
        org.junit.Assert.assertEquals(true, ClinicalTrialCatalogUtilIty.isAndroid());
    }
}
