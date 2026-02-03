ALTER TABLE users
ADD COLUMN role_id BIGINT NOT NULL DEFAULT 1,
ADD COLUMN password_hash TEXT,
ADD CONSTRAINT fk_roles_id_to_role_id foreign key (role_id) references roles(id) on delete cascade;
