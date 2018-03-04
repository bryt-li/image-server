FROM ubuntu:16.04

ENV DEBIAN_FRONTEND noninteractive
ENV NPM_CONFIG_LOGLEVEL=${NPM_CONFIG_LOGLEVEL:-info}    \
    NODE_VERSION=${NODE_VERSION:-8.0.0}                 \
    SVC_USER=${SVC_USER:-node}

RUN                                 \
    groupadd    --system            \
                --force             \
                  "${SVC_USER}"     \
 && useradd     --system            \
                --gid "${SVC_USER}" \
                --shell /bin/bash   \
                  "${SVC_USER}"     \
\
##  GPG keys listed at https://github.com/nodejs/node#release-team  \ 
##  --------------------------------------------------------------------------------  ## \ 
 && set -ex                                         \
 && for key in                                      \
        9554F04D7259F04124DE6B476D5A82AC7E37093B    \
        94AE36675C464D64BAFA68DD7434390BDBE9B9C5    \
        0034A06D9D9B0064CE8ADF6BF1747F4AD2306D93    \
        FD3A5288F042B6850C66B31F09FE44734EB7990E    \
        71DCFD284A79C3B38668286BC97EC7A07EDE3FC1    \
        DD8F2338BAE7501E3DD5AC78C273792F7D83545D    \
        B9AE9905FFD7803F25714661B63B535A4C206CA9    \
        C4F0DFFF4E8C1A8236409D08E73BC641CC11F4C8    \
  ; do                                              \
        gpg --keyserver ha.pool.sks-keyservers.net  \
            --recv-keys "$key";                     \
    done                                            \
\
##  Tools Setup                     \
##  --------------------------------------------------------------------------------  ## \ 
 && apt-get -q update               \
 && apt-get -y install              \
            --no-install-recommends \
            curl                    \
            mc                      \
            wget                    \
 && apt-get clean                   \
 && rm -rf /var/lib/apt/lists/*     \
 && printf "\n\tDEPLOYED: \t TOOLS \n" \
\
##  Node.js Setup                 */\ 
##  --------------------------------------------------------------------------------  ##        \ 
 && curl -SLO "https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}-linux-x64.tar.xz" \
 && curl -SLO "https://nodejs.org/dist/v${NODE_VERSION}/SHASUMS256.txt.asc"                     \
 && gpg --batch --decrypt --output SHASUMS256.txt SHASUMS256.txt.asc                            \
 && grep "node-v${NODE_VERSION}-linux-x64.tar.xz\$" SHASUMS256.txt | sha256sum -c -             \
 && tar -xJf node-v${NODE_VERSION}-linux-x64.tar.xz -C /usr/local --strip-components=1          \
 && rm "node-v${NODE_VERSION}-linux-x64.tar.xz" SHASUMS256.txt.asc SHASUMS256.txt               \
 && ln -s /usr/local/bin/node /usr/local/bin/nodejs                                             \
 && printf "\n\n\tDEPLOYED: \t Node.js:$(node -v) \n\n"


 
RUN apt-get update -qq && apt-get install -qq gdebi wget libgd2-xpm-dev make
RUN wget -O $HOME/graphviz-2.38.0.deb \
        http://www.graphviz.org/pub/graphviz/stable/ubuntu/ub12.04/x86_64/graphviz_2.38.0-1~precise_amd64.deb
RUN wget -O $HOME/libgraphviz4.deb \
        http://www.graphviz.org/pub/graphviz/stable/ubuntu/ub12.04/x86_64/libgraphviz4_2.38.0-1~precise_amd64.deb
# RUN apt-get remove libcdt4 libpathplan4 libxdot4
RUN wget -O libgd-xpm.deb http://security.ubuntu.com/ubuntu/pool/main/libg/libgd2/libgd2-xpm_2.0.36~rc1~dfsg-6ubuntu2.1_amd64.deb
RUN dpkg -i libgd-xpm.deb && apt-get install -f
RUN yes | gdebi $HOME/libgraphviz4.deb
RUN yes | gdebi $HOME/graphviz-2.38.0.deb


# Copy source code
COPY . /app

# Change working directory
WORKDIR /app

# Install dependencies
RUN npm install

# Expose API port to the outside
EXPOSE 8182

# Launch application
CMD ["npm","start"]