# Prueba PetStore API con SwaggerParser
La ATM de este repositorio se encuentra todavia en proceso de desarrollo. 
Pretendo realizar una prueba dinamica, a la API PetStore, utilizando la especificacion del Swagger (archivo json) de la siguiente manera:
Se le indica al robot cual es el servicio (Path) y metodo (Get, Post, Put...) que se va a probar. Esta informacion la recibe el robot desde un archivo de google sheet el cual tiene una url publica.
Con esta informacion y swagger Parser, el robot podra extraer los aspectos del servicio (parametros, body, etc) y con ellos construir la peticion. 
El caso de prueba esta expresado en Gherkin y la ATM se esta desarrollando en Java con los siguientes frameworks

* Cucumber
* Serenity Bdd
* restAssured
* SwaggerParser
* JUnit

## 📋 Requisitos
* Java 17
* Gradle 9
