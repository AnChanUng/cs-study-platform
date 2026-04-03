#!/bin/sh

# Convert Render's DATABASE_URL (postgresql://...) to JDBC format (jdbc:postgresql://...)
if [ -n "$DATABASE_URL" ]; then
  export JDBC_DATABASE_URL="jdbc:${DATABASE_URL}"
fi

exec java -Dfile.encoding=UTF-8 -jar app.jar
