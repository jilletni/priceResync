create table product(
	id bigint auto_increment not null,
	model varchar(100) not null,
	seller varchar(50) not null,
	prdId varchar(100),
	name varchar(300),
	expected decimal(9,2),
	lastPrice decimal(9,2),
	isActive varchar(1) default 'Y' not null,
	subscribers varchar(1000),
	prdUrl varchar(2000)
);

create table ptrend(
	id bigint auto_increment not null,
	pId bigint,
	price decimal(9,2),
	datetime timestamp default CURRENT_TIMESTAMP
);

create table amazoneProduct(
	id bigint auto_increment not null,
	pId bigint,
	price decimal(9,2),
	datetime timestamp default CURRENT_TIMESTAMP
);