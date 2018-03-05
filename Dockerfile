FROM nginx

RUN mkdir -p /var/www/image_server
COPY dist /var/www/image_server/dist

RUN mkdir -p /etc/ssl/certs
COPY cert.key /etc/ssl/certs/utils.orienteexpress.com.key
COPY cert.pem /etc/ssl/certs/utils.orienteexpress.com.pem

COPY nginx.conf /etc/nginx/
COPY proxy_params /etc/nginx/
COPY sites-enabled/*.conf /etc/nginx/sites-enabled/