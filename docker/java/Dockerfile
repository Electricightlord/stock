FROM store/oracle/serverjre:8
COPY stock-0.0.1-SNAPSHOT.jar /var
#ENV MYSQL_IP 127.0.0.1
#ENV MYSQL_PORT 3306
#ENV REDIS_IP 127.0.0.1
#ENV REDIS_PORT 6379
#CMD java -jar -Dspring.redis.ip=$REDIS_IP -Dspring.redis.ip=$REDIS_PORT -Dspring.datasource.url=jdbc:mysql.sh://$MYSQL_IP:$MYSQL_PORT/stock?serverTimezone=Asia/Shanghai /var/stock-0.0.1-SNAPSHOT.jar
CMD java -jar -Dspring.redis.host=172.18.0.3 -Dspring.redis.port=6379 -Dspring.datasource.url=jdbc:mysql://172.18.0.4:3306/stock?serverTimezone=Asia/Shanghai /var/stock-0.0.1-SNAPSHOT.jar
