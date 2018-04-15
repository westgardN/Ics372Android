package edu.metrostate.ics372.thatgroup.clinicaltrial.android.catalog;

import edu.metrostate.ics372.thatgroup.clinicaltrial.android.ClinicalTrialClient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Trial;
import edu.metrostate.ics372.thatgroup.clinicaltrial.catalog.ClinicalTrialCatalogUtilIty;
import edu.metrostate.ics372.thatgroup.clinicaltrial.catalog.TrialCatalog;
import edu.metrostate.ics372.thatgroup.clinicaltrial.catalog.TrialManager;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrialCatalogTest {
    @Test
    public void isAndroidShouldReturnTrue() {
        assertEquals(true, ClinicalTrialCatalogUtilIty.isAndroid());
    }

    @Test
    public void trialCatalogShouldBeValid() {
        final String DEFAULT_TRIAL_ID = "test";
        try {
            Trial trial = new Trial(DEFAULT_TRIAL_ID);
            Context context = InstrumentationRegistry.getTargetContext();
            assertNotNull(context);
            TrialManager tm = TrialManager.getInstance(context.getFilesDir().getPath());

            TrialCatalog catalog = tm.getTrialCatalog(trial);

            if (!catalog.isInit()) {
                assertEquals(true, catalog.init());
            }

            assertNotNull(catalog);
        } catch (TrialCatalogException e) {
            e.printStackTrace();
            org.junit.Assert.fail("Unable to initialize the catalog");
        }
    }
}
