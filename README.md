# Challenge: Cupón de compra

API REST que permite procesar una lista de ids de items favoritos, y devolver los siguientes resultados a través de 2 servicios:

1. Dado un monto que representa un cupón de compra, devuelve los items que maximizan el total gastado con dicho cupón
2. Devuelve el top 5 de items mas repetidos y su frecuencia

Para obtener el precio de un item se consulta a la API de items y se lee el valor "price":

curl -X GET https://api.mercadolibre.com/items/$ITEM_ID

Descripción completa: [Challenge Cupón.pdf](https://github.com/user-attachments/files/17721620/Challenge.Cupon.pdf)

## Servicios

1. **POST /coupon**

Ejemplo:

Request
```json
{
  "item_ids": ["MLA1", "MLA2", "MLA3", "MLA4", "MLA5"],
  "amount": 500
}
```

Response
```json
{
  "item_ids": ["MLA1", "MLA2", "MLA4", "MLA5"],
  "total": 480
}
```

2. **POST /coupon/stats**

Ejemplo:

Request
```json
{
  "item_ids": ["MLA1", "MLA1", "MLA2", "MLA2", "MLA3", "MLA3", "MLA4", "MLA4", "MLA5", "MLA5", "MLA6"]
}
```

Response
```json
{
  "top_items": {
    "MLA1": 2,
    "MLA2": 2,
    "MLA3": 2,
    "MLA4": 2,
    "MLA5": 2
  }
}
```

## Instrucciones para el uso de la API

Es necesario tener JDK 17 instalado.

Ejecutar localmente:
```
./gradlew run
```

La API quedará disponible en ```http://localhost:8080```.

Ejecutar los tests:
```
./gradlew test
```

## Tecnologías

 - Java 17
 - Javalin framework
 - JUnit
 - Gradle
