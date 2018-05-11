# Käyttöohje

Lataa tiedosto Roguelike.jar

## HYVIN TÄRKEÄÄ – data-kansio

Pelin voi jar:in voi suorittaa ainoastaan, jos se suoritetaan samasta paikasta, missä sijaitsee [data-kansio](https://github.com/konstakallama/otm-harjoitustyo/tree/master/Roguelike/src/main/resources/data), joka sisältää tiedostot Items.txt, Enemies.txt, Spells.txt, EncounterProbabilities.txt js Scores.txt.


## Pelin käynnistäminen

Peli käynnistetään komennolla 

```
java -jar Roguelike.jar
```


Pelin käynnistyessä aukeaa main menu, josta pääsee uuden pelin aloittamisen lisäksi tarkastelemaan vanhojen pelien tuloksia. Uutta peliä aloittaessa aukeaa ensin character creation, jossa saat allokoida aloitusstatseja hahmollesi.

## Grafiikat

* Vihreä: Pelaaja
* Punainen: Vihollinen
* Sininen: Item
* Keltainen: Portaat
* Valkoinen: Pelaajan näköpiirissä oleva lattia
* Harmaa: Pelajaan näkysvistä ulkona oleva lattia; tässä saattaa olla vihollinen
* Musta: Seinä/Tuntematon
* Vihollisten eri väriset reunat kertovat eri asetyypeistä:
	* Punainen: Sword
	* Sininen: Lance
	* Vihreä: Axe
	* Pelissä on siis Fire Emblem -faneille tuttu weapon triangle, jossa Sword > Axe > Lance > Sword

## Controllit

* Liikkuminen nuolinäppäimillä tai WASD:illa
	* Hyökkäät vieressä olevaa vihollista vastaan liikkumalla sitä kohti
* Voit passata vuoron tekemättä mitään Z:llä ta Shiftillä
* Portaiden päällä seisoessasi voit siirtyä seuraavaan kerrokseen Enterillä tai E:llä
* Klikkaamalla vihollista saat siitä tarkempaa tietoa
* Menuja navigoidaan hiirellä
* Spelliä voi käyttää ruudun yläreunan napilla, kohde valitaan hiirellä
* Menuista voi poistua Escillä
* Itemin, jonka päällä seisoo voi poimia P:llä
* Pikanäppäimiä menuille:
	* Menu: M
	* Inventory: I tai Tab
	* Discard: Q