# Cupón de compra

API REST que permite obtener una combinación de items que maximicen el uso de un cupón de compra según su precio. Estos items representan favoritos de usuarios. Por otro lado, la API permite devolver el top 5 de items con mas favoritos.

Descripción completa: [Challenge Cupón.pdf](https://github.com/user-attachments/files/17721620/Challenge.Cupon.pdf)

## Servicios

1. Dado un monto y una lista de ids de items, devuelve los items que maximizan el total gastado con dicho cupón.

| HTTP method | Endpoint  |
|-------------|-----------|
| POST        | /coupon   |

#### Ejemplo
Asumiendo esta lista de precios y un cupón de $500, se tendría el siguiente resultado.

| Item id | Precio  |
|---------|---------|
| MLA1    | $100    |
| MLA2    | $210    |
| MLA3    | $260    |
| MLA4    | $80     |
| MLA5    | $90     |

Request:
```json
{
  "item_ids": ["MLA1", "MLA2", "MLA3", "MLA4", "MLA5"],
  "amount": 500
}
```

Response:
```json
{
  "item_ids": ["MLA1", "MLA2", "MLA4", "MLA5"],
  "total": 480
}
```

2. Devuelve el top 5 de items con mas favoritos y su frecuencia

| HTTP method | Endpoint      |
|-------------|---------------|
| GET         | /coupon/stats |

#### Ejemplo

Response:
```json
[
  {
    "MLA3": 3
  },
  {
    "MLA2": 2
  },
  {
    "MLA1": 1
  }
]
```

## Instrucciones para el uso de la API

Para usar localmente, es necesario tener JDK 17 instalado.

Levantar la app:
```
./gradlew run
```

La API quedará disponible en ```http://localhost:8080```.

Ejecutar los tests:
```
./gradlew test
```

La API se encuentra disponible en AWS con la URL:  

http://purchase-coupon-app-lb-147740483.sa-east-1.elb.amazonaws.com

## Tecnologías

 - Java 17
 - Javalin framework
 - Guice
 - Gson
 - Lettuce Redis client
 - JUnit
 - Gradle
 - AWS
