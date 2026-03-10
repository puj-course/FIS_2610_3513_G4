CREATE TABLE tags (id BIGSERIAL PRIMARY KEY, name VARCHAR(50) UNIQUE NOT NULL, type VARCHAR(30) NOT NULL);
CREATE TABLE brand_tags (brand_id BIGINT REFERENCES brands(id), tag_id BIGINT REFERENCES tags(id), PRIMARY KEY (brand_id, tag_id));
CREATE TABLE product_tags (product_id BIGINT REFERENCES products(id), tag_id BIGINT REFERENCES tags(id), PRIMARY KEY (product_id, tag_id));
