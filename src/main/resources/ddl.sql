create table vertices
(
	id bigint not null
		constraint vertices_pkey
			primary key,
	parent_id bigint
		constraint fkcju1dqx5pygqly6eko835ktcs
			references vertices
)
;