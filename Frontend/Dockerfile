# base image
# FROM node:10-alpine as build-step
FROM node:14.1-alpine AS builder

RUN mkdir -p /app
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN npm run build --prod
# RUN ls /app/dist/frontend

# Stage 2
FROM nginx:1.17.1-alpine
COPY --from=builder /app/dist/frontend /usr/share/nginx/html