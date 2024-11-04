CREATE TABLE public.users
                        (
                            good_name_man character varying ,
                            surname_with_black_cat character varying ,
                            username_from_mail_ru character varying ,
                            id_chicken bigint NOT NULL,
                            CONSTRAINT users_pkey PRIMARY KEY (id_chicken)
                        );

                        INSERT INTO public.users(
                        	good_name_man, surname_with_black_cat, username_from_mail_ru, id_chicken)
                        	VALUES ('test', 'data', 'data-data', 1);