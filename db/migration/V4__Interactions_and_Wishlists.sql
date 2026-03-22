CREATE TABLE client_follows_brand (client_id BIGINT REFERENCES clients(id), brand_id BIGINT REFERENCES brands(id), PRIMARY KEY (client_id, brand_id));
CREATE TABLE wishlists (id BIGSERIAL PRIMARY KEY, client_id BIGINT NOT NULL REFERENCES clients(id), name VARCHAR(50) NOT NULL);
CREATE TABLE wishlist_saves_product (wishlist_id BIGINT REFERENCES wishlists(id), product_id BIGINT REFERENCES products(id), PRIMARY KEY (wishlist_id, product_id));
CREATE TABLE brand_reviews (id BIGSERIAL PRIMARY KEY, client_id BIGINT NOT NULL REFERENCES clients(id), brand_id BIGINT NOT NULL REFERENCES brands(id), comment TEXT, rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5), created_at TIMESTAMPTZ DEFAULT NOW());
CREATE TABLE product_reviews (id BIGSERIAL PRIMARY KEY, client_id BIGINT NOT NULL REFERENCES clients(id), product_id BIGINT NOT NULL REFERENCES products(id), comment TEXT, rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5), created_at TIMESTAMPTZ DEFAULT NOW());
