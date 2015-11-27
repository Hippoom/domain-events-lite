create table t_job (
    id VARCHAR(40) not null,
    version INTEGER not null,
    status varchar(50) not null,
    scheduled_at TIMESTAMP not null,
    payload blob,
    CONSTRAINT t_job PRIMARY KEY (id)
);

