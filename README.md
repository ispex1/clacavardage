# Projet clavardage

### TODO List

1. Bugs a regler
    - En boucle : 
   
            [SQLITE_CONSTRAINT_NOTNULL] A NOT NULL constraint failed (NOT NULL constraint failed: id_192_168_1_84.message)
            <Session | 33 >  Message recu : null
    - bugs a la chaine quand l'utilisateur distant ferme la conv
    - Lorsque essai de reconnexion :

          java.net.BindException: Address already in use: bind
    - OK Bug messages imbriques
    - Lorsque quelqu'un initialise une session, on a pas toujours la fenetre qui se met automatiquement sur le chat
    - quand on change de de pseudo on voit l'ancien de lautre, la liste ne sactualise pas
    - quand reception de message : bug
        
          Exception in thread "Thread-6" java.lang.IllegalStateException: Not on FX application thread; currentThread = Thread-6
