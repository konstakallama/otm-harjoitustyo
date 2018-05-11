# Roguelike

Kurssilla Ohjelmistotekniikan menetelmät toteutettava ohjelmointiprojekti. Kyseessä on yksinkertainen roguelike-tyylinen peli, joka on siis vuonna 1980 julkaistun [rogue-pelin](https://en.wikipedia.org/wiki/Rogue_%28video_game%29) innoittama.


## Dokumentaatio

[Vaatimusmäärittely](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/Vaatimusm%C3%A4%C3%A4rittely.md)

[Työaikakirjanpito](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/Ty%C3%B6aikakirjanpito.md)

[Arkkitehtuuri](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/arkkitehtuuri.md)

[Käyttöohje](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/k%C3%A4ytt%C3%B6ohje.md)

[Testaus](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/testaus.md)

## Releaset

[Viikko 5](https://github.com/konstakallama/otm-harjoitustyo/releases/tag/v0.1-alpha)

[Viikko 6](https://github.com/konstakallama/otm-harjoitustyo/releases/tag/v0.2-alpha)

[Viikko 7 (Loppupalautus)](https://github.com/konstakallama/otm-harjoitustyo/releases/tag/v1.0)


## Komentorivitoiminnot

### Testaus

Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_

### Suoritettavan jarin generointi

Komento

```
mvn package
```

generoi hakemistoon _target_ suoritettavan jar-tiedoston _Roguelike-1.0-SNAPSHOT.jar_

## HYVIN TÄRKEÄÄ – data-kansio

Pelin jar:in voi suorittaa ainoastaan, jos se suoritetaan samasta kansiosta, jossa sijaitsee [data-kansio](https://github.com/konstakallama/otm-harjoitustyo/tree/master/Roguelike/src/main/resources/data), joka sisältää tiedostot Items.txt, Enemies.txt, Spells.txt, EncounterProbabilities.txt js Scores.txt.

Tämä on toisaalta erittäin kömpelöä ja epätoivottavaa, mutta ainakin pelin moddaaminen onnistuu nyt näiden parametrien osalta erittäin helposti.

### JavaDoc

JavaDoc generoidaan komennolla

```
mvn javadoc:javadoc
```

JavaDocia voi tarkastella avaamalla selaimella tiedosto _target/site/apidocs/index.html_

### Checkstyle

Tiedostoon [checkstyle.xml](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla

```
 mvn jxr:jxr checkstyle:checkstyle
```

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto _target/site/checkstyle.html_