#!/bin/sh

# Parse Render's DATABASE_URL and convert to Spring Boot properties
# DATABASE_URL format: postgresql://user:password@host:port/database
if [ -n "$DATABASE_URL" ]; then
  # Remove protocol prefix
  DB_TEMP=$(echo "$DATABASE_URL" | sed 's|^postgresql://||; s|^postgres://||')

  # Extract user:password and host:port/database
  DB_USERPASS=$(echo "$DB_TEMP" | cut -d'@' -f1)
  DB_HOSTDB=$(echo "$DB_TEMP" | cut -d'@' -f2)

  DB_USER=$(echo "$DB_USERPASS" | cut -d':' -f1)
  DB_PASS=$(echo "$DB_USERPASS" | cut -d':' -f2)

  export JDBC_DATABASE_URL="jdbc:postgresql://${DB_HOSTDB}"
  export SPRING_DATASOURCE_USERNAME="$DB_USER"
  export SPRING_DATASOURCE_PASSWORD="$DB_PASS"
fi

exec java -Dfile.encoding=UTF-8 -jar app.jar
