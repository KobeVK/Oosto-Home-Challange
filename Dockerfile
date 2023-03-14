FROM alpine:3.14.3

ARG MY_STRING
ENV MY_STRING_ENV=$MY_STRING

RUN apk update && \
    apk add --no-cache nginx && \
    echo "daemon off;" >> /etc/nginx/nginx.conf && \
    sed -i "s/Welcome to nginx!/My string is: \$MY_STRING_ENV/g" /usr/share/nginx/html/index.html && \
    chown -R nginx:nginx /var/lib/nginx

EXPOSE 80

CMD ["nginx"]