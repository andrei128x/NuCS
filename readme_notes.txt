
*** NOTE : this is only a tracker file; for the project Architecture and Requirements, visit the WIKI page ***
*** WIKI : https://github.com/andrei128x/NuCS/wiki                                                         ***


1.	Cerinte de baza (sinteza) :

	-	github
	-	API REST - 1 pct bonus
	-	HTML5 valid / Ajax
	-	SQL pentru SGBD
	-	programmer manual, APIs etc
	-	templates/config
	-	Cross Site Script/SQL injection
	
	bonus
	
	-oop
	
	
2.	Arhitectura
	-	baza de date Oracle, accesata prin functii/procedure disponibile intr-un pachet
	-	aplicatie server-side, JAVA, ce are ca scop achizitionarea datelor de pe site-urile de stiri, parsarea acestora, introducerea in baza de date, si livrarea catre client prin API-uri REST
	
	-	aplicatia WEB client, sub forma de cod HTML5 + Ajax( jQuery framework )
	
3. task activity

	- front-end:
		- instalare si configurare template-ului BootStrap
		- crearea interfetei HTML + Knockout.js
		- creare aplicatie JavaScript pentru testare functionalitati ( responsive interface + Data Binging )
		
		Spent resources: cca. 80 ore
		
	- back end
		- crearea aplicatiei stand-alone (Java) pentru parsarea RSS-urilor ( Reddit, TheHackerNews, InfoQ )
		
		
	
		Spent resources ( approx. , continuare ):
		-	20 ore - RSS Collector
		-	10 ore - tomcat + demo servlet + first DB connection from servlet
		-	8 ore	- Oracle SQL + RSS Collector code : INSERT capability + nucs_articles tables
		
		-	6 ore - troubleshooting Tomcat+Oracle connection on the local laptop, after email response.
		-	~15 ore PL/SQL application
		-	~10 ore Java servlets+collector ( REST API servlets )
		-	15 ore	HTML front-end + Knockout.js dynamic DOM manipulation
		
		
		Total(ne-final): ~164 ore
