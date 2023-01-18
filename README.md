# Projet clavardage

### TODO List

1. Bugs a regler
    - En boucle : 
   
            [SQLITE_CONSTRAINT_NOTNULL] A NOT NULL constraint failed (NOT NULL constraint failed: id_192_168_1_84.message)
            <Session | 33 >  Message recu : null
      - Lorsque essai de reconnexion :

            java.net.BindException: Address already in use: bind
      - Bug messages imbriques
      - Lorsque quelqu'un initialise une session, on a pas toujours la fenetre qui se met automatiquement sur le chat
      - 