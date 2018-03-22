package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoAlussaOikein() {
        this.tarkistaSaldo(10);
    }
    
    @Test
    public void lataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(5);
        this.tarkistaSaldo(15);
    }
    
    private void tarkistaSaldo(int maara) {
        assertTrue(kortti.saldo() == maara);
    }
    
    @Test
    public void saldoVahanee() {
        kortti.otaRahaa(5);
        this.tarkistaSaldo(5);
    }
    
    @Test
    public void saldoEiMuutuJosEiRahaa() {
        kortti.otaRahaa(11);
        this.tarkistaSaldo(10);
    }
    
    @Test
    public void otaRahaaPalauttaaTrue() {
        assertTrue(kortti.otaRahaa(5));
    }
    
    @Test
    public void otaRahaaPalauttaaFalse() {
        assertFalse(kortti.otaRahaa(15));
    }
    
    @Test
    public void toStringToimii() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    
}
