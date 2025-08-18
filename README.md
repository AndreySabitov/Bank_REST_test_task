# Система управления банковскими картами

Backend - приложение на Java (Spring Boot) для управления банковскими картами.

### Безопасность:
* Spring Security + JWT
* Разделение доступа по ролям: ADMIN, USER

### Возможности администратора:
* Создаёт, блокирует, активирует, удаляет карты
* Управляет пользователями
* Видит все карты

### Возможности пользователя:
* Просматривает свои карты (поиск + пагинация)
* Запрашивает блокировку карты
* Делает переводы между своими картами
* Смотрит баланс

### Технологический стек:
* Java 21
* Spring Boot 3.5.0
* Spring Security
* JWT
* Spring Data JPA
* PostgreSQL
* Liquibase
* Swagger (OpenAPI)
* Docker / Docker Compose
* JUnit + Mockito

### Для запуска нужно:
1. Клонировать репозиторий: 
```
git clone https://github.com/AndreySabitov/Bank_REST.git
cd Bank_REST
```
2. Собрать проект:
```
mvn clean package
```
3. Запустить Docker и выполнить:
```
docker compose up -d --build
```

### API:
```
http://localhost:8080/swagger-ui/index.html
```
```
http://localhost:8080/v3/api-docs
```
или docs/openapi.yaml
