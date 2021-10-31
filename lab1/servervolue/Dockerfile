FROM python:3

# set a directory for the app
WORKDIR /app

# copy all the files to the container
COPY . .

ENV port 4004

EXPOSE $port

VOLUME ["/serverdata"]

# run the command
CMD ["python", "./server.py"]
