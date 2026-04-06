CREATE TABLE permission (
    name VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE role (
    name VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    dob DATE
);

CREATE TABLE role_permissions (
    role_name VARCHAR(255) REFERENCES role(name),
    permissions_name VARCHAR(255) REFERENCES permission(name),
    PRIMARY KEY (role_name, permissions_name)
);

CREATE TABLE user_roles (
    user_id VARCHAR(255) REFERENCES users(id),
    role_name VARCHAR(255) REFERENCES role(name),
    PRIMARY KEY (user_id, role_name)
);

CREATE TABLE invalidated_token (
    id VARCHAR(255) PRIMARY KEY,
    exp TIMESTAMP
);