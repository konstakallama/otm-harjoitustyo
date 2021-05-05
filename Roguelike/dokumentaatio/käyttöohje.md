# Käyttöohje

Lataa tiedosto Roguelike.jar


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

## Tallentaminen

Jar-formaatin rajoituksista ja tekijän laiskuudeta johtuen pelaajan pisteet tallentuvat pysyvästi ainoastaan, jos pelin kanssa samaan kansioon luo etukäteen tyhjän "scores.txt"-tiedoston. Onnistuu esimerkiksi notepadillä.
