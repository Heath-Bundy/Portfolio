package com.finalproject_heathbundy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.finalproject_heathbundy.services.WeightEntryService;

public class WeightEntryTest {

    private final WeightEntryService weightEntryService = new WeightEntryService();

    //date validation tests
    @Test
    public void dateValidation_empty(){
        assertNotNull(weightEntryService.dateValidation(""));
    }

    @Test
    public void dateValidation_invalidFormat(){
        assertNotNull(weightEntryService.dateValidation("13/72/2006"));
    }

    @Test
    public void dateValidation_futureDate(){
        assertNotNull(weightEntryService.dateValidation("01/01/3000"));
    }

    @Test
    public void dateValidation_validDate(){
        assertNull(weightEntryService.dateValidation("07/25/2026"));
    }

    //weight validation tests
    @Test
    public void weightValidation_empty(){
        assertNotNull(weightEntryService.weightValidation(""));
    }

    @Test
    public void weightValidation_mustBeNumerical(){
        assertNotNull(weightEntryService.weightValidation("12.9q"));
    }

    @Test
    public void weightValidation_tooLow(){
        assertNotNull(weightEntryService.weightValidation("49"));
    }

    @Test
    public void weightValidation_exactly50(){
        assertNull(weightEntryService.weightValidation("50"));
    }

    @Test
    public void weightValidation_tooHigh(){
        assertNotNull(weightEntryService.weightValidation("1501"));
    }

    @Test
    public void weightValidation_exactly1500(){
        assertNull(weightEntryService.weightValidation("1500"));
    }

    @Test
    public void weightValidation_validEntry(){
        assertNull(weightEntryService.weightValidation("200"));
    }
}
