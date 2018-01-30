create table graphs
(
	id bigint not null
		constraint graphs_pkey
			primary key,
	parent_id bigint
		constraint fkcju1dqx5pygqly6eko835ktcs
			references graphs
)
;