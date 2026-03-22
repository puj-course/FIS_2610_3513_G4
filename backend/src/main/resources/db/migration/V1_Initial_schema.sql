-- V1__Initial_schema.sql
-- Fashtoll Database Schema for PostgreSQL

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('CLIENT', 'BRAND', 'ADMIN')),
    user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('CLIENT', 'BRAND')),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE clients (
    id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE brands (
    id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    picture_url VARCHAR(500),
    link_official VARCHAR(255),
    followers INTEGER DEFAULT 0,
    rating NUMERIC(2,1) DEFAULT 0,
    is_verified BOOLEAN DEFAULT FALSE
);

CREATE TABLE product_types (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('BOTTOMS', 'TOPS', 'OUTERWEAR', 'FULL_BODY'))
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    brand_id BIGINT NOT NULL REFERENCES brands(id) ON DELETE CASCADE,
    product_type_id BIGINT NOT NULL REFERENCES product_types(id),
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price NUMERIC(12,2) NOT NULL,
    general_fit VARCHAR(50) NOT NULL CHECK (general_fit IN ('SLIM', 'REGULAR', 'LOOSE')),
    gender VARCHAR(20) NOT NULL CHECK (gender IN ('MALE', 'FEMALE', 'UNISEX')),
    color VARCHAR(30) NOT NULL CHECK (color IN ('WHITE', 'BLACK', 'GREY', 'BROWN', 'BEIGE', 'GREEN', 'BLUE', 'PURPLE', 'RED', 'ORANGE', 'PINK', 'YELLOW', 'GOLD', 'SILVER', 'MULTICOLOR', 'OTHER')),
    available BOOLEAN DEFAULT TRUE,
    rating NUMERIC(2,1) DEFAULT 0,
    link_product VARCHAR(500),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_images (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    image_url VARCHAR(500) NOT NULL
);

CREATE TABLE client_follows_brand (
    client_id BIGINT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    brand_id BIGINT NOT NULL REFERENCES brands(id) ON DELETE CASCADE,
    PRIMARY KEY (client_id, brand_id)
);

CREATE TABLE wishlists (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE wishlist_saves_product (
    wishlist_id BIGINT NOT NULL REFERENCES wishlists(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    PRIMARY KEY (wishlist_id, product_id)
);

CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    type VARCHAR(30) NOT NULL CHECK (type IN ('STYLE', 'OCCASION', 'FIT'))
);

CREATE TABLE brand_tags (
    brand_id BIGINT NOT NULL REFERENCES brands(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (brand_id, tag_id)
);

CREATE TABLE product_tags (
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, tag_id)
);

CREATE TABLE brand_reviews (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    brand_id BIGINT NOT NULL REFERENCES brands(id) ON DELETE CASCADE,
    comment TEXT,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_reviews (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    comment TEXT,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_brand_id ON products(brand_id);
CREATE INDEX idx_products_product_type_id ON products(product_type_id);
CREATE INDEX idx_product_images_product_id ON product_images(product_id);
CREATE INDEX idx_wishlists_client_id ON wishlists(client_id);
CREATE INDEX idx_wishlist_saves_product_product_id ON wishlist_saves_product(product_id);
CREATE INDEX idx_brand_tags_tag_id ON brand_tags(tag_id);
CREATE INDEX idx_product_tags_tag_id ON product_tags(tag_id);
CREATE INDEX idx_brand_reviews_client_id ON brand_reviews(client_id);
CREATE INDEX idx_brand_reviews_brand_id ON brand_reviews(brand_id);
CREATE INDEX idx_product_reviews_client_id ON product_reviews(client_id);
CREATE INDEX idx_product_reviews_product_id ON product_reviews(product_id);
CREATE INDEX idx_client_follows_brand_brand_id ON client_follows_brand(brand_id);
CREATE INDEX idx_users_email ON users(email);
