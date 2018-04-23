# Vaatimusmäärittely

## Sovelluksen tarkoitus

Kyseessä on yksinkertainen vuoropohjainen roguelike-peli, jossa pelaaja yrittää päästä n-kerroksisen loulaston pohjalle. Pelin on tarkoitus olla vaikea ja satunnaisesti generoitu; kuolemat ovat yleisiä ja joka pelisessio erilainen. Peli toteutetaan englanniksi Javalla.

## Perustoiminnallisuudet

* Näkymät
	* Ennen pelin alkua hahmon nimen asetus ja mahdollisesti aloitusstatsien valinta
	* Perusnäkymässä näkyvissä tämänhetkisen kerroksen kartta ja sillä olevat viholliset, tavarat jne
		* Todennäköisesti ASCII-grafiikoilla tms esitettynä
		* Kartan olisi tarkoitus olla satunnaisesti generoitu, mutta alkuvaiheessa voitaisiin tyytyä valmiisiin karttoihin
		* Ruudun ylälaidassa tietoa pelaajan statuksesta, esim HP
		* Alalaidassa loki, jossa tietoa siitä, mitä viimeksi tapahtunut, esim kuka löi ketä tehden kuinka paljon vahinkoa
	* Erillinen näkymä pelaajan statsien ja inventoryn tarkasteluun
* Pelin kulku
	* Vuorollaan pelaaja voi liikkua yhden askeleen tai hyökätä viereiseen viholliseen tai käyttää yhden skillin/loitsun
		Liikkuminen ja hyökkääminen tapahtuu näppäimistöltä
	* Pelaajan vuoron jälkeen kaikki kerroksen viholliset saavat vuoron
	* Viholliset ilmestyvät kerrokseen satunnaisesti tietyin väliajoin
	* Pelaaja näkee kerrallaan viholliset vain siitä huoneesta, missä on
		* Jo käydyt huoneet näkyvät, mutta ei niiden sisältöä
	* Seuraavaan kerrokseen pääsee portatita pitkin, ne ovat jossain satunnaisessa huoneessa
	* Maatsta tai kuolleitten vihollisten jäljiltä voi löytyä tavaroita
		* Tärkeimpinä aseet ja panssarit
		* Pelaaja voi kantaa kerrallaan vain tietyn määrän tavaroita
	* Pelaajalla on alustavien suunnitelmien mukaan 4 stattia: Str, Con, Int ja Dex
		* Vihollisia tappamalla saa Exp, jonka kertyessä saa leveleitä, jollin jotain stattia saa kasvattaa
		* Statit vaikuttavat mm tehtyyn vahinkoon ja osumis- ja väistämistodennäköisyyteen
	* Peli päättyy, kun pelaajan HP tippuu nollaan tai hän saavuttaa luolaston lopun
		* Peli tallentaa itsensä joka liikkeen jälkeen eli uudestaan ei voi yrittää, kuollessaan on aloitettava alusta
		
		
## Laajennusideoita

* Eri skillejä ja magiaa, joilla perusnäkymässä omat kuvakkeet
	* Operoivat cooldownilla, ei erikseen manaa tjv
	* N paikkaa loitsuille, kasvaa Int:in myötä
* Käyttöliittymäparannuksia, kuten lisätietoa kartan objektista kuten vihollisesta mouseoverilla/klikillä
	* Liikkumien ja hyökkääminen hiirellä, skillien/loitsujen käyttö kuvakkeita klikkaamalla
		* AoE-skilleissä vaikutusalueen ilmaiseminen esim ruutuja värittämällä mouseoverilla
	* Mahdollisuus lukea loki pelin alusta lähtien
* Useita asettyyppejä joilla omat skillit ja heikkouksia/vahvuuksia toisiaan vastaan
* Stamina-stat, joka kuluu hieman joka vuoro ja palautuu ruokaa syömällä
	* Staminan loppuessa alkaa pelaajan hahmo nääntyä nälkään
* Stamina ja pelaajan inventoryn koko riippumaan statseista, todnäk Con
* Aseille, panssareille ja loitsuille stat-vaatimuksia
* Erikoisobjekteja kartalle, kuten kiviä, puskiä, lätäköitä, joilla omanlaisia vaikutuksia taisteluun
* Statseilla enemmän vaikutusta peliin, esim korkealla Str voi rikkoa seiniä, korkea Int näkee enemmän tietoa vihollisesta
* Boss-taisteluita pelin loppuun ja tärkeimpiin välietappeihin
* Satunnaisesti ilmestyvä kauppa ja rahaa
* Score-lista, joka voisi olla internetissä, eli kaikki peliä pelanneet saavat tuloksesna listalle
* Kerrosten karttojen generoinnin kehittely
* Vihollisten AI:n parantelu
* Survival mode, jossa luolasto on päättymätön
* Lore-tekstejä, joita pelaaja voi löytää ja lueskella
* Musiikkia, todnäk CC-lisensoitua netistä


