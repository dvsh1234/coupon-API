# Coupon Management API

**Monk Commerce 2024 - Junior Backend Developer Task**

## Project Overview

The **Coupon Management API** is a RESTful service designed to manage and apply various types of discount coupons for an e-commerce platform. It supports **cart-wise**, **product-wise**, and **Buy X Get Y (BxGy)** coupons, with the flexibility to easily add new coupon types in the future.

This project was developed as part of the Junior Backend Developer Task for **Monk Commerce 2024**.

## Features

### Implemented Cases

1. **Coupon Types**

    - **Cart-wise Coupons**
        - **Description**: Apply a percentage discount to the entire cart if the total amount exceeds a specified threshold.
        - **Example**: 10% off on carts over Rs. 100.
        - **Implementation**: Checks if the cart's total price exceeds the threshold and applies the discount accordingly.

    - **Product-wise Coupons**
        - **Description**: Apply a percentage discount to specific products in the cart.
        - **Example**: 20% off on Product A.
        - **Implementation**: Identifies applicable products in the cart and applies the discount to their prices.

2. **API Endpoints**

    - **Coupon Endpoints**
        - `POST /coupons`: Create a new coupon.
        - `GET /coupons`: Retrieve all coupons.
        - `GET /coupons/{id}`: Retrieve a specific coupon by its ID.
        - `PUT /coupons/{id}`: Update a specific coupon by its ID.
        - `DELETE /coupons/{id}`: Delete a specific coupon by its ID.

    - **Cart Endpoints**
        - `POST /applicable-coupons`: Fetch all applicable coupons for a given cart and calculate the total discount each coupon would apply.
        - `POST /apply-coupon/{id}`: Apply a specific coupon to the cart and return the updated cart with discounted prices.

3. **Database Integration**

    - **MySQL**: Utilized MySQL for persistent storage of coupons, products, and cart details.
    - **Entities**: Defined entities for `Coupon`, `Product`, `Cart`, and `CartItem` with appropriate relationships.
    - **Repositories**: Implemented JPA repositories for data access and manipulation.

4. **Error Handling**

    - Implemented basic error handling for scenarios such as:
        - Coupon not found.
        - Coupon not applicable.
        - GlobalExceptionHandler

5. **Documentation**

    - Provided comprehensive `README.md` documenting implemented cases, unimplemented cases, assumptions, limitations, and instructions to run the project.

### Unimplemented Cases

1. **Buy X Get Y (BxGy) Coupons**
    - **Description**: Implement "Buy X, Get Y" deals with repetition limits and applicability to specific product sets.
    - **Example**: Buy 2 of Product X or Y, get 1 of Product P or Q free.
    - **Reason for Non-Implementation**: Due to time constraints, the logic for handling complex BxGy conditions, including tracking multiple product quantities and managing repetition limits, was not fully implemented.

2. **Multiple Coupon Applications**
    - **Description**: Handling scenarios where multiple coupons can be applied to a single cart.
    - **Reason for Non-Implementation**: The current implementation supports applying a single coupon at a time. Managing multiple coupon applications requires additional logic to handle conflicts and cumulative discounts.

3. **Coupon Expiration Handling in Real-time**
    - **Description**: Automatically invalidating expired coupons without manual checks.
    - **Reason for Non-Implementation**: The system currently checks for coupon expiration during application, but does not have a background scheduler to deactivate expired coupons proactively.

## Assumptions

- **Single Coupon Application**: Only one coupon can be applied to a cart at a time.
- **Product Identification**: Each product has a unique `product_id` which is used to apply product-wise discounts.
- **Currency**: All prices and discounts are handled in Indian Rupees (Rs.).
- **Coupon Validity**: A coupon is considered valid if its expiration date is on or after the current date.
- **Data Consistency**: It is assumed that the product data is consistent and up-to-date in the database.
- **User Authentication**: The API does not handle user authentication or authorization. It is assumed to be managed externally or is not required for this task.

## Limitations

- **BxGy Coupon Logic**: The system does not currently support "Buy X, Get Y" coupons, limiting the types of promotions that can be applied.
- **Multiple Coupon Applications**: Only one coupon can be applied to a cart at a time, restricting promotional flexibility.
- **Lack of Advanced Error Handling**: While basic error handling is implemented, more granular error messages and handling for complex scenarios are not present.
- **Scalability Considerations**: The current implementation may not be optimized for high-traffic scenarios, as performance optimizations like caching are not implemented.
- **No Frontend Integration**: The API is backend-only and does not include any frontend interface for interaction.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **Spring Boot DevTools**
- **Maven**



