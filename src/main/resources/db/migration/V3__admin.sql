INSERT
INTO employee (id, first_name, last_name, username, password, created)
VALUES (nextval('employee_sequence'), 'John', 'Doe', 'admin', 'admin', now());