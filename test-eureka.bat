@echo off
echo ========================================
echo    Test de demarrage d'Eureka
echo ========================================
echo.

echo Demarrage d'Eureka Server...
cd eureka
mvn spring-boot:run
cd ..

echo.
echo Test termine.
pause 