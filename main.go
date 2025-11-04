package main

import (
    "fmt"
    "log"
    "net/http"
)
func handler(w http.ResponseWriter, r *http.Request) {
    
    fmt.Fprint(w, "<h1>Soy alumno de la UOC</h1>")

    switch r.Method {
    case "GET":
        fmt.Fprint(w, "<img src='/static/imagen.png' alt='Logo' style='width: 300px;'/>")
        fmt.Fprint(w, "<br><br>")
        fmt.Fprint(w, "<form method='POST'><button type='submit'>Enviar Petición POST</button></form>")

    case "POST":
        fmt.Fprint(w, "<p>POST recibido.</p>")
        fmt.Fprint(w, "<img src='/static/imagen.png' alt='Logo' style='width: 300px;'/>")
        fmt.Fprint(w, "<br><br>")
        fmt.Fprint(w, "<br><a href='/'>Volver</a>")

    default:
        http.Error(w, "Método no permitido.", http.StatusMethodNotAllowed)
    }
}
func main() {
    fs := http.FileServer(http.Dir("static"))
    http.Handle("/static/", http.StripPrefix("/static/", fs))
    http.HandleFunc("/", handler)
    puerto := "8080"
    log.Printf("Servidor arrancando en http://localhost:%s", puerto)
    log.Fatal(http.ListenAndServe(":"+puerto, nil))
}
