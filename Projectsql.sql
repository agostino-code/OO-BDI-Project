PGDMP                          z            Project    14.1    14.1 c    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16398    Project    DATABASE     e   CREATE DATABASE "Project" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "Project";
                postgres    false            �           0    0    DATABASE "Project"    COMMENT     P   COMMENT ON DATABASE "Project" IS 'Database per il Proggetto di Basi di Dati I';
                   postgres    false    3467            v           1247    16718    sesso    DOMAIN     }   CREATE DOMAIN public.sesso AS character(1)
	CONSTRAINT sesso_check CHECK (((VALUE = 'M'::bpchar) OR (VALUE = 'F'::bpchar)));
    DROP DOMAIN public.sesso;
       public          postgres    false            �            1255    17028    defaultvalueDescrizione()    FUNCTION     �   CREATE FUNCTION public."defaultvalueDescrizione"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
IF NEW."descrizione" is null THEN
NEW."descrizione"='Nessuna Descrizione';
END IF;
return NEW;
END;
$$;
 2   DROP FUNCTION public."defaultvalueDescrizione"();
       public          postgres    false            �            1255    16965    generatecodCorso()    FUNCTION     Z  CREATE FUNCTION public."generatecodCorso"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
    LOOP
            NEW."codCorso" =('C' || CAST(nextval('seqcorso') AS char));
            if NOT EXISTS (select "codCorso" FROM "Corso" WHERE "codCorso"= new."codCorso" ) then
                RETURN NEW;
            end if;
    END LOOP;

END;$$;
 +   DROP FUNCTION public."generatecodCorso"();
       public          postgres    false            �            1255    16907    generatecodGestore()    FUNCTION     i  CREATE FUNCTION public."generatecodGestore"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
    LOOP
            NEW."codGestore" =('G' || CAST(nextval('seqgestore') AS char));
            if NOT EXISTS (select "codGestore" FROM "Gestore" WHERE "codGestore"= new."codGestore" ) then
                RETURN NEW;
            end if;
    END LOOP;

END;
$$;
 -   DROP FUNCTION public."generatecodGestore"();
       public          postgres    false            �            1255    16953    generatecodLezione()    FUNCTION     h  CREATE FUNCTION public."generatecodLezione"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
    LOOP
            NEW."codLezione" =('L' || CAST(nextval('seqlezione') AS char));
            if NOT EXISTS (select "codLezione" FROM "Lezione" WHERE "codLezione"= new."codLezione" ) then
                RETURN NEW;
            end if;
    END LOOP;

END;$$;
 -   DROP FUNCTION public."generatecodLezione"();
       public          postgres    false            �            1255    16952    generatecodOperatore()    FUNCTION     v  CREATE FUNCTION public."generatecodOperatore"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
    LOOP
            NEW."codOperatore" =('O' || CAST(nextval('seqoperatore') AS char));
            if NOT EXISTS (select "codOperatore" FROM "Operatore" WHERE "codOperatore"= new."codOperatore" ) then
                RETURN NEW;
            end if;
    END LOOP;

END;$$;
 /   DROP FUNCTION public."generatecodOperatore"();
       public          postgres    false            �            1255    16784    generatecodStudente()    FUNCTION     p  CREATE FUNCTION public."generatecodStudente"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
    LOOP
            NEW."codStudente" =('S' || CAST(nextval('seqstudente') AS char));
            if NOT EXISTS (select "codStudente" FROM "Studente" WHERE "codStudente"= new."codStudente" ) then
                RETURN NEW;
            end if;
    END LOOP;

END;
$$;
 .   DROP FUNCTION public."generatecodStudente"();
       public          postgres    false            �            1255    16954    generazionecodAreaTematica()    FUNCTION     �  CREATE FUNCTION public."generazionecodAreaTematica"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
    LOOP
            NEW."codAreaTematica" =('A' || CAST(nextval('seqareatematica') AS char));
            if NOT EXISTS (select "codAreaTematica" FROM "AreaTematica" WHERE "codAreaTematica"= new."codAreaTematica" ) then
                RETURN NEW;
            end if;
    END LOOP;

