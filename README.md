# Projet clavardage

### TODO List

AJOUTER TESTS JENKINS + FINIR LA DOC 

1. Bugs a regler
    - En boucle : 

            [SQLITE_CONSTRAINT_NOTNULL] A NOT NULL constraint failed (NOT NULL constraint failed: id_192_168_1_84.message)
            <Session | 33 >  Message recu : null
    - Quand l'utilisateur distant ferme la conv
    
          Exception in thread "Thread-6" java.lang.NullPointerException: Cannot invoke "String.split(String)" because "data" is null
          at network.TCPSession.run(TCPSession.java:113)
    - Lorsque essai de reconnexion :

          java.net.BindException: Address already in use: bind
    
    - OK Bug messages imbriques
    - Lorsque quelqu'un initialise une session, on a pas toujours la fenetre qui se met automatiquement sur le chat
    - quand on change de de pseudo on voit l'ancien de lautre, la liste ne sactualise pas
    - quand reception de message : bug
        
          Exception in thread "Thread-6" java.lang.IllegalStateException: Not on FX application thread; currentThread = Thread-6
    - Lorsqu'on change de pseudo on ne oeut pas rouvrir une session avec userdist, on doit d'abord cliquer sur un autre user, pourquoi ????
    - Lorsquon se deconnecte, le thread est toujours actif en TCP sur le port s'en face
    - 