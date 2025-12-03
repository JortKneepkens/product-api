# Compose files
COMPOSE = docker compose

# Container names
SPRING_CONTAINER = product-api
REDIS = redis

# ===================================================================
# 🛠 Development Commands
# ===================================================================

# Build development environment
build:
	$(COMPOSE) build --no-cache

# Start development environment (with auto-reload + mounted volumes)
up:
	$(COMPOSE) up

# Stop and remove containers (dev)
down:
	$(COMPOSE) down --remove-orphans

# Tail logs from dev container
logs:
	docker logs -f $(SPRING_CONTAINER)

# Exec into the dev container (interactive shell)
bash:
	docker exec -it $(SPRING_CONTAINER) bash

# Run all tests inside the dev container
test:
	docker exec -it $(SPRING_CONTAINER) ./mvnw test

# Run a specific test class
test-class:
	docker exec -it $(SPRING_CONTAINER) ./mvnw -Dtest=$(TEST) test

# ===================================================================
# 🧹 Cleaning & Utilities
# ===================================================================

# Fully clean Maven + rebuild dev container
clean-all:
	docker compose down -v --remove-orphans
	rm -rf ~/.m2/repository/com/mechhive/product-api
	echo "Clean complete."

# Force Maven clean install inside dev container
maven-clean:
	docker exec -it $(SPRING_CONTAINER) ./mvnw clean install

# Format code (if using spotless or formatter plugin)
format:
	docker exec -it $(SPRING_CONTAINER) ./mvnw spotless:apply || true

help:
	@echo "Available commands:"
	@echo ""
	@echo "Development:"
	@echo "  make dev         - Start dev environment (auto reload)"
	@echo "  make dev-down    - Stop dev environment"
	@echo "  make logs        - Tail dev logs"
	@echo "  make sh          - Shell into dev container"
	@echo "  make test        - Run all tests"
	@echo "  make test-class TEST=ClassName"
	@echo ""
	@echo "Production:"
	@echo "  make prod        - Start production environment"
	@echo "  make prod-down   - Stop production"
	@echo "  make prod-logs   - Tail prod logs"
	@echo ""
	@echo "Utilities:"
	@echo "  make clean-all   - Clean containers + cache"
	@echo "  make maven-clean - Run mvn clean install inside container"
	@echo "  make format      - Format code (if configured)"
	@echo ""