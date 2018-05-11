# Vaatimusmäärittely

## Sovelluksen tarkoitus

Kyseessä on yksinkertainen vuoropohjainen roguelike-peli, jossa pelaaja yrittää päästä mahdollisimma syvälle päättymättömässä luolastossa. Pelin on tarkoitus olla vaikea ja satunnaisesti generoitu; kuolemat ovat yleisiä ja joka pelisessio erilainen. Peli toteutetaan englanniksi Javalla.

## Perustoiminnallisuudet

* Näkymät
	* Main menu, josta voi aloittaa uuden pelin tai tarkastella aikaisempien pelien tuloksia.
	* Ennen pelin alkua hahmon nimen asetus ja aloitusstatsien valinta
	* Perusnäkymässä näkyvissä tämänhetkisen kerroksen kartta ja sillä olevat viholliset, tavarat jne
		* Esitetty yksinkertaisesti eri värisinä neliöinä.
		* Kartta on satunnaisesti generoitu.
		* Ruudun ylälaidassa tietoa pelaajan statuksesta, esim HP
		* Alalaidassa loki, jossa tietoa siitä, mitä viimeksi tapahtunut, esim kuka löi ketä tehden kuinka paljon vahinkoa
	* Erillinen näkymä pelaajan statsien ja inventoryn tarkasteluun. Lisäksi näkymät itemien pois heittämiseen, loitsujen trkasteluun ja lokihistorialle. Näihin pääsee menu-napin kautta.
* Pelin kulku
	* Vuorollaan pelaaja voi liikkua yhden askeleen tai hyökätä viereiseen viholliseen tai käyttää yhden itemin tai loitsun
		Liikkuminen ja hyökkääminen tapahtuu näppäimistöltä
	* Pelaajan vuoron jälkeen kaikki kerroksen viholliset saavat vuoron
	* Viholliset ilmestyvät kerrokseen satunnaisesti tietyin väliajoin
	* Pelaaja näkee kerrallaan viholliset vain siitä huoneesta, missä on
		* Jo käydyt huoneet näkyvät, mutta ei niiden sisältöä
	* Seuraavaan kerrokseen pääsee portatita pitkin, ne ovat jossain satunnaisessa huoneessa
	* Maasta voi löytyä tavaroita
		* Tärkeimpinä aseet ja panssarit
		* Pelaaja voi kantaa kerrallaan vain tietyn määrän tavaroita (tällä hetkellä rajoitus on 20).
	* Pelaajalla ja vihollisilla 4 stattia: Str, Con, Int ja Dex
		* Vihollisia tappamalla saa Exp, jonka kertyessä saa leveleitä, jollin jotain stattia saa kasvattaa
		* Statit vaikuttavat mm tehtyyn vahinkoon ja osumis- ja väistämistodennäköisyyteen
	* Peli päättyy, kun pelaajan HP tippuu nollaan
		* Pelin päätyttyä peli tallentaa missä, milloin ja miten pelaaja kuoli.
* Tarkempaa tietoa käyttöliittymästä ja grafiikoista: [käyttöohje](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/k%C3%A4ytt%C3%B6ohje.md)


## Laajennusideoita

* Enemmän vihollisia, itemeitä ja loitsuja
* Parempi balancaus
* Eri skillejä eri aselajeille
* Aseille, panssareille ja loitsuille stat-vaatimuksia
* Erikoisobjekteja kartalle, kuten kiviä, puskiä, lätäköitä, joilla omanlaisia vaikutuksia taisteluun
* Statseilla enemmän vaikutusta peliin, esim korkealla Str voi rikkoa seiniä, korkea Int näkee enemmän tietoa vihollisesta
* Boss-taisteluita pelin loppuun ja tärkeimpiin välietappeihin
* Satunnaisesti ilmestyvä kauppa ja rahaa
* Score-listan siirtäminen internetiin
* Kerrosten karttojen generoinnin kehittely, tällä hetkellä esimerkiksi vierekkäiset käytävät ovat turhan yleisiä
* Vihollisten AI:n parantelu
* Lore-tekstejä, joita pelaaja voi löytää ja lueskella


