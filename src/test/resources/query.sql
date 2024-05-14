select name, isbn, year, author, book_category_id, description from books
where id = 26171;

select * from users;
select full_name, email, password, user_group_id, status, start_date, end_date, address from users
where id = 14123;

select count(b.id), count(is_returned), count(user_id) from books b
                                                                left outer join book_borrow bb on b.id = bb.book_id
                                                                right outer join users on bb.user_id = users.id
;

select count(id) from books;

select count(id) from users;

select count(is_returned) from book_borrow
where is_returned = 0;

select id, name from book_categories;

select id from users;

select user_id from book_borrow;

select * from book_borrow;

select bb.id, bb.book_id, bb.user_id, bb.borrowed_date, bb.planned_return_date, bb.returned_date, bb.is_returned, b.name from books b
inner join book_borrow bb
on b.id = bb.book_id
where user_id = 13454;

select id from books;

select is_returned from book_borrow
where book_id = 26780 and user_id = 13729;

select book_id, user_id, returned_date, is_returned from book_borrow;

select *
from book_borrow
where user_id = 5736;

select book_id, is_returned from book_borrow
where book_id = 21794;

select * from user_groups;

select id, group_name from user_groups;

select full_name, email, user_group_id, status, start_date, end_date, address, id from users;