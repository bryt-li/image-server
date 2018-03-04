FROM nginx
COPY nginx.conf /etc/nginx/
COPY proxy_params /etc/nginx/
COPY sites-enabled/*.conf /etc/nginx/sites-enabled/