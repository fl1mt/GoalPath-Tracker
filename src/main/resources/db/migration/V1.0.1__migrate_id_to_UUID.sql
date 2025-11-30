CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER TABLE subtasks DROP CONSTRAINT IF EXISTS fk_subtask_task;
ALTER TABLE subtasks DROP CONSTRAINT IF EXISTS fksvs126nsj9ohhvwjog5ddp76x; -- Пример непредсказуемого имени
ALTER TABLE subtasks DROP CONSTRAINT IF EXISTS subtasks_task_id_fkey; -- Типичное авто-имя

ALTER TABLE tasks DROP CONSTRAINT IF EXISTS fk_task_goal;
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS fkacjm73w3vfy4x7gj42hd1ltkb; -- Имя, вызвавшее ошибку
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS tasks_goal_id_fkey; -- Типичное авто-имя



ALTER TABLE goals ADD COLUMN new_id UUID;
UPDATE goals SET new_id = uuid_generate_v4();
ALTER TABLE goals ALTER COLUMN new_id SET NOT NULL;

ALTER TABLE tasks ADD COLUMN new_id UUID;
ALTER TABLE tasks ADD COLUMN new_goal_id UUID;

UPDATE tasks SET new_id = uuid_generate_v4();
UPDATE tasks SET new_goal_id = goals.new_id
    FROM goals
WHERE goals.id = tasks.goal_id;

ALTER TABLE tasks ALTER COLUMN new_id SET NOT NULL;
ALTER TABLE tasks ALTER COLUMN new_goal_id SET NOT NULL;


ALTER TABLE subtasks ADD COLUMN new_id UUID;
ALTER TABLE subtasks ADD COLUMN new_task_id UUID;

UPDATE subtasks SET new_id = uuid_generate_v4();
UPDATE subtasks SET new_task_id = tasks.new_id
    FROM tasks
WHERE tasks.id = subtasks.task_id;

ALTER TABLE subtasks ALTER COLUMN new_id SET NOT NULL;
ALTER TABLE subtasks ALTER COLUMN new_task_id SET NOT NULL;



ALTER TABLE goals DROP COLUMN id;
ALTER TABLE goals RENAME COLUMN new_id TO id;
ALTER TABLE goals ADD PRIMARY KEY (id);


ALTER TABLE tasks DROP COLUMN id;
ALTER TABLE tasks RENAME COLUMN new_id TO id;
ALTER TABLE tasks ADD PRIMARY KEY (id);

ALTER TABLE tasks DROP COLUMN goal_id;
ALTER TABLE tasks RENAME COLUMN new_goal_id TO goal_id;


ALTER TABLE subtasks DROP COLUMN id;
ALTER TABLE subtasks RENAME COLUMN new_id TO id;
ALTER TABLE subtasks ADD PRIMARY KEY (id);

ALTER TABLE subtasks DROP COLUMN task_id;
ALTER TABLE subtasks RENAME COLUMN new_task_id TO task_id;



ALTER TABLE tasks
    ADD CONSTRAINT fk_task_goal_uuid
        FOREIGN KEY (goal_id)
            REFERENCES goals (id);

ALTER TABLE subtasks
    ADD CONSTRAINT fk_subtask_task_uuid
        FOREIGN KEY (task_id)
            REFERENCES tasks (id);