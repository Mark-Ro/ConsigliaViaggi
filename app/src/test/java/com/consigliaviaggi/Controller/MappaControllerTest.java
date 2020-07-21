package com.consigliaviaggi.Controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MappaControllerTest {

    private MappaController mappaController;

    @Before
    public void setUp() {
        mappaController = new MappaController();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testArrotondaValutazioneMinimumMinus() {
        float inputValue = -0.000001f;
        mappaController.arrotondaValutazione(inputValue);
    }

    @Test
    public void testArrotondaValutazioneMinimum() {
        float inputValue = 0.0f,expected=0.0f,output;
        output = mappaController.arrotondaValutazione(inputValue);
        assertEquals(expected,output,0.1f);
    }

    @Test
    public void testArrotondaValutazioneMinimumPlus() {
        float inputValue=0.000001f,expected=0.0f,output;
        output = mappaController.arrotondaValutazione(inputValue);
        assertEquals(expected,output,0.1f);
    }

    @Test
    public void testArrotondaValutazioneAverageValue() {
        float inputValue = 2.555555f,expected = 2.5f,output;
        output = mappaController.arrotondaValutazione(inputValue);
        assertEquals(expected,output,0.1f);
    }

    @Test
    public void testArrotondaValutazioneMaximumMinus() {
        float inputValue = 4.999999f,expected = 4.9f,output;
        output = mappaController.arrotondaValutazione(inputValue);
        assertEquals(expected,output,0.1f);
    }

    @Test
    public void testArrotondaValutazioneMaximum() {
        float inputValue = 5.0f,expected = 5.0f,output;
        output = mappaController.arrotondaValutazione(inputValue);
        assertEquals(expected,output,0.1f);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testArrotondaValutazioneMaximumPlus() {
        float inputValue = 5.000001f,output;
        mappaController.arrotondaValutazione(inputValue);
    }

    //Branch coverage
    @Test (expected = IllegalArgumentException.class)
    public void testArrotondaValutazionePath_2_3() {
        float inputValue=-10f;
        mappaController.arrotondaValutazione(inputValue);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testArrotondaValutazionePath_2_4_5() {
        float inputValue = 10f;
        mappaController.arrotondaValutazione(inputValue);
    }

    @Test
    public void testArrotondaValutazionePath_2_4_6_7_8() {
        float inputValue = 2.345f,expected=2.0f,output;
        output = mappaController.arrotondaValutazione(inputValue);
        assertEquals(expected,output,0.1f);
    }

    @Test
    public void testArrotondaValutazionePath_2_4_6_7_9_10() {
        float inputValue = 2.678f,expected=3.0f,output;
        output = mappaController.arrotondaValutazione(inputValue);
        assertEquals(expected,output,0.1f);
    }

    @Test
    public void testArrotondaValutazionePath_2_4_6_7_9_11() {
        float inputValue = 2.578f,expected=2.5f,output;
        output = mappaController.arrotondaValutazione(inputValue);
        assertEquals(expected,output,0.1f);
    }
}