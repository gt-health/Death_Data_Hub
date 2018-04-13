## Local development

```
npm install
```

```
npm start
```

## Local dev w/ Docker

```
docker build -t [your-app-tag] .
```

```
docker run -it -v ${PWD}:/usr/src/app -p 3000:3000 --rm [your-app-tag]
```

## Local dev w/ Docker Compose

```
docker-compose up -d --build
```

## Prod deploy w/ Docker compose

```
docker-compose -f docker-compose-prod.yml up -d --build
```
