INSERT INTO students (name, kana_name, nickname, email, area, age, sex)
VALUES
('山田太郎', 'ヤマダ タロウ', 'タロー', 'taro@example.com', '東京', 20, '男性'),
('鈴木花子', 'スズキ ハナコ', 'ハナ', 'hanako@example.com', '大阪', 22, '女性'),
('田中健太', 'タナカ ケンタ', 'ケン', 'kenta@example.com', '愛知', 21, '男性'),
('斉藤由依', 'サイトウ ユイ', 'ユイ', 'yui@example.com', '京都', 19, '女性'),
('小宮祥太', 'コミヤ ショウタ', 'コミー', 'shota@example.com', '福岡', 23, '男性');

INSERT INTO students_courses (student_id, course_name, course_start_at, course_end_at)
VALUES
(5, 'Java', '2025-01-07', '2026-06-30'),
(4, 'English', '2024-12-01', '2025-12-31'),
(2, 'Photo', '2024-10-18', '2025-03-31'),
(1, 'WebDesign', '2024-04-20', '2025-09-30'),
(3, 'Illustrator', '2024-11-11', '2025-10-31'),
(1, 'Java', '2025-02-07', '2026-07-31'),
(3, 'English', '2024-11-01', '2026-12-31'),
(1, 'Photo', '2024-12-18', '2026-01-31'),
(4, 'WebDesign', '2024-06-20', '2025-11-30'),
(5, 'Marketing', '2024-08-11', '2025-06-30');
