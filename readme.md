# Документация

### Тема проекта: web-приложения для хлебобулочного магазина 

### Автор: *Дударев Артём*


## Используемые фреймворки

* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

## Используемая база данных

* PostgreSQL

## Используемый сборщик проекта

* Maven

## Дополнительные технологии

* Lombok
* JUnit5
* Mockito
* Postman

## Описание эндпоинтов

### Доступные всем

**GET** /api/products — *Позволяет незарегестрированным пользователям пользователям просмотреть продаваемый товар*

### Доступные зарегистрированным пользователям

**GET** /api/products
/api/addresses/**
/api/categories
/api/categories/{id}
/api/images
/api/images/{id}
/api/images/by-product/{productId}
/api/loyalty-programs
/api/loyalty-programs/{id}
/api/orders/create
/api/orders/{orderId}/add-item
/api/orders/{orderId}/confirm
/api/orders/order/{id}
/api/payment-methods/{id}
/api/payment-methods
/api/products/{id}
/api/products
/api/profile/create
/api/profile/update/{id}
/api/profile/getProfile/{id}
/api/profile/delete/{id}

### Доступные работникам огганизации

/api/orders/**
/api/products/update/{id}
/api/roles/{id}

### Доступные администратору



