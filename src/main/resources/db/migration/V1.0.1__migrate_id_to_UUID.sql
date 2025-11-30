-- Flyway Migration: V1.0.1__migrate_id_to_UUID.sql

-- УСТАНОВКА ДОПОЛНЕНИЯ UUID (если еще не установлено)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--------------------------------------------------------------------------------
-- ЧАСТЬ 1: УДАЛЕНИЕ СУЩЕСТВУЮЩИХ ОГРАНИЧЕНИЙ И ИНДЕКСОВ
-- Используем DROP CONSTRAINT IF EXISTS, чтобы скрипт не падал, если имя FK изменилось.
--------------------------------------------------------------------------------

-- 1.1 Удаление FK из таблицы subtasks
ALTER TABLE subtasks DROP CONSTRAINT IF EXISTS fk_subtask_task;
ALTER TABLE subtasks DROP CONSTRAINT IF EXISTS fksvs126nsj9ohhvwjog5ddp76x; -- Пример непредсказуемого имени
-- Если был FK на tasks.id:
ALTER TABLE subtasks DROP CONSTRAINT IF EXISTS subtasks_task_id_fkey; -- Типичное авто-имя

-- 1.2 Удаление FK из таблицы tasks
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS fk_task_goal;
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS fkacjm73w3vfy4x7gj42hd1ltkb; -- Имя, вызвавшее ошибку
-- Если был FK на goals.id:
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS tasks_goal_id_fkey; -- Типичное авто-имя

-- 1.3 Удаление FK с Hibernate-сгенерированными именами (если применимо)
-- В зависимости от того, как Hibernate генерировал FK, могут быть другие имена.
-- Если знаете другие конкретные имена, добавьте их сюда с IF EXISTS.


--------------------------------------------------------------------------------
-- ЧАСТЬ 2: ДОБАВЛЕНИЕ НОВЫХ UUID-КОЛОНОК И КОПИРОВАНИЕ ДАННЫХ
--------------------------------------------------------------------------------

-- 2.1 Таблица goals
ALTER TABLE goals ADD COLUMN new_id UUID;
UPDATE goals SET new_id = uuid_generate_v4();
ALTER TABLE goals ALTER COLUMN new_id SET NOT NULL;

-- 2.2 Таблица tasks
ALTER TABLE tasks ADD COLUMN new_id UUID;
ALTER TABLE tasks ADD COLUMN new_goal_id UUID;

-- Копирование данных для tasks.id и tasks.goal_id
UPDATE tasks SET new_id = uuid_generate_v4();
UPDATE tasks SET new_goal_id = goals.new_id
    FROM goals
WHERE goals.id = tasks.goal_id;

ALTER TABLE tasks ALTER COLUMN new_id SET NOT NULL;
ALTER TABLE tasks ALTER COLUMN new_goal_id SET NOT NULL;


-- 2.3 Таблица subtasks
ALTER TABLE subtasks ADD COLUMN new_id UUID;
ALTER TABLE subtasks ADD COLUMN new_task_id UUID;

-- Копирование данных для subtasks.id и subtasks.task_id
UPDATE subtasks SET new_id = uuid_generate_v4();
UPDATE subtasks SET new_task_id = tasks.new_id
    FROM tasks
WHERE tasks.id = subtasks.task_id;

ALTER TABLE subtasks ALTER COLUMN new_id SET NOT NULL;
ALTER TABLE subtasks ALTER COLUMN new_task_id SET NOT NULL;


--------------------------------------------------------------------------------
-- ЧАСТЬ 3: ОЧИСТКА И ПЕРЕИМЕНОВАНИЕ
--------------------------------------------------------------------------------

-- 3.1 Таблица goals
ALTER TABLE goals DROP COLUMN id;
ALTER TABLE goals RENAME COLUMN new_id TO id;
ALTER TABLE goals ADD PRIMARY KEY (id);

-- 3.2 Таблица tasks
ALTER TABLE tasks DROP COLUMN id;
ALTER TABLE tasks RENAME COLUMN new_id TO id;
ALTER TABLE tasks ADD PRIMARY KEY (id);

ALTER TABLE tasks DROP COLUMN goal_id;
ALTER TABLE tasks RENAME COLUMN new_goal_id TO goal_id;

-- 3.3 Таблица subtasks
ALTER TABLE subtasks DROP COLUMN id;
ALTER TABLE subtasks RENAME COLUMN new_id TO id;
ALTER TABLE subtasks ADD PRIMARY KEY (id);

ALTER TABLE subtasks DROP COLUMN task_id;
ALTER TABLE subtasks RENAME COLUMN new_task_id TO task_id;


--------------------------------------------------------------------------------
-- ЧАСТЬ 4: ВОССТАНОВЛЕНИЕ ВНЕШНИХ КЛЮЧЕЙ (FK) С НОВЫМ ТИПОМ UUID
--------------------------------------------------------------------------------

-- 4.1 Восстановление FK в таблице tasks (goal_id -> goals.id)
ALTER TABLE tasks
    ADD CONSTRAINT fk_task_goal_uuid
        FOREIGN KEY (goal_id)
            REFERENCES goals (id);

-- 4.2 Восстановление FK в таблице subtasks (task_id -> tasks.id)
ALTER TABLE subtasks
    ADD CONSTRAINT fk_subtask_task_uuid
        FOREIGN KEY (task_id)
            REFERENCES tasks (id);