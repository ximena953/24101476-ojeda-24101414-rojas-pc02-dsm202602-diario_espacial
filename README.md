# Diario espacial

Práctica Calificada 2 — Desarrollo de Aplicaciones Móviles (ESAN, ciclo 2026-2).

Aplicación Android en Kotlin y Jetpack Compose que consume la API abierta de la NASA
(APOD y NeoWs) y guarda los datos en una base de datos local SQLite mediante Room.

## Integrantes

| Rol | Opción | Código | Nombre |
|-----|--------|--------|--------|
| Alumno 1 | Imágenes (APOD) | 24101476 | Flavia Camila Ojeda Rodas |
| Alumno 2 | Asteroides (NeoWs) | 24101414 | Ximena Rojas _(completar nombres)_ |

## Cómo ejecutar el proyecto

1. Generar una clave en <https://api.nasa.gov> (sección **Generate API Key**). La clave llega por correo.
   No usar `DEMO_KEY`.
2. Abrir el proyecto en Android Studio y esperar a que termine el Gradle Sync.
3. Abrir el archivo `local.properties` de la raíz del proyecto (Android Studio lo crea solo) y
   agregar al final esta línea, con la clave propia:

   ```properties
   NASA_API_KEY=tu_clave_aqui
   ```

4. Pulsar **Sync Project with Gradle Files** y luego **Run ▶** en un emulador o teléfono
   con Android 8.0 (API 26) o superior y conexión a internet.

`local.properties` está en `.gitignore`: la clave nunca se sube al repositorio. El archivo
`app/build.gradle.kts` la lee y la publica como `BuildConfig.NASA_API_KEY`. Si falta la clave,
la aplicación no se cierra: muestra el mensaje "Falta la clave NASA_API_KEY en local.properties".

## Acuerdo de coordinación

- **Alumno 1 prepara la base común** en su rama `feature/imagenes`, como primeros commits:
  proyecto Android, dependencias, lectura de la clave con `BuildConfig`, configuración de
  Retrofit (`RetrofitClient`, `NasaApi`), navegación principal con la barra inferior
  (`NavegacionPrincipal`, `Rutas`), componentes comunes (`IndicadorCarga`, `MensajeSinDatos`,
  `BotonFavorito`) y utilidades (`Fechas`, `mensajeDeError`).
- La clase `AppDatabase` necesita al menos una tabla, así que **Alumno 1 la crea junto con su
  tabla** `imagenes_apod` (versión 1).
- **Alumno 2 integra la base común** creando su rama `feature/asteroides` desde `main` cuando el
  Pull Request de Alumno 1 ya está fusionado. Sobre esa base:
  - agrega su endpoint en `NasaApi`;
  - agrega su Entity y su DAO en `AppDatabase` y sube la versión a 2;
  - reemplaza el marcador de Asteroides en `NavegacionPrincipal` por su grafo `asteroidesGraph`.
- Cada opción tiene su propio paquete (`ui/imagenes` y `ui/asteroides`), su Entity, su DAO,
  su Repository y su ViewModel, para no editar los mismos archivos al mismo tiempo.

## Flujo de trabajo con Git

- `main` está protegida: no se hacen commits directos.
- Alumno 1 trabaja en `feature/imagenes` y Alumno 2 en `feature/asteroides`.
- Cada rama se entrega con un Pull Request hacia `main`, que revisa y aprueba el otro integrante
  dejando al menos un comentario de revisión.
- Los Pull Requests se fusionan con **Create a merge commit** para conservar los commits pequeños.

## Opción Imágenes (Alumno 1)

- Endpoint: `GET planetary/apod` con `start_date` y `end_date` (últimos 7 días, desde hace 6 días hasta hoy).
- Tabla Room `imagenes_apod` (`ApodEntity`, `ApodDao`). La fecha es la clave primaria.
- `ApodDto.aEntity()` convierte la respuesta de la API en la Entity; descarta elementos sin fecha.
- `ApodRepository` guarda la respuesta en Room conservando las favoritas; la pantalla lee de Room.
- `ImagenesViewModel` expone `ImagenesUiState` con `StateFlow` (lista, cargando y error).
- Pantallas: lista (de la más reciente a la más antigua), detalle (`hdurl` o `url`, título, fecha,
  autor y explicación) y "Mis imágenes favoritas".
- Si `media_type` es `video`, no se carga imagen: se muestra "Contenido en video".
