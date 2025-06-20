INSERT INTO roles (id, name) VALUES ('ADMIN', 'Admin role') ON CONFLICT DO NOTHING;
INSERT INTO roles (id, name) VALUES ('USER', 'User role') ON CONFLICT DO NOTHING;

INSERT INTO users (username, password, name) VALUES ('admin', '$2a$10$5ULdlIy.j7.V5yqNK27uA.7StJ0yBMCgTt2odO55EGwC/a.F23iWK', 'admin') ON CONFLICT DO NOTHING;
INSERT INTO user_roles(user_id, role_id) SELECT id, 'ADMIN' FROM users WHERE username = 'admin';