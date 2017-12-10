CREATE SEQUENCE nobts_seq;

CREATE TABLE expenses
(
  debtee         VARCHAR(255),
  expensename    VARCHAR(50)              NOT NULL,
  nobtid         BIGINT                   NOT NULL,
  shares         JSONB,
  splitstrategy  VARCHAR(255),
  createdon      TIMESTAMP WITH TIME ZONE NOT NULL,
  date           DATE                     NOT NULL,
  currency       VARCHAR(3)               NOT NULL,
  conversionrate NUMERIC(20, 10)          NOT NULL,
  id             BIGINT                   NOT NULL
);

CREATE UNIQUE INDEX expenses_pkey
  ON expenses (id, nobtid);

CREATE TABLE nobts
(
  id                   BIGINT                   NOT NULL,
  nobtname             VARCHAR(50)              NOT NULL,
  createdon            TIMESTAMP WITH TIME ZONE NOT NULL,
  currency             VARCHAR(3)               NOT NULL,
  optimizer            VARCHAR(50),
  explicitparticipants JSONB,
  externalid           VARCHAR(20)              NOT NULL
);

CREATE UNIQUE INDEX nobts_pkey
  ON nobts (id);

CREATE UNIQUE INDEX nobts_externalid_key
  ON nobts (externalid);

CREATE TABLE payments
(
  id             BIGINT                   NOT NULL,
  amount         NUMERIC(19, 2)           NOT NULL,
  sender         VARCHAR(255)             NOT NULL,
  recipient      VARCHAR(255)             NOT NULL,
  description    VARCHAR(255),
  nobtid         BIGINT                   NOT NULL,
  createdon      TIMESTAMP WITH TIME ZONE NOT NULL,
  date           DATE                     NOT NULL,
  currency       VARCHAR(3)               NOT NULL,
  conversionrate NUMERIC(20, 10)          NOT NULL
);

CREATE UNIQUE INDEX payments_pkey
  ON payments (id, nobtid);

