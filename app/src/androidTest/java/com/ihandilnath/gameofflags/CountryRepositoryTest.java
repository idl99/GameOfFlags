package com.ihandilnath.gameofflags;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CountryRepositoryTest {

    @Test
    public void testIfAllCountriesLoaded() throws IOException, JSONException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        CountryRepository countryRepository = CountryRepository.getInstance(appContext);
        assertEquals(countryRepository.getCountries().size(),256);
    }

}