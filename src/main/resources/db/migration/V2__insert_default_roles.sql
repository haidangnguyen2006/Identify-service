INSERT INTO role (name, description, created_at, updated_at)
VALUES ('USER', 'Người dùng tiêu chuẩn', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

INSERT INTO role (name, description, created_at, updated_at)
VALUES ('ADMIN', 'Quản trị viên hệ thống', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;