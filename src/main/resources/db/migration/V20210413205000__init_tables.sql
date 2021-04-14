CREATE TABLE customer (
	id int8 NOT NULL,
	"email" varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT customer_pkey PRIMARY KEY (id)
);