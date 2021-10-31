FROM openjdk:17

# set a directory for the app
WORKDIR /app

# copy all the files to the container
COPY . .

WORKDIR client/src/

RUN javac com/company/Main.java

VOLUME ["/clientdata"]

ENV port 4004

# run the command
CMD ["java", "com/company/Main"]
