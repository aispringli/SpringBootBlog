drop database springbootblog;
create database springbootblog character set utf8;

create database springBootBlog;

use springBootBlog;

create table userRole(
    userRoleId int UNSIGNED,
    userRoleName VARCHAR(50) NOT NULL,
    PRIMARY KEY ( userRoleId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

select * from userRole;

insert into userRole(userRoleId,userRoleName) values(101,'admin');
insert into userRole(userRoleId,userRoleName) values(201,'manger');
insert into userRole(userRoleId,userRoleName) values(301,'user');



create table user(
    userId bigint auto_increment,
    username VARCHAR(20) NOT NULL,
    password char(60) not null,
    userEmail VARCHAR(100) NOT NULL,
    userStatus int default 0,
    userRoleId int default 301,
    userDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userLogoSrc VARCHAR(100)  NULL,
    userMotto VARCHAR(50)  NULL,
    PRIMARY KEY (userId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into user(userName,password,userEmail,userStatus) values('administrator','$2a$10$/udjFoC7X2oWoUTP5.Zt6.ydzzndnagYnOKoUqsULcn52DbbatVj2','3377939225@qq.com',0);

create table userPassword(
    password VARCHAR(60) NOT NULL,
    userId bigint UNSIGNED,
    PRIMARY KEY ( userId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


select user.userId,userName,userEmail,userStatus,userDate,userLogoSrc,passwordId,password
from user left join  userPassword
on user.userId = userPassword.userId

category
create table category(
    categoryId bigint auto_increment,
    categoryName VARCHAR(20) NOT NULL,
    categoryStatus int default 0 not null,
    categoryDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    blogQuantity bigint default 0 NOT NULL,
    userId bigint NOT NULL,
    PRIMARY KEY (categoryId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TRIGGER user_category AFTER INSERT ON user FOR EACH ROW
BEGIN
INSERT INTO category(categoryName,userId) values("默认",NEW.userId);
END;

//触发器
DELIMITER $
create trigger user_category after insert
on user for each row
begin
INSERT INTO category(categoryName,userId) values("默认",NEW.userId);
end$


blog
create table blog(
    blogId bigint auto_increment,
    title VARCHAR(20) NOT NULL,
    summary VARCHAR(200) NULL,
    blogLogo VARCHAR(100) NULL,
    content MEDIUMTEXT null,
    blogDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    blogStatus int default 0 not null,
    starQuantity bigint default 0 NOT NULL,
    collectQuantity bigint default 0 NOT NULL,
    commentQuantity bigint default 0 NOT NULL,
    categoryId bigint not null,
    PRIMARY KEY (blogId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

follow

create table follow(
    followId bigint auto_increment,
    userId bigint not null,
    userFollowId bigint not null,
    followDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (followId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table star(
    starId bigint auto_increment,
    userId bigint not null,
    blogId bigint not null,
    starDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (starId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table collect(
    collectId bigint auto_increment,
    userId bigint not null,
    blogId bigint not null,
    collectDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (collectId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;





