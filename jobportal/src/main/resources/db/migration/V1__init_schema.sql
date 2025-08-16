-- Tworzenie tabel
CREATE TABLE job_company (
    id INTEGER NOT NULL PRIMARY KEY,
    logo VARCHAR(255),
    name VARCHAR(255)
);

CREATE SEQUENCE job_company_id_seq START 1 INCREMENT BY 1;
ALTER SEQUENCE job_company_id_seq OWNED BY job_company.id;
ALTER TABLE job_company ALTER COLUMN id SET DEFAULT nextval('job_company_id_seq');

CREATE TABLE job_location (
    id INTEGER NOT NULL PRIMARY KEY,
    city VARCHAR(255),
    country VARCHAR(255),
    state VARCHAR(255)
);

CREATE SEQUENCE job_location_id_seq START 1 INCREMENT BY 1;
ALTER SEQUENCE job_location_id_seq OWNED BY job_location.id;
ALTER TABLE job_location ALTER COLUMN id SET DEFAULT nextval('job_location_id_seq');

CREATE TABLE job_post_activity (
    job_post_id INTEGER NOT NULL PRIMARY KEY,
    description_of_job VARCHAR(10000),
    job_title VARCHAR(255),
    job_type VARCHAR(255),
    posted_date TIMESTAMP,
    remote VARCHAR(255),
    salary VARCHAR(255),
    job_company_id INTEGER,
    job_location_id INTEGER,
    posted_by_id INTEGER,
    CONSTRAINT job_post_activity_job_company_id_fkey FOREIGN KEY (job_company_id) REFERENCES job_company(id),
    CONSTRAINT job_post_activity_job_location_id_fkey FOREIGN KEY (job_location_id) REFERENCES job_location(id),
    CONSTRAINT job_post_activity_posted_by_id_fkey FOREIGN KEY (posted_by_id) REFERENCES users(user_id)
);

CREATE SEQUENCE job_post_activity_job_post_id_seq START 1 INCREMENT BY 1;
ALTER SEQUENCE job_post_activity_job_post_id_seq OWNED BY job_post_activity.job_post_id;
ALTER TABLE job_post_activity ALTER COLUMN job_post_id SET DEFAULT nextval('job_post_activity_job_post_id_seq');

CREATE TABLE job_seeker_apply (
    id INTEGER NOT NULL PRIMARY KEY,
    apply_date TIMESTAMP,
    cover_letter VARCHAR(255),
    job INTEGER REFERENCES job_post_activity(job_post_id),
    user_id INTEGER REFERENCES job_seeker_profile(user_account_id),
    CONSTRAINT job_seeker_apply_user_id_job_key UNIQUE (user_id, job)
);

CREATE SEQUENCE job_seeker_apply_id_seq START 1 INCREMENT BY 1;
ALTER SEQUENCE job_seeker_apply_id_seq OWNED BY job_seeker_apply.id;
ALTER TABLE job_seeker_apply ALTER COLUMN id SET DEFAULT nextval('job_seeker_apply_id_seq');

CREATE TABLE job_seeker_profile (
    user_account_id INTEGER NOT NULL PRIMARY KEY REFERENCES users(user_id),
    city VARCHAR(255),
    country VARCHAR(255),
    employment_type VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    profile_photo VARCHAR(255),
    resume VARCHAR(255),
    state VARCHAR(255),
    work_authorization VARCHAR(255)
);

CREATE TABLE job_seeker_save (
    id INTEGER NOT NULL PRIMARY KEY,
    job INTEGER REFERENCES job_post_activity(job_post_id),
    user_id INTEGER REFERENCES job_seeker_profile(user_account_id),
    CONSTRAINT job_seeker_save_user_id_job_key UNIQUE (user_id, job)
);

CREATE SEQUENCE job_seeker_save_id_seq START 1 INCREMENT BY 1;
ALTER SEQUENCE job_seeker_save_id_seq OWNED BY job_seeker_save.id;
ALTER TABLE job_seeker_save ALTER COLUMN id SET DEFAULT nextval('job_seeker_save_id_seq');

CREATE TABLE recruiter_profile (
    user_account_id INTEGER NOT NULL PRIMARY KEY REFERENCES users(user_id),
    city VARCHAR(255),
    company VARCHAR(255),
    country VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    profile_photo VARCHAR(64),
    state VARCHAR(255)
);

CREATE TABLE skills (
    id INTEGER NOT NULL PRIMARY KEY,
    experience_level VARCHAR(255),
    name VARCHAR(255),
    years_of_experience VARCHAR(255),
    job_seeker_profile INTEGER REFERENCES job_seeker_profile(user_account_id)
);

CREATE SEQUENCE skills_id_seq START 1 INCREMENT BY 1;
ALTER SEQUENCE skills_id_seq OWNED BY skills.id;
ALTER TABLE skills ALTER COLUMN id SET DEFAULT nextval('skills_id_seq');

CREATE TABLE users_type (
    user_type_id INTEGER NOT NULL PRIMARY KEY,
    user_type_name VARCHAR(255)
);

CREATE SEQUENCE users_type_user_type_id_seq START 1 INCREMENT BY 1;
ALTER SEQUENCE users_type_user_type_id_seq OWNED BY users_type.user_type_id;
ALTER TABLE users_type ALTER COLUMN user_type_id SET DEFAULT nextval('users_type_user_type_id_seq');

CREATE TABLE users (
    user_id INTEGER NOT NULL PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    is_active BOOLEAN,
    password VARCHAR(255),
    registration_date TIMESTAMP,
    user_type_id INTEGER REFERENCES users_type(user_type_id)
);

CREATE SEQUENCE users_user_id_seq START 1 INCREMENT BY 1;
ALTER SEQUENCE users_user_id_seq OWNED BY users.user_id;
ALTER TABLE users ALTER COLUMN user_id SET DEFAULT nextval('users_user_id_seq');

-- Dane początkowe
INSERT INTO users_type (user_type_id, user_type_name) VALUES (1, 'Recruiter');
INSERT INTO users_type (user_type_id, user_type_name) VALUES (2, 'Job Seeker');
