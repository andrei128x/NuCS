drop table nucs_articles;
/
create table nucs_articles(
		id		number primary key,
		title	varchar2(255) not null,
		link	varchar2(256) not null,
		categ	number(4) null,
		autor	varchar2(128) null,
		pubdate	date not null,
		text	varchar2(1024)
	);
/

create or replace procedure add_rss_data(
		v_title in	nucs_articles.title%type,
		v_link	in	nucs_articles.link%type,
		v_categ	in	nucs_articles.categ%type,
		v_autor	in	nucs_articles.autor%type,
		v_pubdate	in	nucs_articles.pubdate%type,
		v_text	in	nucs_articles.text%type
	) is

	v_next_index nucs_articles.id%type	:=	0;

	cursor c_idx is select id into v_next_index from nucs_articles where rownum = 1 order by id desc;

begin

	for c_rec in c_idx loop
		v_next_index := c_rec.id;
	end loop;
	
	insert into nucs_articles values(
			v_next_index + 1,
			v_title,
			v_link,
			v_categ,
			v_autor,
			v_pubdate,
			v_text
		);
	
	
end;
/