END;$$;
 5   DROP FUNCTION public."generazionecodAreaTematica"();
       public          postgres    false            �            1255    16951    resetseqAreaTematica()    FUNCTION     �   CREATE FUNCTION public."resetseqAreaTematica"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN 
ALTER SEQUENCE seqareatematica
RESTART WITH 0;
RETURN NEW;
END;$$;
 /   DROP FUNCTION public."resetseqAreaTematica"();
       public          postgres    false            �            1255    16961    resetseqCorso()    FUNCTION     �   CREATE FUNCTION public."resetseqCorso"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN 
ALTER SEQUENCE seqcorso
RESTART WITH 0;
RETURN NEW;
END;$$;
 (   DROP FUNCTION public."resetseqCorso"();
       public          postgres    false            �            1255    16905    resetseqGestore()    FUNCTION     �   CREATE FUNCTION public."resetseqGestore"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN 
ALTER SEQUENCE seqgestore
RESTART WITH 0;
RETURN NEW;
END;$$;
 *   DROP FUNCTION public."resetseqGestore"();
       public          postgres    false            �            1255    16950    resetseqLezione()    FUNCTION     �   CREATE FUNCTION public."resetseqLezione"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN 
ALTER SEQUENCE seqlezione
RESTART WITH 0;
RETURN NEW;
END;$$;
 *   DROP FUNCTION public."resetseqLezione"();
       public          postgres    false            �            1255    16949    resetseqOperatore()    FUNCTION     �   CREATE FUNCTION public."resetseqOperatore"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN 
ALTER SEQUENCE seqoperatore
RESTART WITH 0;
RETURN NEW;
END;$$;
 ,   DROP FUNCTION public."resetseqOperatore"();
       public          postgres    false            �            1255    16821    resetseqStudente()    FUNCTION     �   CREATE FUNCTION public."resetseqStudente"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN 
ALTER SEQUENCE seqstudente
RESTART WITH 0;
RETURN NEW;
END;$$;
 +   DROP FUNCTION public."resetseqStudente"();
       public          postgres    false            �            1259    16746 
   Appartiene    TABLE     x   CREATE TABLE public."Appartiene" (
    "codAreaTematica" character(8) NOT NULL,
    "codCorso" character(8) NOT NULL
);
     DROP TABLE public."Appartiene";
       public         heap    postgres    false            �            1259    16741    AreaTematica    TABLE     }   CREATE TABLE public."AreaTematica" (
    tipo character varying(20) NOT NULL,
    "codAreaTematica" character(8) NOT NULL
);
 "   DROP TABLE public."AreaTematica";
       public         heap    postgres    false            �            1259    16435    Autenticazione    TABLE     �   CREATE TABLE public."Autenticazione" (
    email character varying(60) NOT NULL,
    password character varying(16) NOT NULL
);
 $   DROP TABLE public."Autenticazione";
       public         heap    postgres    false            �            1259    16424    Corso    TABLE     D  CREATE TABLE public."Corso" (
    titolo character varying(20) NOT NULL,
    descrizione character varying(200) DEFAULT 'Nessuna descrizione'::character varying,
    "iscrizioniMassime" integer NOT NULL,
    "tassoPresenzMinime" real NOT NULL,
    "codCorso" character(8) NOT NULL,
    "codGestore" character(8) NOT NULL
);
    DROP TABLE public."Corso";
       public         heap    postgres    false            �            1259    16405    Gestore    TABLE     #  CREATE TABLE public."Gestore" (
    nome character varying(30) NOT NULL,
    descrizione character varying(200) DEFAULT 'Nessuna descrizione'::character varying,
    telefono character varying(15) NOT NULL,
    "codGestore" character(8) NOT NULL,
    email character varying(60) NOT NULL
);
    DROP TABLE public."Gestore";
       public         heap    postgres    false            �            1259    16532    Iscritti    TABLE     �   CREATE TABLE public."Iscritti" (
    "codCorso" character(8) NOT NULL,
    "codStudente" character(8) NOT NULL,
    "Idoneo" boolean
);
    DROP TABLE public."Iscritti";
       public         heap    postgres    false            �            1259    16550    Lezione    TABLE     `  CREATE TABLE public."Lezione" (
    titolo character varying(30) NOT NULL,
    descrizione character varying(200) DEFAULT 'Nessuna descrizione'::character varying,
    "dataoraInizio" timestamp(6) without time zone NOT NULL,
    durata time(6) without time zone NOT NULL,
    "codLezione" character(8) NOT NULL,
    "codCorso" character(8) NOT NULL
);
    DROP TABLE public."Lezione";
       public         heap    postgres    false            �            1259    16460 	   Operatore    TABLE     �   CREATE TABLE public."Operatore" (
    "codOperatore" character varying(16) NOT NULL,
    "codiceFiscale" character varying(16) NOT NULL
);
    DROP TABLE public."Operatore";
       public         heap    postgres    false            �            1259    16728    Prenotazioni    TABLE     x   CREATE TABLE public."Prenotazioni" (
    "codLezione" character(8) NOT NULL,
    "codStudente" character(8) NOT NULL
);
 "   DROP TABLE public."Prenotazioni";
       public         heap    postgres    false            �            1259    16414    Sede    TABLE     �   CREATE TABLE public."Sede" (
    "città" character varying(30) NOT NULL,
    via character varying(30) NOT NULL,
    civico character varying(5) NOT NULL,
    provincia character varying(30) NOT NULL,
    "codGestore" character(8) NOT NULL
);
    DROP TABLE public."Sede";
       public         heap    postgres    false            �            1259    16574    Statistiche    TABLE     �   CREATE TABLE public."Statistiche" (
    "presenzeMinime" integer NOT NULL,
    "presenzeMassime" integer NOT NULL,
    "presenzeMedie" integer NOT NULL,
    "percentualeRempimento" real NOT NULL,
    "codCorso" character(8) NOT NULL
);
 !   DROP TABLE public."Statistiche";
       public         heap    postgres    false            �            1259    16561    Studente    TABLE     �   CREATE TABLE public."Studente" (
    "codStudente" character varying(8) NOT NULL,
    "codiceFiscale" character varying(16) NOT NULL
);
    DROP TABLE public."Studente";
       public         heap    postgres    false            �            1259    16450    Utente    TABLE     L  CREATE TABLE public."Utente" (
    email character varying(60) NOT NULL,
    nome character varying(15) NOT NULL,
    cognome character varying(15) NOT NULL,
    "dataNascita" date NOT NULL,
    "comunediNascita" character varying(30) NOT NULL,
    sesso public.sesso NOT NULL,
    "codiceFiscale" character varying(16) NOT NULL
);
    DROP TABLE public."Utente";
       public         heap    postgres    false    886            �            1259    17005    logingestore    VIEW       CREATE VIEW public.logingestore AS
 SELECT "Autenticazione".email,
    "Autenticazione".password,
    "Gestore".nome,
    "Gestore".descrizione,
    "Gestore".telefono,
    "Gestore"."codGestore"
   FROM (public."Autenticazione"
     JOIN public."Gestore" USING (email));
    DROP VIEW public.logingestore;
       public          postgres    false    209    209    212    209    212    209    209            �            1259    16985    loginutente    VIEW     B  CREATE VIEW public.loginutente AS
 SELECT "Autenticazione".email,
    "Autenticazione".password,
    "Utente".nome,
    "Utente".cognome,
    "Utente"."dataNascita",
    "Utente"."comunediNascita",
    "Utente".sesso,
    "Utente"."codiceFiscale"
   FROM (public."Autenticazione"
     JOIN public."Utente" USING (email));
    DROP VIEW public.loginutente;
       public          postgres    false    212    213    213    213    213    213    213    213    212    886            �            1259    16948    seqareatematica    SEQUENCE     |   CREATE SEQUENCE public.seqareatematica
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999
    CACHE 1;
 &   DROP SEQUENCE public.seqareatematica;
       public          postgres    false            �            1259    16960    seqcorso    SEQUENCE     u   CREATE SEQUENCE public.seqcorso
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999
    CACHE 1;
    DROP SEQUENCE public.seqcorso;
       public          postgres    false            �            1259    16903 
   seqgestore    SEQUENCE     w   CREATE SEQUENCE public.seqgestore
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999
    CACHE 1;
 !   DROP SEQUENCE public.seqgestore;
       public          postgres    false            �            1259    16947 
   seqlezione    SEQUENCE     w   CREATE SEQUENCE public.seqlezione
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999
    CACHE 1;
 !   DROP SEQUENCE public.seqlezione;
       public          postgres    false            �            1259    16946    seqoperatore    SEQUENCE     y   CREATE SEQUENCE public.seqoperatore
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999
    CACHE 1;
 #   DROP SEQUENCE public.seqoperatore;
       public          postgres    false            �            1259    16776    seqstudente    SEQUENCE     x   CREATE SEQUENCE public.seqstudente
    START WITH 0
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999
    CACHE 1;
 "   DROP SEQUENCE public.seqstudente;
       public          postgres    false            �            1259    16924    studentiscrittiacorsi    VIEW     �  CREATE VIEW public.studentiscrittiacorsi AS
 SELECT "codStudente",
    "Utente"."codiceFiscale",
    "Utente".email,
    "Utente".nome,
    "Utente".cognome,
    "Utente"."dataNascita",
    "Utente"."comunediNascita",
    "Utente".sesso,
    "Iscritti"."codCorso",
    "Iscritti"."Idoneo"
   FROM ((public."Utente"
     JOIN public."Studente" USING ("codiceFiscale"))
     JOIN public."Iscritti" USING ("codStudente"));
 (   DROP VIEW public.studentiscrittiacorsi;
       public          postgres    false    215    217    217    215    215    213    213    213    213    213    213    213    886                      0    16746 
   Appartiene 
   TABLE DATA           E   COPY public."Appartiene" ("codAreaTematica", "codCorso") FROM stdin;
    public          postgres    false    221   ��       ~          0    16741    AreaTematica 
   TABLE DATA           A   COPY public."AreaTematica" (tipo, "codAreaTematica") FROM stdin;
    public          postgres    false    220   �       v          0    16435    Autenticazione 
   TABLE DATA           ;   COPY public."Autenticazione" (email, password) FROM stdin;
    public          postgres    false    212   5�       u          0    16424    Corso 
   TABLE DATA           {   COPY public."Corso" (titolo, descrizione, "iscrizioniMassime", "tassoPresenzMinime", "codCorso", "codGestore") FROM stdin;
    public          postgres    false    211   ��       s          0    16405    Gestore 
   TABLE DATA           U   COPY public."Gestore" (nome, descrizione, telefono, "codGestore", email) FROM stdin;
    public          postgres    false    209   ��       y          0    16532    Iscritti 
   TABLE DATA           I   COPY public."Iscritti" ("codCorso", "codStudente", "Idoneo") FROM stdin;
    public          postgres    false    215   
�       z          0    16550    Lezione 
   TABLE DATA           k   COPY public."Lezione" (titolo, descrizione, "dataoraInizio", durata, "codLezione", "codCorso") FROM stdin;
    public          postgres    false    216   '�       x          0    16460 	   Operatore 
   TABLE DATA           F   COPY public."Operatore" ("codOperatore", "codiceFiscale") FROM stdin;
    public          postgres    false    214   D�       }          0    16728    Prenotazioni 
   TABLE DATA           E   COPY public."Prenotazioni" ("codLezione", "codStudente") FROM stdin;
    public          postgres    false    219   a�       t          0    16414    Sede 
   TABLE DATA           P   COPY public."Sede" ("città", via, civico, provincia, "codGestore") FROM stdin;
    public          postgres    false    210   ~�       |          0    16574    Statistiche 
   TABLE DATA           �   COPY public."Statistiche" ("presenzeMinime", "presenzeMassime", "presenzeMedie", "percentualeRempimento", "codCorso") FROM stdin;
    public          postgres    false    218   ��       {          0    16561    Studente 
   TABLE DATA           D   COPY public."Studente" ("codStudente", "codiceFiscale") FROM stdin;
    public          postgres    false    217   ҇       w          0    16450    Utente 
   TABLE DATA           r   COPY public."Utente" (email, nome, cognome, "dataNascita", "comunediNascita", sesso, "codiceFiscale") FROM stdin;
    public          postgres    false    213   �       �           0    0    seqareatematica    SEQUENCE SET     >   SELECT pg_catalog.setval('public.seqareatematica', 0, false);
          public          postgres    false    227            �           0    0    seqcorso    SEQUENCE SET     7   SELECT pg_catalog.setval('public.seqcorso', 0, false);
          public          postgres    false    228            �           0    0 
   seqgestore    SEQUENCE SET     8   SELECT pg_catalog.setval('public.seqgestore', 0, true);
          public          postgres    false    223            �           0    0 
   seqlezione    SEQUENCE SET     9   SELECT pg_catalog.setval('public.seqlezione', 0, false);
          public          postgres    false    226            �           0    0    seqoperatore    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.seqoperatore', 0, false);
          public          postgres    false    225            �           0    0    seqstudente    SEQUENCE SET     :   SELECT pg_catalog.setval('public.seqstudente', 0, false);
          public          postgres    false    222            �           2606    16745    AreaTematica areatematica_pkey 
   CONSTRAINT     m   ALTER TABLE ONLY public."AreaTematica"
    ADD CONSTRAINT areatematica_pkey PRIMARY KEY ("codAreaTematica");
 J   ALTER TABLE ONLY public."AreaTematica" DROP CONSTRAINT areatematica_pkey;
       public            postgres    false    220            �           2606    16760 "   Autenticazione autenticazione_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY public."Autenticazione"
    ADD CONSTRAINT autenticazione_pkey PRIMARY KEY (email);
 N   ALTER TABLE ONLY public."Autenticazione" DROP CONSTRAINT autenticazione_pkey;
       public            postgres    false    212            �           2606    16632    Corso corso_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public."Corso"
    ADD CONSTRAINT corso_pkey PRIMARY KEY ("codCorso");
 <   ALTER TABLE ONLY public."Corso" DROP CONSTRAINT corso_pkey;
       public            postgres    false    211            �           2606    16494    Gestore gestore_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public."Gestore"
    ADD CONSTRAINT gestore_pkey PRIMARY KEY ("codGestore");
 @   ALTER TABLE ONLY public."Gestore" DROP CONSTRAINT gestore_pkey;
       public            postgres    false    209            �           2606    16555    Lezione lezione_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public."Lezione"
    ADD CONSTRAINT lezione_pkey PRIMARY KEY ("codLezione");
 @   ALTER TABLE ONLY public."Lezione" DROP CONSTRAINT lezione_pkey;
       public            postgres    false    216            �           2606    16679    Operatore operatore_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY public."Operatore"
    ADD CONSTRAINT operatore_pkey PRIMARY KEY ("codOperatore");
 D   ALTER TABLE ONLY public."Operatore" DROP CONSTRAINT operatore_pkey;
       public            postgres    false    214            �           2606    16799    Studente studente_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY public."Studente"
    ADD CONSTRAINT studente_pkey PRIMARY KEY ("codStudente");
 B   ALTER TABLE ONLY public."Studente" DROP CONSTRAINT studente_pkey;
       public            postgres    false    217            �           2606    16945 '   Operatore unique_codiceFiscaleOperatore 
   CONSTRAINT     q   ALTER TABLE ONLY public."Operatore"
    ADD CONSTRAINT "unique_codiceFiscaleOperatore" UNIQUE ("codiceFiscale");
 U   ALTER TABLE ONLY public."Operatore" DROP CONSTRAINT "unique_codiceFiscaleOperatore";
       public            postgres    false    214            �           2606    16941 %   Studente unique_codiceFiscaleStudente 
   CONSTRAINT     o   ALTER TABLE ONLY public."Studente"
    ADD CONSTRAINT "unique_codiceFiscaleStudente" UNIQUE ("codiceFiscale");
 S   ALTER TABLE ONLY public."Studente" DROP CONSTRAINT "unique_codiceFiscaleStudente";
       public            postgres    false    217            �           2606    16667    Gestore unique_nome 
   CONSTRAINT     P   ALTER TABLE ONLY public."Gestore"
    ADD CONSTRAINT unique_nome UNIQUE (nome);
 ?   ALTER TABLE ONLY public."Gestore" DROP CONSTRAINT unique_nome;
       public            postgres    false    209            �           2606    16669    Gestore unique_telefono 
   CONSTRAINT     X   ALTER TABLE ONLY public."Gestore"
    ADD CONSTRAINT unique_telefono UNIQUE (telefono);
 C   ALTER TABLE ONLY public."Gestore" DROP CONSTRAINT unique_telefono;
       public            postgres    false    209            �           2606    16939    Corso unique_titolo 
   CONSTRAINT     R   ALTER TABLE ONLY public."Corso"
    ADD CONSTRAINT unique_titolo UNIQUE (titolo);
 ?   ALTER TABLE ONLY public."Corso" DROP CONSTRAINT unique_titolo;
       public            postgres    false    211            �           2606    16454    Utente utente_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public."Utente"
    ADD CONSTRAINT utente_pkey PRIMARY KEY ("codiceFiscale");
 >   ALTER TABLE ONLY public."Utente" DROP CONSTRAINT utente_pkey;
       public            postgres    false    213            �           2620    16957 $   AreaTematica assegna_codAreaTematica    TRIGGER     �   CREATE TRIGGER "assegna_codAreaTematica" BEFORE INSERT ON public."AreaTematica" FOR EACH ROW EXECUTE FUNCTION public."generazionecodAreaTematica"();
 A   DROP TRIGGER "assegna_codAreaTematica" ON public."AreaTematica";
       public          postgres    false    240    220            �           2620    16962    Corso assegna_codCorso    TRIGGER        CREATE TRIGGER "assegna_codCorso" BEFORE INSERT ON public."Corso" FOR EACH ROW EXECUTE FUNCTION public."generatecodLezione"();
 3   DROP TRIGGER "assegna_codCorso" ON public."Corso";
       public          postgres    false    253    211            �           2620    16910    Gestore assegna_codGestore    TRIGGER     �   CREATE TRIGGER "assegna_codGestore" BEFORE INSERT ON public."Gestore" FOR EACH ROW EXECUTE FUNCTION public."generatecodGestore"();
 7   DROP TRIGGER "assegna_codGestore" ON public."Gestore";
       public          postgres    false    209    251            �           2620    16966    Lezione assegna_codLezione    TRIGGER     �   CREATE TRIGGER "assegna_codLezione" BEFORE INSERT ON public."Lezione" FOR EACH ROW EXECUTE FUNCTION public."generatecodLezione"();
 7   DROP TRIGGER "assegna_codLezione" ON public."Lezione";
       public          postgres    false    253    216            �           2620    16956    Operatore assegna_codOperatore    TRIGGER     �   CREATE TRIGGER "assegna_codOperatore" BEFORE INSERT ON public."Operatore" FOR EACH ROW EXECUTE FUNCTION public."generatecodOperatore"();
 ;   DROP TRIGGER "assegna_codOperatore" ON public."Operatore";
       public          postgres    false    214    234            �           2620    16816    Studente assegna_codStudente    TRIGGER     �   CREATE TRIGGER "assegna_codStudente" BEFORE INSERT ON public."Studente" FOR EACH ROW EXECUTE FUNCTION public."generatecodStudente"();
 9   DROP TRIGGER "assegna_codStudente" ON public."Studente";
       public          postgres    false    217    239            �           2620    17029    Gestore default_descrizione    TRIGGER     �   CREATE TRIGGER default_descrizione BEFORE INSERT ON public."Gestore" FOR EACH ROW EXECUTE FUNCTION public."defaultvalueDescrizione"();
 6   DROP TRIGGER default_descrizione ON public."Gestore";
       public          postgres    false    254    209            �           2620    16964 $   AreaTematica resetta_codAreaTematica    TRIGGER     �   CREATE TRIGGER "resetta_codAreaTematica" AFTER DELETE ON public."AreaTematica" FOR EACH ROW EXECUTE FUNCTION public."resetseqAreaTematica"();
 A   DROP TRIGGER "resetta_codAreaTematica" ON public."AreaTematica";
       public          postgres    false    220    233            �           2620    16963    Corso resetta_codCorso    TRIGGER     y   CREATE TRIGGER "resetta_codCorso" AFTER DELETE ON public."Corso" FOR EACH ROW EXECUTE FUNCTION public."resetseqCorso"();
 3   DROP TRIGGER "resetta_codCorso" ON public."Corso";
       public          postgres    false    241    211            �           2620    16911    Gestore resetta_codGestore    TRIGGER        CREATE TRIGGER "resetta_codGestore" AFTER DELETE ON public."Gestore" FOR EACH ROW EXECUTE FUNCTION public."resetseqGestore"();
 7   DROP TRIGGER "resetta_codGestore" ON public."Gestore";
       public          postgres    false    209    250            �           2620    16959    Lezione resetta_codLezione    TRIGGER        CREATE TRIGGER "resetta_codLezione" AFTER DELETE ON public."Lezione" FOR EACH ROW EXECUTE FUNCTION public."resetseqLezione"();
 7   DROP TRIGGER "resetta_codLezione" ON public."Lezione";
       public          postgres    false    216    231            �           2620    16958    Operatore resetta_codOperatore    TRIGGER     �   CREATE TRIGGER "resetta_codOperatore" AFTER DELETE ON public."Operatore" FOR EACH ROW EXECUTE FUNCTION public."resetseqOperatore"();
 ;   DROP TRIGGER "resetta_codOperatore" ON public."Operatore";
       public          postgres    false    232    214            �           2620    16822    Studente resetta_codStudente    TRIGGER     �   CREATE TRIGGER "resetta_codStudente" AFTER DELETE ON public."Studente" FOR EACH ROW EXECUTE FUNCTION public."resetseqStudente"();
 9   DROP TRIGGER "resetta_codStudente" ON public."Studente";
       public          postgres    false    235    217            �           2606    16680 "   Operatore references codiceFiscale    FK CONSTRAINT     �   ALTER TABLE ONLY public."Operatore"
    ADD CONSTRAINT "references codiceFiscale" FOREIGN KEY ("codiceFiscale") REFERENCES public."Utente"("codiceFiscale") ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 P   ALTER TABLE ONLY public."Operatore" DROP CONSTRAINT "references codiceFiscale";
       public          postgres    false    3262    213    214            �           2606    16749 %   Appartiene references_codAreaTematica    FK CONSTRAINT     �   ALTER TABLE ONLY public."Appartiene"
    ADD CONSTRAINT "references_codAreaTematica" FOREIGN KEY ("codAreaTematica") REFERENCES public."AreaTematica"("codAreaTematica") ON UPDATE CASCADE ON DELETE CASCADE;
 S   ALTER TABLE ONLY public."Appartiene" DROP CONSTRAINT "references_codAreaTematica";
       public          postgres    false    3274    220    221            �           2606    16633    Iscritti references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Iscritti"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 J   ALTER TABLE ONLY public."Iscritti" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    3256    215    211            �           2606    16638    Lezione references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Lezione"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 I   ALTER TABLE ONLY public."Lezione" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    216    211    3256            �           2606    16643    Statistiche references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Statistiche"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 M   ALTER TABLE ONLY public."Statistiche" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    211    3256    218            �           2606    16754    Appartiene references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Appartiene"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 L   ALTER TABLE ONLY public."Appartiene" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    3256    221    211            �           2606    16657    Corso references_codGestore    FK CONSTRAINT     �   ALTER TABLE ONLY public."Corso"
    ADD CONSTRAINT "references_codGestore" FOREIGN KEY ("codGestore") REFERENCES public."Gestore"("codGestore") ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 I   ALTER TABLE ONLY public."Corso" DROP CONSTRAINT "references_codGestore";
       public          postgres    false    211    209    3250            �           2606    17011    Sede references_codGestore    FK CONSTRAINT     �   ALTER TABLE ONLY public."Sede"
    ADD CONSTRAINT "references_codGestore" FOREIGN KEY ("codGestore") REFERENCES public."Gestore"("codGestore") ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 H   ALTER TABLE ONLY public."Sede" DROP CONSTRAINT "references_codGestore";
       public          postgres    false    3250    209    210            �           2606    16736 "   Prenotazioni references_codLezione    FK CONSTRAINT     �   ALTER TABLE ONLY public."Prenotazioni"
    ADD CONSTRAINT "references_codLezione" FOREIGN KEY ("codLezione") REFERENCES public."Lezione"("codLezione") NOT VALID;
 P   ALTER TABLE ONLY public."Prenotazioni" DROP CONSTRAINT "references_codLezione";
       public          postgres    false    3268    216    219            �           2606    16800 #   Prenotazioni references_codStudente    FK CONSTRAINT     �   ALTER TABLE ONLY public."Prenotazioni"
    ADD CONSTRAINT "references_codStudente" FOREIGN KEY ("codStudente") REFERENCES public."Studente"("codStudente") ON UPDATE CASCADE ON DELETE CASCADE;
 Q   ALTER TABLE ONLY public."Prenotazioni" DROP CONSTRAINT "references_codStudente";
       public          postgres    false    219    3270    217            �           2606    16707 !   Studente references_codiceFiscale    FK CONSTRAINT     �   ALTER TABLE ONLY public."Studente"
    ADD CONSTRAINT "references_codiceFiscale" FOREIGN KEY ("codiceFiscale") REFERENCES public."Utente"("codiceFiscale") ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 O   ALTER TABLE ONLY public."Studente" DROP CONSTRAINT "references_codiceFiscale";
       public          postgres    false    213    3262    217            �           2606    16771    Utente references_email    FK CONSTRAINT     �   ALTER TABLE ONLY public."Utente"
    ADD CONSTRAINT references_email FOREIGN KEY (email) REFERENCES public."Autenticazione"(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 C   ALTER TABLE ONLY public."Utente" DROP CONSTRAINT references_email;
       public          postgres    false    3260    213    212            �           2606    16967    Gestore references_email    FK CONSTRAINT     �   ALTER TABLE ONLY public."Gestore"
    ADD CONSTRAINT references_email FOREIGN KEY (email) REFERENCES public."Autenticazione"(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 D   ALTER TABLE ONLY public."Gestore" DROP CONSTRAINT references_email;
       public          postgres    false    209    3260    212                  x������ � �      ~      x������ � �      v   B   x�KL�/.����KN-N,J��wH�M���K���tL�/(�/K4�JDWed``��,ohd����� ��      u      x������ � �      s   V   x�sL�/.���I-.��K-..�KTpI-N.ʬ���K�4666745�t7P �D�����Ģļ|##��������\�=... � 3      y      x������ � �      z      x������ � �      x      x������ � �      }      x������ � �      t   '   x��s����tv�	s�s�45�􃈸(�W� �}�      |      x������ � �      {      x������ � �      w   V   x�KL�/.����KN-N,J��wH�M���K���t��q:C�8��tLt�L8�KJ�2A�����A�!~F.F&\1z\\\ ��     