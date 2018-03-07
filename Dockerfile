FROM nginx

RUN mkdir -p /etc/ssl/certs
COPY cert/cashaflow.com/cashaflow.com.key /etc/ssl/certs/cashaflow.com.key
COPY cert/cashaflow.com/ssl-bundle.crt /etc/ssl/certs/cashaflow.com.crt

COPY nginx.conf /etc/nginx/
COPY proxy_params /etc/nginx/
COPY sites-enabled/*.conf /etc/nginx/sites-enabled/