<<<<<<< HEAD
# Usa una imagen oficial de Go como base
FROM golang:1.20

# Configura el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el contenido del proyecto al contenedor
COPY . .

# Construye la aplicación
RUN go build -o main .

# Especifica el comando para ejecutar la aplicación
CMD ["./main"]

# Expone el puerto 8080
EXPOSE 8080
=======
# Usa una imagen oficial de Go como base
FROM golang:1.25

# Configura el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el contenido del proyecto al contenedor
COPY . .

# Construye la aplicación
RUN go build -o main .

# Especifica el comando para ejecutar la aplicación
CMD ["./main"]

# Expone el puerto 8080
EXPOSE 8080
>>>>>>> 878f07f01c0f8a914cffeba5a737373672a8edf9
