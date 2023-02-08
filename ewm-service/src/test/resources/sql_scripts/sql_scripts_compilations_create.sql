INSERT INTO compilations (id, pinned, title)
VALUES (1, true, 'test');
INSERT INTO compilations_event (compilations_id, event_id)
VALUES (1, 1);