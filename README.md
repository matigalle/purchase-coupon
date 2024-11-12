# Challenge: Cupón de compra

API REST que permite procesar una lista de ids de items favoritos, y un monto que representa un cupón de compra, para devolver los items que maximizan el total gastado con dicho cupón.

Ejemplo:

Asumiendo la siguiente lista y un cupón de $500.

| Id   | Precio |
|------|--------|
| MLA1 | $100   |
| MLA2 | $210   |
| MLA3 | $260   |
| MLA4 | $80    |
| MLA5 | $90    |

La respuesta será: ["MLA1", "MLA2", "MLA4", "MLA5"].

Para obtener el precio de un item se consulta a la API de items y se lee el valor "price":

curl -X GET https://api.mercadolibre.com/items/$ITEM_ID

Descripción completa: [Challenge Cupón.pdf](https://github.com/user-attachments/files/17721620/Challenge.Cupon.pdf)

## Servicios

**POST /coupon**

Ejemplo:

Request body
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

## Instrucciones

Es necesario tener JDK 17 instalado.

Ejecutar localmente:
```
./gradlew run
```

La API quedará disponible en ```http://localhost:8080/```.

Ejecutar los tests:
```
./gradlew test
```

## Tecnologías

 - Java 17
 - Javalin framework
 - JUnit
 - Gradle
