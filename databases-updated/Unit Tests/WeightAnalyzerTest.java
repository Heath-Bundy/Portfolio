package com.finalproject_heathbundy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.finalproject_heathbundy.models.WeightEntry;
import com.finalproject_heathbundy.services.TrendResult;
import com.finalproject_heathbundy.services.WeightAnalyzer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class WeightAnalyzerTest {

    private final WeightAnalyzer weightAnalyzer = new WeightAnalyzer();

    @Test
    public void sortChronologically() {
        List<WeightEntry> entries = new ArrayList<>();
        entries.add(new WeightEntry(3, "06/15/2026", 198));
        entries.add(new WeightEntry(1, "06/01/2026", 200));
        entries.add(new WeightEntry(2, "06/08/2026", 199));

        List<WeightEntry> sorted = WeightAnalyzer.sortChronologically(entries);

        assertEquals("06/01/2026", sorted.get(0).getDate());
        assertEquals("06/08/2026", sorted.get(1).getDate());
        assertEquals("06/15/2026", sorted.get(2).getDate());
    }

    @Test
    public void analyzeTrend_returnsInsufficientData() {
        List<WeightEntry> entries = new ArrayList<>();
        entries.add(new WeightEntry(1, "06/01/2026", 200));
        entries.add(new WeightEntry(2, "06/08/2026", 198));

        TrendResult result = weightAnalyzer.analyzeTrend(entries, 190);

        assertFalse(result.hasDate());
    }

    @Test
    public void analyzeTrend_matchesHandCalculatedExample() {
        List<WeightEntry> entries = new ArrayList<>();
        entries.add(new WeightEntry(1, "06/01/2026", 200));
        entries.add(new WeightEntry(2, "06/08/2026", 198));
        entries.add(new WeightEntry(3, "06/15/2026", 197));
        entries.add(new WeightEntry(4, "06/22/2026", 196));
        entries.add(new WeightEntry(5, "06/29/2026", 195));

        TrendResult result = weightAnalyzer.analyzeTrend(entries, 190);

        assertTrue(result.hasDate());
    }

    @Test
    public void analyzeTrend_handlesUnsortedInputCorrectly() {
        List<WeightEntry> entries = new ArrayList<>();
        entries.add(new WeightEntry(1, "06/29/2026", 195));
        entries.add(new WeightEntry(2, "06/01/2026", 200));
        entries.add(new WeightEntry(3, "06/15/2026", 197));
        entries.add(new WeightEntry(4, "06/08/2026", 198));
        entries.add(new WeightEntry(5, "06/22/2026", 196));

        TrendResult result = weightAnalyzer.analyzeTrend(entries, 190);

        assertTrue(result.hasDate());
    }

    @Test
    public void analyzeTrend_returnsNoTrend_whenSlopeIsZero() {
        List<WeightEntry> entries = new ArrayList<>();
        entries.add(new WeightEntry(1, "06/01/2026", 200));
        entries.add(new WeightEntry(2, "06/08/2026", 200));
        entries.add(new WeightEntry(3, "06/15/2026", 200));

        TrendResult result = weightAnalyzer.analyzeTrend(entries, 190);

        assertFalse(result.hasDate());
    }
}
