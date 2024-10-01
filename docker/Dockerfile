from debian:12

RUN apt-get update && \
	apt-get install -y \
		procps \
		bash \
		vim \
		netcat-traditional \
		strace \
		less \
		gradle \
		openjdk-17-jdk-headless

RUN mkdir -p /app
WORKDIR /app

COPY build/libs/timekeeping-0.0.1-SNAPSHOT.jar /app
# COPY gradle /app/gradle/
# COPY src /app/src/
# COPY build.gradle gradle.properties gradlew settings.gradle /app/
# CMD ["./gradlew", "bootRun"]
CMD ["java", "-jar", "/app/timekeeping-0.0.1-SNAPSHOT.jar"]
