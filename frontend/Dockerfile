FROM node:20-alpine AS build
WORKDIR /app

ARG VITE_API_URL=http://localhost:8080
ENV VITE_API_URL=${VITE_API_URL}

COPY package*.json ./
RUN npm ci --prefer-offline

COPY . .
RUN npm run build

FROM nginx:alpine

LABEL org.opencontainers.image.title="Fashtoll Frontend" \
      org.opencontainers.image.description="React + Vite frontend para la plataforma Fashtoll" \
      org.opencontainers.image.vendor="Ceiba" \
      org.opencontainers.image.source="https://github.com/nicojoya/FIS_2610_3513_G4"

RUN apk add --no-cache curl

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD curl -f http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
