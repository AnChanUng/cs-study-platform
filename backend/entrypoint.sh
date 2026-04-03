#!/bin/sh

# Convert Render's DATABASE_URL to JDBC format
# Render gives: postgres://user:pass@host:port/db
# Spring needs: jdbc:postgresql://user:pass@host:port/db
if [ -n "$DATABASE_URL" ]; then
  export JDBC_DATABASE_URL=$(echo "$DATABASE_URL" | sed 's|^postgres://|jdbc:postgresql://|; s|^postgresql://|jdbc:postgresql://|')
fi

exec java -Dfile.encoding=UTF-8 -jar app.jar
