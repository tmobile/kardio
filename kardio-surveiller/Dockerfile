FROM openjdk:8-jre-alpine3.7
MAINTAINER T-Mobile

WORKDIR /surveiller

ADD target/kardio-surveiller.jar /surveiller/
ADD target/dependency-jars /surveiller/dependency-jars
COPY config/surveiller-cron.jobs /etc/cron.d/surveiller-cron
ADD config /surveiller/
RUN chmod 755 /surveiller/*.sh

RUN crontab /etc/cron.d/surveiller-cron

CMD ["crond", "-f"]
