Feature: Consulta el servicio de Pet

Scenario: Consulta todas las mascotas deacuerdo a un estado especifico
Given que se tiene acceso a la especificacion Swagger y los datos del caso
When Se envian los parametros requeridos
Then se obtiene un codigo de respuesta
And se obtienen los datos de las mascotas