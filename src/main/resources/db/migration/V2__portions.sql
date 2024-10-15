INSERT
INTO
    portion (id, name, created, updated)
VALUES
    (nextval('portion_sequence'), 'Small', now(), null),
    (nextval('portion_sequence'), 'Medium', now(), null),
    (nextval('portion_sequence'), 'Large', now(), null);