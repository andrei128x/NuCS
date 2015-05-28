drop table	nucs_articles;
drop table	nucs_cat_list;
drop table	nucs_categories;
drop table	nucs_users;

drop table	nucs_security_logs;

drop sequence nucs_id_generator;
create sequence nucs_id_generator	start with 1 increment by	1;

drop trigger nucs_trig_suspicios_log;

create table nucs_articles(
		art_id	number primary key,
		title	varchar2(512) not null,
		link	varchar2(512) not null,
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

create table nucs_security_logs(
	art_id		number,
	trig_date	date
)
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
		v_autor	in	nucs_articles.autor%type,
		v_pubdate	in	varchar2,
		v_text	in	nucs_articles.text%type,
    v_cat_list	in nucs_categ_list_type
	) is

	v_next_art_index number;

begin

	v_next_art_index := nucs_get_index;
	
	nucs_sync_categories(v_next_art_index, v_cat_list);
	
	insert into nucs_articles values(
			v_next_art_index,
			v_title,
			v_link,
			v_autor,
			sysdate,
			v_text
		);
		
	commit;
	
end;
/


create trigger nucs_trig_suspicios_log
	after insert or update of title on nucs_articles
      when(old.title like '%hack' )
begin
	insert into nucs_security_logs values( :new.art_id, sysdate );
end;
/

create or replace view nucs_view_categories
as
	select cat_name from nucs_categories; 
/

create or replace view nucs_view_logs
as
	select art_id from nucs_security_logs;
/

commit
/