FROM python:3.8-slim-buster

RUN apt-get update && apt-get install -y nginx

COPY nginx.conf /etc/nginx/nginx.conf
COPY requirements.txt /app/requirements.txt
RUN pip install -r /app/requirements.txt

COPY app.py /app/app.py
WORKDIR /app

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
