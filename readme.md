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

**GET** /api/products — *Позволяет незарегестрированным пользователям просмотреть продаваемый товар*

### Доступные зарегистрированным пользователям

/api/addresses/**  \
**GET** /api/categories — *Просмотр категорий товаров* \
**GET** /api/categories/{id} \
**GET** /api/images \
**GET** /api/images/{id} \
**GET** /api/images/by-product/{productId} \
**GET** /api/loyalty-programs \
**GET** /api/loyalty-programs/{id} \
**POST** /api/orders/create \
**POST** /api/orders/{orderId}/add-item \
**PUT** /api/orders/{orderId}/confirm \
**GET** /api/orders/order/{id} \
**GET** /api/payment-methods/{id} \
**GET** /api/payment-methods \
**GET** /api/products/{id} \
**GET** /api/products \
**POST** /api/profile/create \
**PUT** /api/profile/update/{id} \
**GET** /api/profile/getProfile/{id} \
**DELETE** /api/profile/delete/{id} \

### Доступные работникам огганизации

/api/orders/** \
**PUT** /api/products/update/{id} \
**GET** /api/roles/{id} \

### Доступные администратору



