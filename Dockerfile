# Start with the official Debian image
FROM debian:bullseye-slim

# Install OpenJDK 17 and Maven
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    && rm -rf /var/lib/apt/lists/*  # Clean up the apt cache to reduce image size

# Dynamically determine the JDK installation path
RUN JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac)))) \
    && echo "JAVA_HOME is set to $JAVA_HOME" \
    && echo "export JAVA_HOME=$JAVA_HOME" >> /etc/profile.d/java_home.sh \
    && echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> /etc/profile.d/java_home.sh

# Source the profile script to ensure JAVA_HOME is available during runtime
RUN chmod +x /etc/profile.d/java_home.sh && . /etc/profile.d/java_home.sh

# Remove hardcoded JAVA_HOME
ENV MAVEN_HOME=/usr/share/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Validate JAVA_HOME during runtime
RUN java -version && javac -version

# Set the working directory
WORKDIR /app
# Declare the volume mount point inside the container
VOLUME ["/data"]

# Copy the project files into the container
COPY . .

# Command to run the Maven tests using the specified TestNG XML file
#CMD ["mvn", "clean", "test", "-Dtestng.suiteXmlFiles=testng.xml"]
CMD ["mvn", "clean", "test"]
