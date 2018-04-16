package edu.metrostate.ics372.thatgroup.clinicaltrial.android.catalog;

import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Clinic;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Patient;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.PatientStatus;
import edu.metrostate.ics372.thatgroup.clinicaltrial.beans.Trial;
import edu.metrostate.ics372.thatgroup.clinicaltrial.catalog.ClinicalTrialCatalogUtilIty;
import edu.metrostate.ics372.thatgroup.clinicaltrial.catalog.TrialCatalog;
import edu.metrostate.ics372.thatgroup.clinicaltrial.catalog.TrialManager;
import edu.metrostate.ics372.thatgroup.clinicaltrial.exceptions.TrialCatalogException;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrialCatalogTest {
    private static final String TEST_TRIAL_ID = "test";
    Trial trial;
    TrialCatalog catalog;
    TrialManager trialManager;


    @Before
    public void setupTestHarness() {
        try {
            trial = new Trial(TEST_TRIAL_ID);
            Context context = InstrumentationRegistry.getTargetContext();
            assertNotNull(context);
            trialManager = TrialManager.getInstance(context.getFilesDir().getPath());

            if (trialManager.exists(trial)) {
                boolean expected = true;
                boolean actual = trialManager.remove(trial);

                assertEquals(expected, actual);
            }

            catalog = trialManager.getTrialCatalog(trial);
        } catch (TrialCatalogException e) {
            e.printStackTrace();
            org.junit.Assert.fail("Unable to initialize the catalog");
        }
    }

    @After
    public void teardownTestHarness() {
        TrialManager.getInstance().uninitialize();
        catalog = null;
        trial = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertInvalidPatientShouldThrowException() throws TrialCatalogException {
        Patient patient = new Patient();

        if(!catalog.exists(patient)) {
            catalog.insert(patient);
        }
    }

    @Test
    public void testInsertClinic() throws TrialCatalogException {
        Clinic clinic = new Clinic("test", TEST_TRIAL_ID,"testing" );

        boolean expected = true;
        boolean actual = catalog.insert(clinic);

        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveClinic() throws TrialCatalogException {
        Clinic clinic = new Clinic("test", TEST_TRIAL_ID,"testing");

        testInsertClinic();
        boolean expected = true;
        boolean actual = catalog.exists(clinic);
        assertEquals(expected, actual);

        actual = catalog.remove(clinic);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateClinic() throws TrialCatalogException {
        Clinic clinic = new Clinic("test", TEST_TRIAL_ID,"updated testing");

        testInsertClinic();
        boolean expected = true;
        boolean actual = catalog.exists(clinic);
        assertEquals(expected, actual);

        actual = catalog.update(clinic);
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertPatient() throws TrialCatalogException {
        Patient patient = new Patient("test", TEST_TRIAL_ID, LocalDate.of(2007, 1, 18), LocalDate.of(2018, 3, 18), PatientStatus.COMPLETED_ID );

        boolean expected = true;
        boolean actual = catalog.insert(patient);

        assertEquals(expected, actual);
    }

    @Test
    public void testRemovePatient() throws TrialCatalogException {
        Patient patient = new Patient("test", TEST_TRIAL_ID, null, null);

        testInsertPatient();
        boolean expected = true;
        boolean actual = catalog.exists(patient);
        assertEquals(expected, actual);

        actual = catalog.remove(patient);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdatePatient() throws TrialCatalogException {
        Patient patient = new Patient("test", TEST_TRIAL_ID, LocalDate.of(2007, 1, 18), LocalDate.of(2018, 3, 18), PatientStatus.FAILED_ID);

        testInsertPatient();
        boolean expected = true;
        boolean actual = catalog.exists(patient);
        assertEquals(expected, actual);

        String oldPatientStatusId = catalog.get(patient).getStatusId();
        assertEquals(PatientStatus.COMPLETED_ID, oldPatientStatusId);

        actual = catalog.update(patient);
        assertEquals(expected, actual);

        oldPatientStatusId = catalog.get(patient).getStatusId();
        assertEquals(PatientStatus.FAILED_ID, oldPatientStatusId);

    }

    @Test
    public void isAndroidShouldReturnTrue() {
        assertEquals(true, ClinicalTrialCatalogUtilIty.isAndroid());
    }

    @Test
    public void trialCatalogShouldBeValid() {
        if (!catalog.isInit()) {
            fail("The catalog should already be initialized!");
        }

        assertNotNull(trial);
        assertNotNull(catalog);
    }
}
