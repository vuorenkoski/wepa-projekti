# wepa-projekti
Web-palvelinohjelmointi Java, 2021 -projektityö (Lauri Vuorenkoski)

Toteutuskielet: Java ja Javascript (Spring framework, Thymeleaf, Bootstrap). Data liikkuu selaimen ja palvelimen välillä restin läpi, lukuunottamatta kirjautumista ja profiilin tietoja. Pääosin single-page application.

[Tehtäväksianto](https://web-palvelinohjelmointi-21.mooc.fi/projekti)

## Heroku

[Sovellus herokussa](https://hidden-tundra-44605.herokuapp.com/)

## Issues

- selenium ei tunnista javascriptin metodia document.prepend. Vaihtaminen chrome-driveriin auttaisi? Selenium tulostaa virheilmoituksia mutta testit menevät kyllä läpi

## Test and run

Testing
```
mvn test
```

Running
```
mvn compile exec:java -Dserver.port=8080 -Dexec.mainClass=projekti.MyApplication
```

## Tietokantarakenne

![Tietokantarakenne](db_structure.png)

## Rajapinnan tiivis kuvaus

### Data liikkuu json muodossa

#### POST: "/api/messages"

Uuden viestin lähettäminen. payload: {message: "viesti"}

#### GET: "/api/messages"
Viestien hakeminen. Palauttaa kaikki käyttäjän ja seurattavien viestit.

#### POST: "/api/messages/{id}/comments"
Kommentin lähettäminen. id=viestin id. payload: {comment: "kommentti"}

#### POST: "/api/messages/{id}/likes"
Tykkäyksen lähettäminen. id=viestin id.

#### GET: "/api/photos"
Kuvien tietojen hakeminen. Palauttaa kaikki käyttäjän ja seurattavien kuvat.

#### GET: "/api/photos/{id}"
Yksittäinen kuva jpg muodossa.

#### DELETE: "/api/photos/{id}"
Kuvan poistaminen. Id=kuvan id.

#### POST: "/api/photos/{id}/comments"
Kommentin lähettäminen. id=kuvan id. Payload: {comment: "kommentti"}

#### POST: "/api/photos/{id}/likes"
Tykkäyksen lähettäminen. id=kuvan id.

#### POST: "/api/follow/{id}"
Käyttäjän seuraamisen aloittaminen. id=seurattavan profiilin id.

#### GET: "/api/follow"
Haet profiilit joita käyttäjä seuraa.

#### DELETE: "/api/follow/{id}"
Poista toisen käyttäjä profiilin seuraaminen. id=seuraamisen id

#### GET: "/api/followers"
Hae profiilit jotka seuraavat käyttäjää

#### POST: "/api/follower/{id}/hide"
Piilota oman profiilin näkyminen seuraajalta. id=seuraamisen id

#### POST: "/api/follower/{id}/unhide"
Palauta oman profiilin näkyminen seuraajalta. id=seuraamisen id

#### GET: "/api/profiles?name={merkkijono}"
Käyttäjäprofiilien haku. Get parametrina name voi antaa hakumerkkijonon.

#### POST: "/profile/photo?id={id}"
Kuva asetetaan propfiilikuvaksi. id=kuvan id.

### Data lähetetään html formilla

#### POST: "/api/photos"
Kuvan lähettäminen. post-parametrit image ja description. multipart/form-data 

#### POST: "/signup"
rekisteröityminen. post-parametrit username ja password.

#### POST: "/profile"
Profiilin muokkaaminen. post-parametrit fullname ja profilename.

#### POST: "login"
Kirjautuminen. post-parametrit username ja password.
