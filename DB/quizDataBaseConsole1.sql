create database if not exists QuizDataBase;

USE QuizDataBase;

create table if not exists Difficulty(
    DifficultyID int auto_increment unique PRIMARY KEY ,
    DifficultyName varchar(30)
);

INSERT INTO Difficulty (DifficultyName) values ('Easy'), ('Medium'), ('Hard');

create table if not exists Type(
    TypeID int auto_increment unique PRIMARY KEY ,
    TypeName varchar(30)
);

INSERT INTO Type (TypeName) values ('Multiple'), ('Boolean');

create table if not exists Category(
    CategoryID int auto_increment unique PRIMARY KEY ,
    CategoryName varchar(60)
);

INSERT INTO Category(CategoryName) VALUES
('General Knowledge'),
('Entertainment: Books'),
('Entertainment: Film'),
('Entertainment: Music'),
('Entertainment: Musicals & Theatres'),
('Entertainment: Television'),
('Entertainment: Video Games'),
('Entertainment: Board Games'),
('Science & Nature'),
('Science: Computers'),
('Science: Mathematics'),
('Mythology'),
('Sports'),
('Geography'),
('History'),
('Politics'),
('Art'),
('Celebrities'),
('Animals'),
('Vehicles'),
('Entertainment: Comics'),
('Science: Gadgets'),
('Entertainment: Japanese Anime & Manga'),
('Entertainment: Cartoon & Animations');

create table if not exists CorrectAnswers(
    CorrectAnswerID int auto_increment unique PRIMARY KEY ,
      CorrectAnswerName varchar(60)
);

CREATE TABLE IF NOT EXISTS Question(
    QuestionID INT AUTO_INCREMENT PRIMARY KEY,
    TypeID INT,
    DifficultyID INT,
    CategoryID INT,
    QuestionTitle VARCHAR(100),
    CorrectAnswerID INT,
    FOREIGN KEY (TypeID) REFERENCES Type(TypeID),
    FOREIGN KEY (DifficultyID) REFERENCES Difficulty(DifficultyID),
    FOREIGN KEY (CorrectAnswerID) REFERENCES CorrectAnswers(CorrectAnswerID),
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

CREATE TABLE IF NOT EXISTS WrongAnswers(
    WrongAnswersID INT AUTO_INCREMENT,
    WrongAnswerName VARCHAR(60),
    QuestionID INT,
    PRIMARY KEY (WrongAnswersID),
    FOREIGN KEY (QuestionID) REFERENCES Question(QuestionID)
);

select * from category;


