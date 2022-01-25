PGDMP                          z            Project    14.1    14.1 w    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16398    Project    DATABASE     e   CREATE DATABASE "Project" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Italian_Italy.1252';
    DROP DATABASE "Project";
                postgres    false            �           0    0    DATABASE "Project"    COMMENT     P   COMMENT ON DATABASE "Project" IS 'Database per il Proggetto di Basi di Dati I';
                   postgres    false    3507                       1247    16718    sesso    DOMAIN     }   CREATE DOMAIN public.sesso AS character(1)
	CONSTRAINT sesso_check CHECK (((VALUE = 'M'::bpchar) OR (VALUE = 'F'::bpchar)));
    DROP DOMAIN public.sesso;
       public          postgres    false                       1255    17249    calcola_statistiche()    FUNCTION     �  CREATE FUNCTION public.calcola_statistiche() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    ris_stat "Statistiche"%RowType;
    
BEGIN
    SELECT MIN(numero_presenti),MAX(numero_presenti),avg(numero_presenti) INTO ris_stat
    FROM(SELECT "codLezione",count(*) AS "numero_presenti"
         FROM "Prenotazioni" NATURAL JOIN "Lezione"
         WHERE "codCorso" IN(SELECT "codCorso"
                             FROM "Lezione"
                             WHERE "codLezione"=NEW."codLezione")
           AND "Presente"=true
         GROUP BY "codLezione")as results;

    UPDATE "Statistiche" SET "presenzeMassime"=ris_stat."presenzeMassime",
                             "presenzeMinime"=ris_stat."presenzeMinime",
                             "presenzeMedie"=ris_stat."presenzeMedie"
    WHERE "codCorso" IN(SELECT "codCorso"
                        FROM "Lezione"
                        WHERE "codLezione"=NEW."codLezione");
    RETURN new;
END;
$$;
 ,   DROP FUNCTION public.calcola_statistiche();
       public          postgres    false                       1255    17164    checkcorsopublic()    FUNCTION     n  CREATE FUNCTION public.checkcorsopublic() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE
    corso "Corso"%rowtype;
BEGIN
    SELECT *
    FROM "Corso"
    INTO corso
    WHERE "codCorso"= NEW."codCorso";

    if(corso."Privato"=false)then
    NEW."Richiesta"=true;
    RETURN NEW;
    else
        NEW."Richiesta"=false;
        RETURN NEW;
end if;
END;
$$;
 )   DROP FUNCTION public.checkcorsopublic();
       public          postgres    false                       1255    17186    checkoperatore()    FUNCTION     �   CREATE FUNCTION public.checkoperatore() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
DELETE FROM "Operatore"
WHERE "codOperatore" NOT IN(SELECT "codOperatore"
                            FROM "Coordina");
RETURN OLD;
END;$$;
 '   DROP FUNCTION public.checkoperatore();
       public          postgres    false                       1255    17232    checkstudente()    FUNCTION     �   CREATE FUNCTION public.checkstudente() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
DELETE FROM "Studente"
WHERE "codStudente" NOT IN(SELECT "codStudente"
                            FROM "Iscritti");
RETURN OLD;
END;$$;
 &   DROP FUNCTION public.checkstudente();
       public          postgres    false                       1255    17251    crea_statistiche()    FUNCTION     �   CREATE FUNCTION public.crea_statistiche() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
INSERT INTO "Statistiche" VALUES(0,0,0,0,NEW."codCorso");
RETURN new;
END$$;
 )   DROP FUNCTION public.crea_statistiche();
       public          postgres    false                       1255    17028    defaultvalueDescrizione()    FUNCTION     �   CREATE FUNCTION public."defaultvalueDescrizione"() RETURNS trigger
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
       public          postgres    false                        1255    16953    generatecodLezione()    FUNCTION     h  CREATE FUNCTION public."generatecodLezione"() RETURNS trigger
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
       public          postgres    false                       1255    17140    noprimarykeyviolation()    FUNCTION       CREATE FUNCTION public.noprimarykeyviolation() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
            if NOT EXISTS (select "tag" FROM "AreaTematica" WHERE "tag"= new."tag" ) then
                RETURN NEW;
            end if;
