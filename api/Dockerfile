FROM python:3.8.2-alpine3.11

RUN mkdir /src
COPY . ./src 
WORKDIR /src
RUN pip install -r requirements.txt
EXPOSE 5000
ENV FLASK_APP server

CMD ["flask", "run", "--host", "0.0.0.0"]