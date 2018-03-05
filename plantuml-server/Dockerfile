FROM n3ziniuka5/ubuntu-oracle-jdk:14.04-JDK8

COPY sources.list /etc/apt/sources.list

RUN apt-get -y update && apt-get install -y curl graphviz

RUN curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -

RUN sudo apt-get install -y nodejs

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