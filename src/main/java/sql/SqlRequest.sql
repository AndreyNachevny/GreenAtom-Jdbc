
SELECT sportsman_name, year_of_birth, country
FROM records.r_sportsman;

SELECT discipline_description, world_record
FROM records.r_discipline;

SELECT sportsman_name
FROM records.r_sportsman
WHERE (year_of_birth = 1990);

SELECT discipline_description, world_record
FROM records.r_discipline
WHERE (set_date = '2020-11-21' OR set_date = '2015-11-27');


SELECT DISTINCT hold_date
FROM records.r_competition comp
         JOIN records.r_result res ON (comp.competition_id = res.competition_id)
WHERE (res.result < 30.00 AND comp.city = 'Winsdor');


SELECT hold_date
FROM records.r_competition
WHERE city LIKE 'W%';


SELECT sportsman_name
FROM records.r_sportsman
WHERE sportsman_name LIKE 'S%' AND (year_of_birth % 10) != 6;


SELECT competition_name
FROM records.r_competition
WHERE competition_name LIKE '%World%';

SELECT DISTINCT year_of_birth
FROM records.r_sportsman;


SELECT COUNT(*)
FROM records.r_result res
         JOIN records.r_competition comp ON (res.competition_id = comp.competition_id)
WHERE comp.hold_date = '2016-12-06';


SELECT MIN(year_of_birth)
FROM records.r_sportsman
WHERE sportsman_name LIKE 'S%';

SELECT s.sportsman_id, s.sportsman_name, AVG(r.result) AS average_result
FROM records.r_sportsman s
         JOIN records.r_result r ON (s.sportsman_id = r.sportsman_id)
GROUP BY s.sportsman_id, s.sportsman_name;

SELECT
        'Спортсмен ' || s.sportsman_name || ' показал результат ' || r.result || ' в городе ' || c.city AS results
FROM records.r_sportsman s
         JOIN records.r_result r ON s.sportsman_id = r.sportsman_id
         JOIN records.r_competition c ON (r.competition_id = c.competition_id);

SELECT s.sportsman_name, s.year_of_birth, s.country, d.discipline_description
FROM records.r_sportsman s
         JOIN records.r_personal_records pr ON (s.sportsman_id = pr.sportsman_id)
         JOIN records.r_discipline d ON (pr.discipline_id = d.discipline_id)
WHERE (pr.personal_record = d.world_record);

SELECT COUNT(DISTINCT s.sportsman_id)
FROM records.r_sportsman s
         JOIN records.r_result r ON s.sportsman_id = r.sportsman_id
         JOIN records.r_competition c ON r.competition_id = c.competition_id
WHERE s.sportsman_name LIKE '%Puts%' AND c.competition_name LIKE '%World%';

SELECT r_sportsman.country, COUNT(*) AS participant_count
FROM records.r_sportsman
GROUP BY r_sportsman.country
ORDER BY participant_count DESC
    LIMIT 1;

UPDATE records.r_competition
SET competition_name = 'FINA World Swimming Championships 2016 (25 m)'
WHERE competition_name = 'FINA World Swimming Championships (25 m)';

SELECT DISTINCT sportsman_name
FROM records.r_sportsman s
         JOIN records.r_personal_records pr ON (s.sportsman_id = pr.sportsman_id)
         JOIN records.r_discipline r ON (r.discipline_id = pr.discipline_id)
WHERE (pr.personal_record != 20.99 AND r.discipline_description = '50m freestyle. Men (25m pool)');

UPDATE records.r_competition
SET hold_date = hold_date + INTERVAL '4 days'
WHERE city = 'Winsdor';

DELETE FROM records.r_result
WHERE sportsman_id IN (
    SELECT sportsman_id
    FROM records.r_sportsman
    WHERE year_of_birth = 2001
);

SELECT DISTINCT c.city
FROM records.r_competition c
         JOIN records.r_result r ON c.competition_id = r.competition_id
WHERE r.result IN (13, 25, 17, 21.10);

SELECT sportsman_name
FROM records.r_sportsman
WHERE year_of_birth = 1991 AND rank NOT IN ('AA', 'AAA', 'A');

SELECT MAX(result) AS max_result
FROM records.r_result r
         JOIN records.r_competition c ON r.competition_id = c.competition_id
WHERE c.city = 'Winsdor';

UPDATE records.r_sportsman
SET country = 'Russia'
WHERE rank IN ('AAAA', 'AAA') AND country = 'Italy';

UPDATE records.r_sportsman
SET rank = '1'
WHERE sportsman_id IN (
    SELECT s.sportsman_id
    FROM records.r_sportsman s
             JOIN records.r_personal_records pr ON s.sportsman_id = pr.sportsman_id
             JOIN records.r_discipline d ON pr.discipline_id = d.discipline_id
    WHERE pr.personal_record = d.world_record
);


UPDATE records.r_discipline
SET world_record = world_record + 2
WHERE set_date < '2005-03-20';

UPDATE records.r_result
SET result = result - 2
WHERE competition_id IN (
    SELECT competition_id
    FROM records.r_competition
    WHERE hold_date = '2012-05-20'
) AND result >= 45;

SELECT c.competition_name
FROM records.r_competition c
         JOIN records.r_competitions_disciplines cd ON c.competition_id = cd.competition_id
         JOIN records.r_discipline d ON cd.discipline_id = d.discipline_id
WHERE d.world_record = 20.99 AND d.set_date <> '2015-02-12';

DELETE FROM records.r_competition
WHERE competition_id IN (
    SELECT r.competition_id
    FROM records.r_result r
    WHERE r.result = 20
);

SELECT AVG(s.year_of_birth) AS average_age
FROM records.r_sportsman s
WHERE s.sportsman_id IN (
    SELECT DISTINCT r.sportsman_id
    FROM records.r_competition c
             JOIN records.r_result r ON c.competition_id = r.competition_id
    WHERE c.city = 'Winsdor'
);