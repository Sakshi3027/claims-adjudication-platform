# Build the Angular app, then serve the static output with nginx.
FROM node:20-alpine AS build
WORKDIR /build
COPY package.json ./
RUN npm install
COPY . .
RUN npx ng build --configuration production

FROM nginx:alpine
COPY --from=build /build/dist/claims-adjudication-frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
