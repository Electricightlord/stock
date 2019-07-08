FROM mysql:latest

ENV MYSQL_ROOT_PASSWORD 123456

EXPOSE 3306

ENV AUTO_RUN_DIR /docker-entrypoint-initdb.d

COPY ./init.sql $AUTO_RUN_DIR/

RUN chmod 777 $AUTO_RUN_DIR/init.sql