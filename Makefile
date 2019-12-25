container=aggregator_app

up:
	docker-compose up -d

down:
	docker-compose rm -vsf
	docker-compose down -v --remove-orphans

docker-build:
	docker-compose rm -vsf
	docker-compose down -v --remove-orphans
	docker-compose build
	docker-compose up -d

logs:
	docker-compose logs -f