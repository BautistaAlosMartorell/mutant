# 🧬 Parcial BakEnd Desarrollo de Software - Detección de Mutantes

Este proyecto fue desarrollado para detectar mutantes basándose en su secuencia de ADN. Magneto busca reclutar a la mayor cantidad de mutantes posibles para enfrentarse a los X-Mens, y para ello se ha implementado una API en Java con Spring Boot.

## 📜 Descripción
El programa identifica si un humano es mutante analizando su secuencia de ADN. Un humano es considerado mutante si se encuentran **dos o más secuencias de cuatro letras iguales** en direcciones horizontal, vertical u oblicua en una matriz (NxN) compuesta de bases nitrogenadas representadas por: A, T, C y G.

## 🏛 Arquitectura
El proyecto sigue una arquitectura en capas que separa:

- **Controladores**: manejo de las peticiones HTTP.
- **Servicios**: lógica de negocio.
- **Repositorios**: conexión con la base de datos H2.

## 🔗 Endpoints
1. **🧪 Detección de Mutantes**  
   - **POST** `/mutant/`: Verifica si el ADN enviado corresponde a un mutante.
   - **Body**: JSON con la secuencia de ADN.
   - **Respuesta**: `200 OK` si es mutante, `403 Forbidden` si no lo es.

2. **📊 Estadísticas**  
   - **GET** `/stats`: Devuelve estadísticas de las verificaciones de ADN.
   - **Respuesta**: JSON con el conteo de mutantes, humanos y el ratio.

## 👤 Autor
- **Bautista Alòs Martorell**  
- **Legajo**: 50523  
- **Comisión**: 3K9, Desarrollo de Software, UTN FRM  

---

