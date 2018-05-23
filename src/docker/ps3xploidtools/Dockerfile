FROM openjdk:8
CMD mkdir /work && mkdir /databases && mkdir /work/bin && mkdir /app
WORKDIR /app
COPY /bin /work/bin
COPY ps3xploidtools.jar /app
ENTRYPOINT java -jar ps3xploidtools.jar