drop table		nucs_articles;
drop table    nucs_cat_list;
drop table		nucs_categories;
drop table		nucs_users;

drop sequence nucs_id_generator;
create sequence nucs_id_generator	start with 1 increment by	1;


create table nucs_articles(
		art_id		number primary key,
		title	varchar2(512) not null,
		link	varchar2(512) not null,
		cat_list_id	number null,
		autor	varchar2(128) null,
		pubdate	date not null,
		text	varchar2(2048)
	);
/

create table nucs_categories(
	cat_id		number primary key,
	cat_name	varchar(128) not null
);
/

create table nucs_cat_list(
	art_id	number,
	cat_id	number
);
/

create table nucs_users(
	user_id		number primary key,
	user_name	varchar(32),
	pass		varchar(32) not null
);
/

create or replace function nucs_get_index return number is
begin
	return( nucs_id_generator.nextval);
end;
/

create or replace type nucs_categ_list_type as table of varchar2(128);
/

create or replace procedure nucs_sync_categories(
    v_art_id in nucs_articles.art_id%type,
    v_cat_list in nucs_categ_list_type
  )
  as
  v_next_cat_index  nucs_cat_list.cat_id%type;
begin
	for k in 1..v_cat_list.count loop
		dbms_output.put_line(v_cat_list(k));
    
		if(v_cat_list(k) is not null) then
    
      v_next_cat_index := nucs_get_index;
    
      insert into nucs_categories values(v_next_cat_index, v_cat_list(k));
      insert into nucs_cat_list values(v_art_id, v_next_cat_index);
    
    end if;  
		
	end loop;
end;
/

create or replace procedure add_rss_data(
		v_title in	nucs_articles.title%type,
		v_link	in	nucs_articles.link%type,
		v_cat_list	in nucs_categ_list_type,
		v_autor	in	nucs_articles.autor%type,
		v_pubdate	in	nucs_articles.pubdate%type,
		v_text	in	nucs_articles.text%type
	) is

	v_next_art_index number;

begin

	v_next_art_index := nucs_get_index;
	
	nucs_sync_categories(v_next_art_index, v_cat_list);
	
	insert into nucs_articles values(
			v_next_art_index,
			v_title,
			v_link,
			10,
			v_autor,
			v_pubdate,
			v_text
		);
		
	commit;
	
end;
/

commit
/