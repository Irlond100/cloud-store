
create table user (
                       id int generated by default as identity primary key,
                       login varchar(255),
                       password varchar(255)

);
