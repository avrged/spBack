#!/bin/bash

echo "Deteniendo la aplicación..."
sudo pkill -f "java -jar /opt/apps/backend/spBack*.jar"

echo "Eliminando JAR antiguos..."
find /opt/apps/backend -name "spBack*.jar" -type f -delete

echo "Predeploy completado"