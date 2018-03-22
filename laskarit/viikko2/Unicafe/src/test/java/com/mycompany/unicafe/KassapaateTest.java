/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author konstakallama
 */
public class KassapaateTest {
    private Kassapaate kassa;
    private Maksukortti kortti;
    
    public KassapaateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(1000);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void alussaOikeinRahaa() {
        assertTrue(kassa.kassassaRahaa() == 100000);
    }
    
    @Test
    public void alussaOikeinMyyty() {
        assertTrue(kassa.edullisiaLounaitaMyyty() == 0);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void edullisenMaksuToimii() {
        assertTrue(kassa.syoEdullisesti(500) == 260);
        assertTrue(kassa.kassassaRahaa() == 100240);
        assertTrue(kassa.edullisiaLounaitaMyyty() == 1);
    }
    
    @Test
    public void maukkaanMaksuToimii() {
        assertTrue(kassa.syoMaukkaasti(500) == 100);
        assertTrue(kassa.kassassaRahaa() == 100400);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 1);
    }
    
    @Test
    public void edullisenMaksuEiOnnistu() {
        assertTrue(kassa.syoEdullisesti(100) == 100);
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.edullisiaLounaitaMyyty() == 0);
    }
    
    @Test
    public void maukkaanMaksuEiOnnistu() {
        assertTrue(kassa.syoMaukkaasti(100) == 100);
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void edullisenMaksuKortillaOnnistuu() {
        assertTrue(kassa.syoEdullisesti(kortti));
        assertTrue(kortti.saldo() == 760);
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.edullisiaLounaitaMyyty() == 1);
    }
    
    @Test
    public void edullisenMaksuKortillaEiOnnistu() {
        kortti.otaRahaa(900);
        assertTrue(!kassa.syoEdullisesti(kortti));
        assertTrue(kortti.saldo() == 100);
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.edullisiaLounaitaMyyty() == 0);
    }
    
    @Test
    public void maukkaanMaksuKortillaOnnistuu() {
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertTrue(kortti.saldo() == 600);
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 1);
    }
    
    @Test
    public void maukkaanMaksuKortillaEiOnnistu() {
        kortti.otaRahaa(900);
        assertTrue(!kassa.syoMaukkaasti(kortti));
        assertTrue(kortti.saldo() == 100);
        assertTrue(kassa.kassassaRahaa() == 100000);
        assertTrue(kassa.maukkaitaLounaitaMyyty() == 0);
    }
    
    @Test
    public void kortilleVoiLadataRahaa() {
        kassa.lataaRahaaKortille(kortti, 100);
        assertTrue(kortti.saldo() == 1100);
        assertTrue(kassa.kassassaRahaa() == 100100);
    }
    
    @Test
    public void kortilleEiVoiLadataRahaa() {
        kassa.lataaRahaaKortille(kortti, -100);
        assertTrue(kortti.saldo() == 1000);
        assertTrue(kassa.kassassaRahaa() == 100000);
    }
}
