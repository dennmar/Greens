FROM python:3.8.2-alpine3.11

RUN mkdir /src
COPY . ./src 
WORKDIR /src

# allows psycopg2-binary to be installed in the following step
RUN apk update && apk add postgresql-dev gcc python3-dev musl-dev

RUN pip install -r requirements.txt
EXPOSE 5000
ENV FLASK_APP server

CMD ["flask", "run", "--host", "0.0.0.0"]