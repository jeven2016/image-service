FROM java:8
MAINTAINER Jeven Wang, jujucom@126.com

RUN mkdir -p /opt/tb \
    && mkdir -p /var/data/


RUN cd /opt/tb/ 
RUN ls -l /opt/tb 

ADD run.sh /opt/tb/run.sh
ADD image-service-0.1.jar /var/data/service.jar
RUN chmod a+x /opt/tb/run.sh

EXPOSE 8080

ENTRYPOINT ["/opt/tb/run.sh"]