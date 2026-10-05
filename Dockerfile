# Built by .github/workflows/deploy.yml and pushed to Artifact Registry.
#
# The Open Liberty starter's Dockerfile, with a build stage in front. Deviations,
# and why:
#   - multi-stage: the image builds the war itself (the stock file expects
#     `./mvnw package` already run on the host), so `docker compose build` is
#     the whole build.
#   - ARG BUILD_ID, like every fleet template.
# Liberty runs as its own non-root user (1001). The port is server.xml's
# httpPort="${PORT}", read from the environment at RUNTIME.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package

FROM icr.io/appcafe/open-liberty:kernel-slim-java21-openj9-ubi-minimal
ARG BUILD_ID=""
ENV PORT=9080 BUILD_ID=$BUILD_ID

COPY --chown=1001:0 /src/main/liberty/config /config

RUN features.sh

COPY --chown=1001:0 --from=build /src/target/*.war /config/apps/

RUN configure.sh

EXPOSE 9080
