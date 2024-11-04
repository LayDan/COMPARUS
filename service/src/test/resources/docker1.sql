  CREATE TABLE public.users_more
                        (
                            test_id character varying NOT NULL,
                            nano_name character varying ,
                            username_super character varying ,
                            surname_with_bao character varying ,
                            CONSTRAINT users_more_pkey PRIMARY KEY (test_id)
                        );

                        INSERT INTO public.users_more(
                        	test_id, nano_name, username_super, surname_with_bao)
                        	VALUES ('dsadasd', 'dasdasd', '23213', '3sdasda');