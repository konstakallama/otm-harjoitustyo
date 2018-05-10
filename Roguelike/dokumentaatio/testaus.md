# Testausdokumentti

Peliä on testattu sekä automatisoiduin yksikkö- ja integraatiotestein JUnitilla että manuaalisella pelitestaamisella.

Eri luokkia testataan erillisillä testiluokilla PlayerTests, MapTests, ItemTests, EnemyTests ja EffectTests. Testit tosin tulevat välttämättä kutsuneeksi muidenkin luokkien metodeja, koska monet testattavista asioista koskevat kahden eri luokan vuorovaikutusta. Lisäksi testeissä käytetään jonkin verran "realistisia" peliskenaarioita, joissa on monta luokkaa toiminnassa yhtä aikaan. Tämän vuoksi testien rivikattavuus on korkea, vaikka testejä ei olekaan kovin paljoa. Graafista käyttöliittymää ei ole testattu JUnit-testeillä.


### Testauskattavuus

Käyttöliittymäkerrosta lukuunottamatta sovelluksen testauksen rivikattavuus on 79% ja haarautumakattavuus 75%.

Aivan kaikkea ei ole testattu, ainakin getterit ja setterit sekä osa MapGeneratorin sisäisesti käyttämistä metodeista ja tietorakenteista on testaamatta.

## Järjestelmätestaus

Sovelluksen järjestelmätestaus on suoritettu manuaalisesti.

### Asennus ja kanfigurointi

Sovellus on haettu ja sitä on testattu [käyttöohjeen](https://github.com/mluukkai/OtmTodoApp/blob/master/dokumentaatio/kayttoohje.md) kuvaamalla tavalla sekä OSX- että Windows-ympäristössä.

### Toiminnallisuudet

Kaikki [määrittelydokumentin](https://github.com/konstakallama/otm-harjoitustyo/blob/master/Roguelike/dokumentaatio/Vaatimusm%C3%A4%C3%A4rittely.md) ja käyttöohjeen listaamat toiminnallisuudet on käyty läpi.

## Sovellukseen jääneet laatuongelmat

Jotkin MapGeneratorin tuottamista mapeista ovat aika rumia ainakin suurilla huonemäärillä. Mappeihin tule myös turhan leveitä käytäviä sekä huoneiden reunoja pitkiin kulkevia käytäviä, joissa pelaajan näkökentän toiminta on hieman epäintuitiivista.