java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -Ddb.default.url=$SNAP_DB_PG_JDBC_URL -Ddb.default.user=$SNAP_DB_PG_USER -DBdb.default.password=$SNAP_DB_PG_PASSWORD -jar `dirname $0`/sbt-launch.jar "$@"
