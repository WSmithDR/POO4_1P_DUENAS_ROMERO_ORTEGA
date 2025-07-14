# POO4_1P_DUENAS_ROMERO_ORTEGA

## Diagrama del Proyecto
Puedes ver el diagrama del proyecto en el siguiente enlace: [Diagrama en Lucidchart](https://lucid.app/lucidchart/7d5b5954-47b4-40fc-841c-178a1c0f842b/edit?viewport_loc=-1266%2C-76%2C2576%2C1290%2CHWEp-vi-RSFO&invitationId=inv_57f22e96-9c0e-4b56-97c8-1871544f12eb)

## Tutorial para crear el manejador de correos:
https://www.youtube.com/watch?v=Ngpe7LyELIY

**Cómo leer variables del `.env` en Java:**
- Se utiliza la clase `EnvReader` en el paquete `services` para acceder a las variables de entorno de forma segura y centralizada.

## Recursos útiles

- [Cómo leer archivos .env en proyectos Maven (Búsqueda Google)](https://www.google.com/search?q=como+leer+los+archvios+de+un+.env+file+en+maven%3F)
- [Librería dotenv-java en GitHub](https://github.com/cdimascio/dotenv-java)

## Tablero de Tareas (Jira)
[Jira - Tablero de Tareas del Proyecto](https://espol-team-i8r9u34a.atlassian.net/jira/software/projects/OPS/boards/1?jql=assignee%20%3D%20712020%3A81ed6eb9-98e6-4f22-8d8f-1ee229fb5124)

## ¡OJO! Carpeta database

Para que el sistema funcione bien, hay que copiar la carpeta `database para copiar` que está dentro de la carpeta `PROYECTO 1P` y pegarla en la raíz de este proyecto como `database` y asegurarse de que este siendo ignorada por git para evitar conflictos a la hora de mergear lso cambios.

## Cómo habilitar el envío de correos desde Gmail

Para que el sistema pueda enviar correos electrónicos usando una cuenta de Gmail, necesitas crear una contraseña de aplicación en tu cuenta de Google. Puedes hacerlo fácilmente desde el siguiente enlace:

[Crear contraseña de aplicación de Gmail](https://accounts.google.com/v3/signin/challenge/pwd?TL=ALgCv6wYqI2pvXzCzSMvA8TCqDlJN2hpvgbP4vSkPmpbRbysl9XginHyFFv-csQT&authuser=0&cid=2&continue=https%3A%2F%2Fmyaccount.google.com%2Fapppasswords%3Fhl%3Des%26utm_source%3DOGB%26utm_medium%3Dact%26gar%3DWzEyMF0%26pli%3D1%26rapt%3DAEjHL4O6V4IV1xuY5N8PtS0WMBq8Ty1pIQkjKT3JdQ9fCoeUIcc9GSxJghgHAvXVU61U7dTQKgjAXTn-UbNfEnIJyTKjsupLUp4ysL_HeR211XvKZ_jYMOs&flowName=GlifWebSignIn&followup=https%3A%2F%2Fmyaccount.google.com%2Fapppasswords%3Fhl%3Des%26utm_source%3DOGB%26utm_medium%3Dact%26gar%3DWzEyMF0%26pli%3D1%26rapt%3DAEjHL4O6V4IV1xuY5N8PtS0WMBq8Ty1pIQkjKT3JdQ9fCoeUIcc9GSxJghgHAvXVU61U7dTQKgjAXTn-UbNfEnIJyTKjsupLUp4ysL_HeR211XvKZ_jYMOs&hl=es&ifkv=AdBytiP0bGRtCDq0i2D1KjrSgMY8CCOfhDuQJqzMb-IBIj_zbELVIHO-w1qKYcTF_BBDq9sCGLIhaw&osid=1&rart=ANgoxcfISvuKN4vQeI4DBiWmbB1JL08lSRfkxULb5em_XGiMwg-f9pohaFBST1NzapK4UsCvW60QLat7Y_oeSryzpHrg4R3jvDlRs3hX3cJZMeHRGUpWhuQ&rpbg=1&service=accountsettings)

Sigue los pasos que te indica Google y usa la contraseña generada en tu archivo `.env` o configuración del sistema.

## Configuración del archivo .env para envío de correos

Debes crear un archivo llamado `.env` en la raíz del proyecto con la siguiente información (reemplaza los valores por los de tu cuenta):

```
EMAIL_SYSTEM=tu_correo@gmail.com
PASSWORD_SYSTEM=tu_contraseña_de_aplicacion
```

- `EMAIL_SYSTEM`: El correo de Gmail desde el que se enviarán los mensajes.
- `PASSWORD_SYSTEM`: La contraseña de aplicación generada en Google (no tu contraseña normal, sino la especial para apps).

Asegúrate de que este archivo **no** se suba a git (ya está en el .gitignore por seguridad).