# Arkkitehtuurikuvaus

## Rakenne

Ohjelma noudattelee pääpiirteissään kuvan mukaista pakkausrakennetta:

![alt text](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/arkkitehtuuri.png)

Map-pakkauksen alla olevat pakkaukset tosin käyttävät toisiaan lisäksi myös "ristiin", esimerkikisi Playerin alla on Inventory, joka käyttää InventoryItmemeitä, joista joillain on Effectejä. Toisaalta Effectejä on myös Spelleillä, joista pelaajan käytettävissä olevista pitää kirjaa Playerin alainen PlayerStats. Lisäksi lähes kaikki loukat käyttävät jollain tavalla hyväkseen support-pakkauksen luokki esimerkiksi paikkojen ja suuntien kuvaukseen Location- ja Direction -luokkien avulla.

## Käyttöliittymä

Käyttöliittymä sisältää seuraavat perusnäkymät:

- Main menu, josta voi aloittaa uuden pelin tai siirtyä score-näkymään.
- Score-näkymä, jossa voi tarkastella aikaisempien pelien tuloksia.
- Pelin aikaiset näkymät:
	- Map, jossa ruudulla on kartta ja siinä näkyvät Pelaaja, viholliset, itemit jne. Tässä näkymässä pelaaja voi tehdä pelitilannetta edistäviä komentoja kuten liikkua.
	- Menu, josta on useita eri tietoa näyttäviä versioita. Map-näkymän komentoja ei voi menussa käyttää. Eri menu-näkymiä ovat:
		- Inventory, jossa pelaaja voi tarkastella itemeitään ja käyttää niitä.
		- Stats, jossa pleaaja näkee hahmonsa statsit, jotka vaikuttavat pelin kulkuun.
		- Spells, jossa pelaaja voi tarkastella oppimiansa loitsuja.
		- Discard, jossa pelaaja voi heittää pois tarpeettomia itemeitä.
		- Log history, jossa pelaaja voi lukea pelin kulkua kuvaavia log-viestejä. Näistä muutama viimeisin on aina ruudun alakulmassa, tässä näkymässä on huomattavasti enemmän.

Menun ja Mapin välillä voi siirtyä vapaasti kokska tahansa. Lisäksi on muutama erikoistilanteessa näytettävä näkymä:

- Character creation, jossa pelaaja allokoi hahmolleen aloitusstatseja ennen uuden pelin alkua.
- Level up, jossa pelaajan tulee valita stat, jota hän kasvattaa noustuaan uudelle tasolle.
- Game over, joka näyttää milloin, missä ja miten pelaaja kuoli. Tästä näkymästä ei enää pääse takaisin peliin.


## Sovelluslogiikka

Sovelluksessa ui vastaa käyttäjän komentojen tulkitsemisesta ja välittää ne GameManagerille, joka puolestaan kertoo alemmille, pelin eri toimijoita kuvaaville luokille, mitä niiden pitää tehdä. Tarvittaessa nämä luokat välittävät titoa takaisin ylöspäin erillisten Result-olioiden avulla, joista ui osaa lukea, mitä sen pitäisi köyttäjälle näyttää. Sekvenssikaavio eräästä mahdollisesta pelaajan vuorosta:

![alt text](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/sekvenssikaavio.png)

## Tiedostojen luku ja kirjoitus

Peli lukee vihollisten, itemeiden ja spellien tietoja tekstitiedostoista. Tämän lisäksi se tallentaa päättyneiden pelien tiedot omaan tiedostoonsa. Näitä tietoja voi tarkastella score-näkymässä.

Tämä toiminnallisuus on hyvin myöhään (ja hyvin kiireellä) toteutettu, eli sen testaus ja optimointi on jääny vähäiselle.

## Ohjelman rakenteeseen jääneet heikkoudet

Graafinen käyttöliittymä on toteutettu kokonaan Main-luokassa, joka onkin erittäin suuri ja vaikeasti luettavissa. Käyttöliittymä on muutenkin hyvin yksinkertainen eikä kovin visuaalisesti miellyttävä.

Pelissä oleva tallennus sekä tietojen luku kohdistuu tiedostoihin eikä tietokantoihin. Lisäksi peli vaatii nykymuodossaan näiden tiedostojen sijaitsevan samassa kansiossa kuin mistä pelin jar suoritetaan.