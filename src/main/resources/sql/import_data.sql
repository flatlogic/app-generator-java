INSERT INTO public.users (id,"createdAt","deletedAt","updatedAt",disabled,email,"emailVerificationToken","emailVerificationTokenExpiresAt","emailVerified","firstName","importHash","lastName","password","passwordResetToken","passwordResetTokenExpiresAt","phoneNumber",provider,"role","createdById","updatedById") VALUES
	 ('52bc1fb6-0fe5-4647-8cbc-8c55e156b889','2020-11-17 14:32:23.553',NULL,'2020-11-17 14:32:23.553',false,'admin@flatlogic.com',NULL,NULL,true,'Admin',NULL,NULL,'$2b$12$Dtk2u7HCtJydGeljMR5tEeKmVxrO8W0FNoSDXSSBoJHrNU3i9R4ca',NULL,NULL,NULL,'local','ADMIN',NULL,NULL),
	 ('127b7b84-5af0-4e66-93ca-346ac35e1bdd','2020-12-10 17:33:50.152',NULL,NULL,false,'user@flatlogic.com',NULL,NULL,false,'Alex',NULL,'Xela',NULL,NULL,NULL,'+888888888888',NULL,'USER','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL);

INSERT INTO public.categories (id,"createdAt","deletedAt","updatedAt","importHash",title,"createdById","updatedById") VALUES
	 ('d160a5fe-1224-43f4-a05f-cbc60e97f2d5','2020-12-10 17:32:23.846',NULL,NULL,NULL,'cars','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL),
	 ('1f5feed0-b487-4089-8b89-92c09ac4c65d','2020-12-10 17:32:27.908',NULL,NULL,NULL,'clocks','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL),
	 ('28aea915-029f-43ec-914b-28c8a9217027','2020-12-10 17:32:32.119',NULL,NULL,NULL,'tables','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL),
	 ('3ea4c021-9879-4315-b61e-0aac16936732','2020-12-10 17:32:39.702',NULL,NULL,NULL,'computers','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL);

 INSERT INTO public.products (id,"createdAt","deletedAt","updatedAt",description,discount,"importHash",price,rating,status,title,"createdById","updatedById") VALUES
	 ('0f5a5c45-9a75-42a5-986a-4be6fbf5a3d4','2020-12-10 17:34:23.75',NULL,NULL,'description',10.00,NULL,100.00,2,'IN_STOCK','product1','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL),
	 ('6b2d3137-6edd-4201-a1c3-0624df6f704a','2020-12-10 17:34:54.11',NULL,NULL,'desc',10.00,NULL,100.00,2,'OUT_OF_STOCK','product2','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL);

INSERT INTO public.orders (id,"createdAt","deletedAt","updatedAt",amount,"importHash",order_date,status,"createdById","updatedById","productId","userId") VALUES
	 ('8050f33a-1903-4ac7-a7d1-a6c3a77834bd','2020-12-10 17:35:15.799',NULL,NULL,10,NULL,'2020-12-10 00:00:00','IN_CART','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL,'0f5a5c45-9a75-42a5-986a-4be6fbf5a3d4','127b7b84-5af0-4e66-93ca-346ac35e1bdd'),
	 ('6d2dd84a-f17f-4be8-8cb1-255aa88342c3','2020-12-10 17:35:30.893',NULL,NULL,20,NULL,'2020-12-17 00:00:00','BOUGHT','52bc1fb6-0fe5-4647-8cbc-8c55e156b889',NULL,'6b2d3137-6edd-4201-a1c3-0624df6f704a','127b7b84-5af0-4e66-93ca-346ac35e1bdd');

 INSERT INTO public."productsCategoriesCategories" ("productId","categoryId") VALUES
	 ('0f5a5c45-9a75-42a5-986a-4be6fbf5a3d4','d160a5fe-1224-43f4-a05f-cbc60e97f2d5'),
	 ('0f5a5c45-9a75-42a5-986a-4be6fbf5a3d4','3ea4c021-9879-4315-b61e-0aac16936732'),
	 ('6b2d3137-6edd-4201-a1c3-0624df6f704a','d160a5fe-1224-43f4-a05f-cbc60e97f2d5');

 INSERT INTO public."productsMore_productsProducts" ("productId","moreProductId") VALUES
	 ('6b2d3137-6edd-4201-a1c3-0624df6f704a','0f5a5c45-9a75-42a5-986a-4be6fbf5a3d4');