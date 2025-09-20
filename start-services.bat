@echo off
echo ========================================
echo    Demarrage des services ST2I
echo ========================================
echo.

echo 1. Demarrage d'Eureka Server...
cd eureka
start "Eureka Server" mvn spring-boot:run
cd ..
timeout /t 15 /nobreak >nul

echo 2. Demarrage du Config Server...
cd config-server
start "Config Server" mvn spring-boot:run
cd ..
timeout /t 10 /nobreak >nul

echo 3. Demarrage du Gateway...
cd gateway
start "Gateway" mvn spring-boot:run
cd ..
timeout /t 10 /nobreak >nul

echo 4. Demarrage du User Service...
cd microservices\User
start "User Service" mvn spring-boot:run
cd ..\..
timeout /t 10 /nobreak >nul

echo.
echo ========================================
echo    Tous les services sont demarres !
echo ========================================
echo.
echo URLs d'acces :
echo - Eureka Server: http://localhost:8761
echo - Config Server: http://localhost:8888
echo - Gateway: http://localhost:8065
echo - User Service: http://localhost:8081
echo.
echo Appuyez sur une touche pour fermer cette fenetre...
pause >nul 