RETURN null;
END;$$;
 .   DROP FUNCTION public.noprimarykeyviolation();
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
   Appartiene    TABLE     s   CREATE TABLE public."Appartiene" (
    "codCorso" character(8) NOT NULL,
    tag character varying(30) NOT NULL
);
     DROP TABLE public."Appartiene";
       public         heap    postgres    false            �            1259    16741    AreaTematica    TABLE     O   CREATE TABLE public."AreaTematica" (
    tag character varying(30) NOT NULL
);
 "   DROP TABLE public."AreaTematica";
       public         heap    postgres    false            �            1259    16435    Autenticazione    TABLE     �   CREATE TABLE public."Autenticazione" (
    email character varying(60) NOT NULL,
    password character varying(30) NOT NULL
);
 $   DROP TABLE public."Autenticazione";
       public         heap    postgres    false            �            1259    17033    Coordina    TABLE     �   CREATE TABLE public."Coordina" (
    "codCorso" character(8) NOT NULL,
    "codOperatore" character(8) NOT NULL,
    "Richiesta" boolean DEFAULT false NOT NULL
);
    DROP TABLE public."Coordina";
       public         heap    postgres    false            �            1259    16424    Corso    TABLE     �  CREATE TABLE public."Corso" (
    titolo character varying(50) NOT NULL,
    descrizione character varying(200) DEFAULT 'Nessuna descrizione'::character varying,
    "iscrizioniMassime" integer NOT NULL,
    "tassoPresenzeMinime" integer NOT NULL,
    "codCorso" character(8) NOT NULL,
    "codGestore" character(8) NOT NULL,
    "numeroLezioni" integer NOT NULL,
    "Privato" boolean NOT NULL
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
    "Idoneo" boolean DEFAULT false NOT NULL,
    "Richiesta" boolean NOT NULL
);
    DROP TABLE public."Iscritti";
       public         heap    postgres    false            �            1259    16550    Lezione    TABLE     `  CREATE TABLE public."Lezione" (
    titolo character varying(50) NOT NULL,
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
       public         heap    postgres    false            �            1259    16728    Prenotazioni    TABLE     �   CREATE TABLE public."Prenotazioni" (
    "codLezione" character(8) NOT NULL,
    "codStudente" character(8) NOT NULL,
    "Presente" boolean DEFAULT false NOT NULL
);
 "   DROP TABLE public."Prenotazioni";
       public         heap    postgres    false            �            1259    16414    Sede    TABLE     �   CREATE TABLE public."Sede" (
    "città" character varying(35) NOT NULL,
    via character varying(30) NOT NULL,
    civico character varying(5) NOT NULL,
    provincia character varying(22) NOT NULL,
    "codGestore" character(8) NOT NULL
);
    DROP TABLE public."Sede";
       public         heap    postgres    false            �            1259    16574    Statistiche    TABLE     �   CREATE TABLE public."Statistiche" (
    "presenzeMinime" integer NOT NULL,
    "presenzeMassime" integer NOT NULL,
    "presenzeMedie" real NOT NULL,
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
    nome character varying(30) NOT NULL,
    cognome character varying(30) NOT NULL,
    "dataNascita" date NOT NULL,
    "comunediNascita" character varying(35) NOT NULL,
    sesso public.sesso NOT NULL,
    "codiceFiscale" character varying(16) NOT NULL
);
    DROP TABLE public."Utente";
       public         heap    postgres    false    895            �            1259    17119    logingestore    VIEW       CREATE VIEW public.logingestore AS
 SELECT "Autenticazione".email,
    "Autenticazione".password,
    "Gestore".nome,
    "Gestore".descrizione,
    "Gestore".telefono,
    "Gestore"."codGestore"
   FROM (public."Autenticazione"
     JOIN public."Gestore" USING (email));
    DROP VIEW public.logingestore;
       public          postgres    false    212    209    209    209    209    209    212            �            1259    17115    loginutente    VIEW     B  CREATE VIEW public.loginutente AS
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
       public          postgres    false    213    213    213    213    213    213    213    212    212    895            �            1259    17158    operatoricorsi    VIEW     �  CREATE VIEW public.operatoricorsi AS
 SELECT "codOperatore",
    "Utente"."codiceFiscale",
    "Utente".email,
    "Utente".nome,
    "Utente".cognome,
    "Utente"."dataNascita",
    "Utente"."comunediNascita",
    "Utente".sesso,
    "Coordina"."codCorso",
    "Coordina"."Richiesta"
   FROM ((public."Utente"
     JOIN public."Operatore" USING ("codiceFiscale"))
     JOIN public."Coordina" USING ("codOperatore"));
 !   DROP VIEW public.operatoricorsi;
       public          postgres    false    227    227    227    214    214    213    213    213    213    213    213    213    895            �            1259    17212    parametriricerca    VIEW     �  CREATE VIEW public.parametriricerca AS
 SELECT "Corso"."codGestore",
    "Corso".descrizione,
    "Corso".titolo,
    "Corso"."codCorso",
    "Corso"."Privato",
    "Corso"."numeroLezioni",
    "Corso"."tassoPresenzeMinime",
    "Corso"."iscrizioniMassime",
    "Gestore".nome,
    "Sede"."città",
    "Sede".provincia
   FROM ((public."Corso"
     JOIN public."Gestore" USING ("codGestore"))
     JOIN public."Sede" USING ("codGestore"));
 #   DROP VIEW public.parametriricerca;
       public          postgres    false    211    211    211    211    211    211    211    211    210    210    210    209    209            �            1259    17262    ris_stat    TABLE     R   CREATE TABLE public.ris_stat (
    min bigint,
    max bigint,
    avg numeric
);
    DROP TABLE public.ris_stat;
       public         heap    postgres    false            �            1259    16960    seqcorso    SEQUENCE     u   CREATE SEQUENCE public.seqcorso
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
       public          postgres    false            �            1259    17227    studenticorsi    VIEW     �  CREATE VIEW public.studenticorsi AS
 SELECT "codStudente",
    "Studente"."codiceFiscale",
    "Utente".email,
    "Utente".nome,
    "Utente".cognome,
    "Utente"."dataNascita",
    "Utente"."comunediNascita",
    "Utente".sesso,
    "Iscritti"."codCorso",
    "Iscritti"."Idoneo",
    "Iscritti"."Richiesta"
   FROM ((public."Studente"
     JOIN public."Utente" USING ("codiceFiscale"))
     JOIN public."Iscritti" USING ("codStudente"));
     DROP VIEW public.studenticorsi;
       public          postgres    false    215    217    217    215    215    215    213    213    213    213    213    213    213    895            �          0    16746 
   Appartiene 
   TABLE DATA           7   COPY public."Appartiene" ("codCorso", tag) FROM stdin;
    public          postgres    false    221   F�       �          0    16741    AreaTematica 
   TABLE DATA           -   COPY public."AreaTematica" (tag) FROM stdin;
    public          postgres    false    220   o�       �          0    16435    Autenticazione 
   TABLE DATA           ;   COPY public."Autenticazione" (email, password) FROM stdin;
    public          postgres    false    212   ��       �          0    17033    Coordina 
   TABLE DATA           M   COPY public."Coordina" ("codCorso", "codOperatore", "Richiesta") FROM stdin;
    public          postgres    false    227   �       �          0    16424    Corso 
   TABLE DATA           �   COPY public."Corso" (titolo, descrizione, "iscrizioniMassime", "tassoPresenzeMinime", "codCorso", "codGestore", "numeroLezioni", "Privato") FROM stdin;
    public          postgres    false    211   F�       �          0    16405    Gestore 
   TABLE DATA           U   COPY public."Gestore" (nome, descrizione, telefono, "codGestore", email) FROM stdin;
    public          postgres    false    209   �       �          0    16532    Iscritti 
   TABLE DATA           V   COPY public."Iscritti" ("codCorso", "codStudente", "Idoneo", "Richiesta") FROM stdin;
    public          postgres    false    215   �       �          0    16550    Lezione 
   TABLE DATA           k   COPY public."Lezione" (titolo, descrizione, "dataoraInizio", durata, "codLezione", "codCorso") FROM stdin;
    public          postgres    false    216   �       �          0    16460 	   Operatore 
   TABLE DATA           F   COPY public."Operatore" ("codOperatore", "codiceFiscale") FROM stdin;
    public          postgres    false    214   ��       �          0    16728    Prenotazioni 
   TABLE DATA           Q   COPY public."Prenotazioni" ("codLezione", "codStudente", "Presente") FROM stdin;
    public          postgres    false    219   Ϋ       �          0    16414    Sede 
   TABLE DATA           P   COPY public."Sede" ("città", via, civico, provincia, "codGestore") FROM stdin;
    public          postgres    false    210   �       �          0    16574    Statistiche 
   TABLE DATA           �   COPY public."Statistiche" ("presenzeMinime", "presenzeMassime", "presenzeMedie", "percentualeRempimento", "codCorso") FROM stdin;
    public          postgres    false    218   V�       �          0    16561    Studente 
   TABLE DATA           D   COPY public."Studente" ("codStudente", "codiceFiscale") FROM stdin;
    public          postgres    false    217   �       �          0    16450    Utente 
   TABLE DATA           r   COPY public."Utente" (email, nome, cognome, "dataNascita", "comunediNascita", sesso, "codiceFiscale") FROM stdin;
    public          postgres    false    213   Ĭ       �          0    17262    ris_stat 
   TABLE DATA           1   COPY public.ris_stat (min, max, avg) FROM stdin;
    public          postgres    false    233   j�       �           0    0    seqcorso    SEQUENCE SET     6   SELECT pg_catalog.setval('public.seqcorso', 2, true);
          public          postgres    false    226            �           0    0 
   seqgestore    SEQUENCE SET     8   SELECT pg_catalog.setval('public.seqgestore', 0, true);
          public          postgres    false    223            �           0    0 
   seqlezione    SEQUENCE SET     8   SELECT pg_catalog.setval('public.seqlezione', 2, true);
          public          postgres    false    225            �           0    0    seqoperatore    SEQUENCE SET     :   SELECT pg_catalog.setval('public.seqoperatore', 0, true);
          public          postgres    false    224            �           0    0    seqstudente    SEQUENCE SET     9   SELECT pg_catalog.setval('public.seqstudente', 1, true);
          public          postgres    false    222            �           2606    17063    AreaTematica AreaTematica_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY public."AreaTematica"
    ADD CONSTRAINT "AreaTematica_pkey" PRIMARY KEY (tag);
 L   ALTER TABLE ONLY public."AreaTematica" DROP CONSTRAINT "AreaTematica_pkey";
       public            postgres    false    220            �           2606    17219 +   Coordina Coordina_codCorso_codOperatore_key 
   CONSTRAINT     �   ALTER TABLE ONLY public."Coordina"
    ADD CONSTRAINT "Coordina_codCorso_codOperatore_key" UNIQUE ("codCorso", "codOperatore");
 Y   ALTER TABLE ONLY public."Coordina" DROP CONSTRAINT "Coordina_codCorso_codOperatore_key";
       public            postgres    false    227    227            �           2606    17221 *   Iscritti Iscritti_codStudente_codCorso_key 
   CONSTRAINT     ~   ALTER TABLE ONLY public."Iscritti"
    ADD CONSTRAINT "Iscritti_codStudente_codCorso_key" UNIQUE ("codStudente", "codCorso");
 X   ALTER TABLE ONLY public."Iscritti" DROP CONSTRAINT "Iscritti_codStudente_codCorso_key";
       public            postgres    false    215    215            �           2606    17237 4   Prenotazioni Prenotazioni_codLezione_codStudente_key 
   CONSTRAINT     �   ALTER TABLE ONLY public."Prenotazioni"
    ADD CONSTRAINT "Prenotazioni_codLezione_codStudente_key" UNIQUE ("codLezione", "codStudente");
 b   ALTER TABLE ONLY public."Prenotazioni" DROP CONSTRAINT "Prenotazioni_codLezione_codStudente_key";
       public            postgres    false    219    219            �           2606    16760 "   Autenticazione autenticazione_pkey 
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
       public            postgres    false    217            �           2606    17172    Sede unique_codGestore 
   CONSTRAINT     ]   ALTER TABLE ONLY public."Sede"
    ADD CONSTRAINT "unique_codGestore" UNIQUE ("codGestore");
 D   ALTER TABLE ONLY public."Sede" DROP CONSTRAINT "unique_codGestore";
       public            postgres    false    210            �           2606    16945 '   Operatore unique_codiceFiscaleOperatore 
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
       public            postgres    false    209            �           2606    17110    Gestore unique_telefono 
   CONSTRAINT     X   ALTER TABLE ONLY public."Gestore"
    ADD CONSTRAINT unique_telefono UNIQUE (telefono);
 C   ALTER TABLE ONLY public."Gestore" DROP CONSTRAINT unique_telefono;
       public            postgres    false    209            �           2606    16454    Utente utente_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public."Utente"
    ADD CONSTRAINT utente_pkey PRIMARY KEY ("codiceFiscale");
 >   ALTER TABLE ONLY public."Utente" DROP CONSTRAINT utente_pkey;
       public            postgres    false    213            �           2620    17047    Corso assegna_codCorso    TRIGGER     }   CREATE TRIGGER "assegna_codCorso" BEFORE INSERT ON public."Corso" FOR EACH ROW EXECUTE FUNCTION public."generatecodCorso"();
 3   DROP TRIGGER "assegna_codCorso" ON public."Corso";
       public          postgres    false    211    255            �           2620    16910    Gestore assegna_codGestore    TRIGGER     �   CREATE TRIGGER "assegna_codGestore" BEFORE INSERT ON public."Gestore" FOR EACH ROW EXECUTE FUNCTION public."generatecodGestore"();
 7   DROP TRIGGER "assegna_codGestore" ON public."Gestore";
       public          postgres    false    254    209                       2620    16966    Lezione assegna_codLezione    TRIGGER     �   CREATE TRIGGER "assegna_codLezione" BEFORE INSERT ON public."Lezione" FOR EACH ROW EXECUTE FUNCTION public."generatecodLezione"();
 7   DROP TRIGGER "assegna_codLezione" ON public."Lezione";
       public          postgres    false    256    216                        2620    16956    Operatore assegna_codOperatore    TRIGGER     �   CREATE TRIGGER "assegna_codOperatore" BEFORE INSERT ON public."Operatore" FOR EACH ROW EXECUTE FUNCTION public."generatecodOperatore"();
 ;   DROP TRIGGER "assegna_codOperatore" ON public."Operatore";
       public          postgres    false    237    214                       2620    16816    Studente assegna_codStudente    TRIGGER     �   CREATE TRIGGER "assegna_codStudente" BEFORE INSERT ON public."Studente" FOR EACH ROW EXECUTE FUNCTION public."generatecodStudente"();
 9   DROP TRIGGER "assegna_codStudente" ON public."Studente";
       public          postgres    false    242    217            �           2620    17054    Corso default_descrizione    TRIGGER     �   CREATE TRIGGER default_descrizione BEFORE INSERT ON public."Corso" FOR EACH ROW EXECUTE FUNCTION public."defaultvalueDescrizione"();
 4   DROP TRIGGER default_descrizione ON public."Corso";
       public          postgres    false    257    211            �           2620    17029    Gestore default_descrizione    TRIGGER     �   CREATE TRIGGER default_descrizione BEFORE INSERT ON public."Gestore" FOR EACH ROW EXECUTE FUNCTION public."defaultvalueDescrizione"();
 6   DROP TRIGGER default_descrizione ON public."Gestore";
       public          postgres    false    257    209            �           2620    17252    Corso default_statistiche    TRIGGER     {   CREATE TRIGGER default_statistiche AFTER INSERT ON public."Corso" FOR EACH ROW EXECUTE FUNCTION public.crea_statistiche();
 4   DROP TRIGGER default_statistiche ON public."Corso";
       public          postgres    false    211    262            	           2620    17187    Coordina delete_operatore    TRIGGER     y   CREATE TRIGGER delete_operatore AFTER DELETE ON public."Coordina" FOR EACH ROW EXECUTE FUNCTION public.checkoperatore();
 4   DROP TRIGGER delete_operatore ON public."Coordina";
       public          postgres    false    227    260                       2620    17233    Iscritti delete_studente    TRIGGER     w   CREATE TRIGGER delete_studente AFTER DELETE ON public."Iscritti" FOR EACH ROW EXECUTE FUNCTION public.checkstudente();
 3   DROP TRIGGER delete_studente ON public."Iscritti";
       public          postgres    false    261    215                       2620    17267 !   Prenotazioni generate_statistiche    TRIGGER     �   CREATE TRIGGER generate_statistiche AFTER UPDATE ON public."Prenotazioni" FOR EACH ROW EXECUTE FUNCTION public.calcola_statistiche();
 <   DROP TRIGGER generate_statistiche ON public."Prenotazioni";
       public          postgres    false    263    219            �           2620    16963    Corso resetta_codCorso    TRIGGER     y   CREATE TRIGGER "resetta_codCorso" AFTER DELETE ON public."Corso" FOR EACH ROW EXECUTE FUNCTION public."resetseqCorso"();
 3   DROP TRIGGER "resetta_codCorso" ON public."Corso";
       public          postgres    false    244    211            �           2620    16911    Gestore resetta_codGestore    TRIGGER        CREATE TRIGGER "resetta_codGestore" AFTER DELETE ON public."Gestore" FOR EACH ROW EXECUTE FUNCTION public."resetseqGestore"();
 7   DROP TRIGGER "resetta_codGestore" ON public."Gestore";
       public          postgres    false    253    209                       2620    16959    Lezione resetta_codLezione    TRIGGER        CREATE TRIGGER "resetta_codLezione" AFTER DELETE ON public."Lezione" FOR EACH ROW EXECUTE FUNCTION public."resetseqLezione"();
 7   DROP TRIGGER "resetta_codLezione" ON public."Lezione";
       public          postgres    false    234    216            �           2620    16958    Operatore resetta_codOperatore    TRIGGER     �   CREATE TRIGGER "resetta_codOperatore" AFTER DELETE ON public."Operatore" FOR EACH ROW EXECUTE FUNCTION public."resetseqOperatore"();
 ;   DROP TRIGGER "resetta_codOperatore" ON public."Operatore";
       public          postgres    false    214    235                       2620    16822    Studente resetta_codStudente    TRIGGER     �   CREATE TRIGGER "resetta_codStudente" AFTER DELETE ON public."Studente" FOR EACH ROW EXECUTE FUNCTION public."resetseqStudente"();
 9   DROP TRIGGER "resetta_codStudente" ON public."Studente";
       public          postgres    false    217    238                       2620    17165    Iscritti set_richiesta    TRIGGER     y   CREATE TRIGGER set_richiesta BEFORE INSERT ON public."Iscritti" FOR EACH ROW EXECUTE FUNCTION public.checkcorsopublic();
 1   DROP TRIGGER set_richiesta ON public."Iscritti";
       public          postgres    false    259    215                       2620    17141    AreaTematica tag_primarykey    TRIGGER     �   CREATE TRIGGER tag_primarykey BEFORE INSERT ON public."AreaTematica" FOR EACH ROW EXECUTE FUNCTION public.noprimarykeyviolation();
 6   DROP TRIGGER tag_primarykey ON public."AreaTematica";
       public          postgres    false    258    220            �           2606    16680 "   Operatore references codiceFiscale    FK CONSTRAINT     �   ALTER TABLE ONLY public."Operatore"
    ADD CONSTRAINT "references codiceFiscale" FOREIGN KEY ("codiceFiscale") REFERENCES public."Utente"("codiceFiscale") ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 P   ALTER TABLE ONLY public."Operatore" DROP CONSTRAINT "references codiceFiscale";
       public          postgres    false    3286    213    214            �           2606    16633    Iscritti references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Iscritti"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 J   ALTER TABLE ONLY public."Iscritti" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    211    215    3282            �           2606    16638    Lezione references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Lezione"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 I   ALTER TABLE ONLY public."Lezione" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    216    211    3282            �           2606    16643    Statistiche references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Statistiche"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 M   ALTER TABLE ONLY public."Statistiche" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    211    218    3282            �           2606    16754    Appartiene references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Appartiene"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 L   ALTER TABLE ONLY public."Appartiene" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    211    3282    221            �           2606    17036    Coordina references_codCorso    FK CONSTRAINT     �   ALTER TABLE ONLY public."Coordina"
    ADD CONSTRAINT "references_codCorso" FOREIGN KEY ("codCorso") REFERENCES public."Corso"("codCorso") ON UPDATE CASCADE ON DELETE CASCADE;
 J   ALTER TABLE ONLY public."Coordina" DROP CONSTRAINT "references_codCorso";
       public          postgres    false    227    211    3282            �           2606    16657    Corso references_codGestore    FK CONSTRAINT     �   ALTER TABLE ONLY public."Corso"
    ADD CONSTRAINT "references_codGestore" FOREIGN KEY ("codGestore") REFERENCES public."Gestore"("codGestore") ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 I   ALTER TABLE ONLY public."Corso" DROP CONSTRAINT "references_codGestore";
       public          postgres    false    209    211    3274            �           2606    17011    Sede references_codGestore    FK CONSTRAINT     �   ALTER TABLE ONLY public."Sede"
    ADD CONSTRAINT "references_codGestore" FOREIGN KEY ("codGestore") REFERENCES public."Gestore"("codGestore") ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 H   ALTER TABLE ONLY public."Sede" DROP CONSTRAINT "references_codGestore";
       public          postgres    false    3274    209    210            �           2606    16736 "   Prenotazioni references_codLezione    FK CONSTRAINT     �   ALTER TABLE ONLY public."Prenotazioni"
    ADD CONSTRAINT "references_codLezione" FOREIGN KEY ("codLezione") REFERENCES public."Lezione"("codLezione") NOT VALID;
 P   ALTER TABLE ONLY public."Prenotazioni" DROP CONSTRAINT "references_codLezione";
       public          postgres    false    219    3294    216            �           2606    17041     Coordina references_codOperatore    FK CONSTRAINT     �   ALTER TABLE ONLY public."Coordina"
    ADD CONSTRAINT "references_codOperatore" FOREIGN KEY ("codOperatore") REFERENCES public."Operatore"("codOperatore") ON UPDATE CASCADE ON DELETE CASCADE;
 N   ALTER TABLE ONLY public."Coordina" DROP CONSTRAINT "references_codOperatore";
       public          postgres    false    227    214    3288            �           2606    16800 #   Prenotazioni references_codStudente    FK CONSTRAINT     �   ALTER TABLE ONLY public."Prenotazioni"
    ADD CONSTRAINT "references_codStudente" FOREIGN KEY ("codStudente") REFERENCES public."Studente"("codStudente") ON UPDATE CASCADE ON DELETE CASCADE;
 Q   ALTER TABLE ONLY public."Prenotazioni" DROP CONSTRAINT "references_codStudente";
       public          postgres    false    3296    219    217            �           2606    16707 !   Studente references_codiceFiscale    FK CONSTRAINT     �   ALTER TABLE ONLY public."Studente"
    ADD CONSTRAINT "references_codiceFiscale" FOREIGN KEY ("codiceFiscale") REFERENCES public."Utente"("codiceFiscale") ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 O   ALTER TABLE ONLY public."Studente" DROP CONSTRAINT "references_codiceFiscale";
       public          postgres    false    3286    213    217            �           2606    16771    Utente references_email    FK CONSTRAINT     �   ALTER TABLE ONLY public."Utente"
    ADD CONSTRAINT references_email FOREIGN KEY (email) REFERENCES public."Autenticazione"(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 C   ALTER TABLE ONLY public."Utente" DROP CONSTRAINT references_email;
       public          postgres    false    3284    213    212            �           2606    16967    Gestore references_email    FK CONSTRAINT     �   ALTER TABLE ONLY public."Gestore"
    ADD CONSTRAINT references_email FOREIGN KEY (email) REFERENCES public."Autenticazione"(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;
 D   ALTER TABLE ONLY public."Gestore" DROP CONSTRAINT references_email;
       public          postgres    false    3284    212    209            �           2606    17064    Appartiene references_tag    FK CONSTRAINT     �   ALTER TABLE ONLY public."Appartiene"
    ADD CONSTRAINT references_tag FOREIGN KEY (tag) REFERENCES public."AreaTematica"(tag) NOT VALID;
 E   ALTER TABLE ONLY public."Appartiene" DROP CONSTRAINT references_tag;
       public          postgres    false    220    3302    221            �      x�s6R μĂ��L�=... 1&t      �   <   x��1� ����E.!0Do��w+�6nR�!V�c]�/w��j%�����Q�      �   S   x�u�A
� �usq��v�61P'F��A������ե<`�K�T��-[����+x��qL�Q����d�����mI+�      �      x�s6R N(��+F��� 2�      �   )   x�K�LO�����4�440�t6R Nw(È3�+F��� �G�      �   Y   x�sL�/.���I-.��K-..�KTpI-N.ʬ���K�46667��0�0�t7P �D�&����Ģļ|##��������\�=... CP�      �       x�s6R �`(#����.j�$���� �(�      �   u   x����S��W(���4202�50�52S04�20 "N��@8�� ��Ă��ԒļDZaZѵ��s����%*��'eVe��"L�P02�26�22�a1�ݔ=... ��(-      �   !   x��7���s�q50q40w4�0v����� KB-      �   *   x��1R �`C(������L� �:�X���X� Xe�      �   >   x�v�qT�u�tT�qTp2B�9�}��\�]9M���8��}<9���+F��� ���      �      x�3�4BNg#0������ !j�      �   5   x�6�tr�30r12q7�4��
6���s�q50q40w4�0v����� �	�      �   �   x�M�A
�0@���^ 2�)���-�E��0	h���`�������`���a����M���ނ:��@ 
��	>��=֐=۲kP�B��_lh���BcH��7m7��g�QrL8���Bk�UԨ�]W۸n�V(�J^N{ǌ�/��5      �      x����!�=... ��     