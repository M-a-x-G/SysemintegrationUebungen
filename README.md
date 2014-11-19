Systemintegration Übungen
==================================

*Übung 1*:  http://localhost:8080/Mandelbrot/?action=getMandelbrot&h=600&w=800&it=100

*Übung 3*:  <b>Im AWSCredentialsHelper müssen die Credentials eingetragen werden.</b><br>
          Erstellen -> http://localhost:8080/Mandelbrot/?action=generateToS3&bucketname=<<b>namen einsetzen</b>>
          Löschen -> http://localhost:8080/Mandelbrot/?action=deleteFromS3&bucketname=<<b>namen einsetzen</b>>

*Übung 5*: <b>Im CredentialsHelper müssen Credentials eingetragen werden.</b><br>
          Um den QuartzPollJob zu starten -> http://localhost:8080/Mandelbrot/?action=pollqueue<br>
          Um etwas in die Queue zu schreiben -> http://localhost:8080/Mandelbrot<br>
          
