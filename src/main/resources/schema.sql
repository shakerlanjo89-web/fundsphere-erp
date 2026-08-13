CREATE TABLE IF NOT EXISTS settings (
    id BIGINT NOT NULL AUTO_INCREMENT,

    organization_name VARCHAR(255),
    short_name VARCHAR(255),
    tagline VARCHAR(255),
    address VARCHAR(255),
    district VARCHAR(255),
    province VARCHAR(255),
    country VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255),
    website VARCHAR(255),
    registration_no VARCHAR(255),
    logo_path VARCHAR(255),

    currency VARCHAR(255),
    date_format VARCHAR(255),

    business_percent INT,
    emergency_percent INT,
    education_percent INT,
    welfare_percent INT,
    administration_percent INT,

    PRIMARY KEY (id)
);